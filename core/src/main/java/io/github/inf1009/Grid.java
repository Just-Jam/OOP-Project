package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Grid {
    public int score=0;
    
	private final int columns;
    private final int rows;
    private BlockShape.BlockType[][] gridMatrix;
    // Squish factors are used for recyclable blocks.
    private float[][] squishFactors;
    // Flag to indicate if a cell is in the process of being cleared.
    private boolean[][] isClearing;
    
    
    // Array to store active fire animations for cleared rows (only for non recyclable blocks).
    private Array<FireAnimation> fireAnimations;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        gridMatrix = new BlockShape.BlockType[columns][rows];
        squishFactors = new float[columns][rows];
        isClearing = new boolean[columns][rows];
        fireAnimations = new Array<>();
        // Initialize squish factors and clearing flags.
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                squishFactors[col][row] = 1.0f;
                isClearing[col][row] = false;
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public BlockShape.BlockType[][] getGridMatrix() {
        return gridMatrix;
    }

    public void addBlock(int x, int y, BlockShape.BlockType type) {
        gridMatrix[x][y] = type;
    }

    public boolean isOccupied(int x, int y) {
        return gridMatrix[x][y] != null || isClearing[x][y];
    }

    /**
     * Draws the grid. Cells marked as clearing are drawn in 50% transparent grey.
     * For recyclable blocks, the drawn height is based on the squish factor.
     * Also updates and draws any active fire animations.
     */
    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);

        // Draw the categorized grid areas first.
        drawCategorizeAreas(shapeRenderer);

        // Draw blocks (or cells in the process of being cleared).
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (gridMatrix[col][row] != null || isClearing[col][row]) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    
                    // If the cell is being cleared, use 50% transparent grey.
                    if (isClearing[col][row]) {
                        shapeRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
                    } else {
                        // Otherwise, color based on block type.
                        if (gridMatrix[col][row] == BlockShape.BlockType.RECYCLABLE) {
                            shapeRenderer.setColor(Color.GREEN);
                        } else {
                            shapeRenderer.setColor(Color.RED);
                        }
                    }
                    
                    // For recyclable cells, use the squish factor (even during clearing); for non-recyclable, full height.
                    float height = (col < columns / 2) ? squishFactors[col][row] : 1.0f;
                    shapeRenderer.rect(col, row, 1, height);
                    shapeRenderer.end();
                }
            }
        }
        
        // Update fire animations using the delta time.
        float delta = Gdx.graphics.getDeltaTime();
        updateFireAnimations(delta);
        
        // Draw fire animations on top of the grid.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < fireAnimations.size; i++) {
            fireAnimations.get(i).draw(shapeRenderer);
        }
        shapeRenderer.end();
    }

    /**
     * Checks each row. For the left half (assumed RECYCLABLE) it uses the squish animation;
     * for the right half (assumed NON RECYCLABLE) it uses the sequential clearing animation.
     * In both cases, cells are marked as clearing so that they appear grey and 50% transparent.
     * The fire animation is added only for the non recyclable (right) section.
     */
    public void clearRow() {
    	
        for (int row = 0; row < rows; row++) {
            boolean leftSideFull = true;
            boolean rightSideFull = true;
            BlockShape.BlockType leftSideType = null;
            BlockShape.BlockType rightSideType = null;

            // Check the left (green/RECYCLABLE) section.
            for (int col = 0; col < columns / 2; col++) {
                if (gridMatrix[col][row] == null) {
                    leftSideFull = false;
                    break;
                }
                if (leftSideType == null) {
                    leftSideType = gridMatrix[col][row];
                }
                if (gridMatrix[col][row] != leftSideType) {
                    leftSideFull = false;
                    break;
                }
            }

            // Check the right (red/NON RECYCLABLE) section.
            for (int col = columns / 2; col < columns; col++) {
                if (gridMatrix[col][row] == null) {
                    rightSideFull = false;
                    break;
                }
                if (rightSideType == null) {
                    rightSideType = gridMatrix[col][row];
                }
                if (gridMatrix[col][row] != rightSideType) {
                    rightSideFull = false;
                    break;
                }
            }

            // For recyclable blocks, mark cells as clearing and use the squish animation.
            if (leftSideFull && leftSideType == BlockShape.BlockType.RECYCLABLE) {
                for (int col = 0; col < columns / 2; col++) {
                    isClearing[col][row] = true;
                }
                // No fire animation for recyclable blocks.
                final int finalRow = row;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                    	score+=5;
                        animateSquishSection(finalRow, 0, columns / 2);
                    }
                }, 0.5f);
            }

            // For non recyclable blocks, mark cells as clearing, add fire animation, and use the sequential clearing animation.
            if (rightSideFull && rightSideType == BlockShape.BlockType.UNRECYCLABLE) {
                for (int col = columns / 2; col < columns; col++) {
                    isClearing[col][row] = true;
                }
                // Add fire animation only for non recyclable blocks.
                fireAnimations.add(new FireAnimation(row, columns / 2, columns));
                final int finalRow = row;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                    	score+=5;
                        animateClearSection(finalRow, columns / 2, columns);
                    }
                }, 0.5f);
            }
        }
    }

    /**
     * Animates the squish effect for a section of a row (for recyclable blocks).
     * After the squish animation completes, cells are shifted downward.
     */
    private void animateSquishSection(final int row, final int startCol, final int endCol) {
        for (int col = startCol; col < endCol; col++) {
            animateSquishCell(col, row);
        }
        // After the squish animation, shift down cells in the cleared section.
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for (int r = row; r < rows - 1; r++) {
                    for (int col = startCol; col < endCol; col++) {
                        gridMatrix[col][r] = gridMatrix[col][r + 1];
                        squishFactors[col][r] = squishFactors[col][r + 1];
                        isClearing[col][r] = false; // reset clearing flag for shifted cell
                    }
                }
                // Clear the top row in the section.
                for (int col = startCol; col < endCol; col++) {
                    gridMatrix[col][rows - 1] = null;
                    squishFactors[col][rows - 1] = 1.0f;
                    isClearing[col][rows - 1] = false;
                }
            }
        }, 0.7f); // Delay slightly longer than the squish animation duration.
    }

    /**
     * Animates a squish effect on a single cell (for recyclable blocks) by gradually reducing its height.
     * The cell is marked as clearing (drawn in 50% transparent grey) during the animation.
     * Once complete, the cell is cleared.
     */
    private void animateSquishCell(final int col, final int row) {
        isClearing[col][row] = true;
        final int steps = 5;
        final float stepDuration = 0.1f;
        for (int i = 1; i <= steps; i++) {
            final int currentStep = i;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    float newFactor = 1.0f - ((float) currentStep / steps);
                    squishFactors[col][row] = newFactor;
                }
            }, currentStep * stepDuration);
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gridMatrix[col][row] = null;
                squishFactors[col][row] = 1.0f;
                isClearing[col][row] = false;
            }
        }, (steps + 1) * stepDuration);
    }

    /**
     * Animates the clearing of a section of a row (for non recyclable blocks) by clearing each cell sequentially.
     * Each cell is marked as clearing (50% transparent grey) before being cleared; afterward, cells above are shifted downward.
     */
    private void animateClearSection(final int row, final int startCol, final int endCol) {
        float cellDelay = 0.1f;
        int numCells = endCol - startCol;
        for (int col = startCol; col < endCol; col++) {
            final int currentCol = col;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    isClearing[currentCol][row] = true;
                }
            }, (currentCol - startCol) * cellDelay);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    gridMatrix[currentCol][row] = null;
                    isClearing[currentCol][row] = false;
                }
            }, (currentCol - startCol) * cellDelay + 0.05f);
        }
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                for (int r = row; r < rows - 1; r++) {
                    for (int col = startCol; col < endCol; col++) {
                        gridMatrix[col][r] = gridMatrix[col][r + 1];
                        isClearing[col][r] = false;
                    }
                }
                for (int col = startCol; col < endCol; col++) {
                    gridMatrix[col][rows - 1] = null;
                    isClearing[col][rows - 1] = false;
                }
            }
        }, numCells * cellDelay);
    }

    /**
     * Draws the grid's vertical and horizontal lines with categorized colors.
     * The left half (greenish) and right half (reddish) are drawn as before.
     */
    public void drawCategorizeAreas(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x <= columns; x++) {
            if (x <= columns / 2) {
                shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
            } else {
                shapeRenderer.setColor(new Color(1, 0, 0, 0.5f));
            }
            shapeRenderer.line(x, 0, x, rows);
        }
        for (int y = 0; y <= rows; y++) {
            shapeRenderer.setColor(new Color(0, 1, 0, 0.5f));
            shapeRenderer.line(0, y, columns / 2, y);
            shapeRenderer.setColor(new Color(1, 0, 0, 0.5f));
            shapeRenderer.line(columns / 2, y, columns, y);
        }
        shapeRenderer.end();
    }
    
    /**
     * Updates all active fire animations and removes any that have finished.
     */
    private void updateFireAnimations(float delta) {
        for (int i = fireAnimations.size - 1; i >= 0; i--) {
            FireAnimation fire = fireAnimations.get(i);
            fire.update(delta);
            if (fire.isFinished()) {
                fireAnimations.removeIndex(i);
            }
        }
    }

    /**
     * Inner class representing a simple fire animation over a row section.
     * It draws flickering red/orange rectangles that fade over a set duration.
     */
    private class FireAnimation {
        int row;
        int startCol;
        int endCol;
        float timeElapsed;
        float duration;

        public FireAnimation(int row, int startCol, int endCol) {
            this.row = row;
            this.startCol = startCol;
            this.endCol = endCol;
            this.timeElapsed = 0;
            this.duration = 0.7f; // Duration of the fire animation (in seconds)
        }

        public void update(float delta) {
            timeElapsed += delta;
        }

        public boolean isFinished() {
            return timeElapsed >= duration;
        }

        public void draw(ShapeRenderer shapeRenderer) {
            // Fade out effect: alpha decreases as time passes.
            float alpha = 1.0f - (timeElapsed / duration);
            for (int col = startCol; col < endCol; col++) {
                // Create a flickering effect by randomly choosing between two fire colors.
                Color baseColor = MathUtils.randomBoolean(0.5f) ? Color.ORANGE : Color.RED;
                Color fireColor = new Color(baseColor.r, baseColor.g, baseColor.b, alpha);
                shapeRenderer.setColor(fireColor);
                shapeRenderer.rect(col, row, 1, 1);
            }
        }
    }
}

