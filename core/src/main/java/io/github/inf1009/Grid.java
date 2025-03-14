package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Grid {
    private final int columns;
    private final int rows;
    private Block.BlockType[][] gridMatrix;
    // Squish factors are used for recyclable blocks.
    private float[][] squishFactors;
    // Flag to indicate if a cell is in the process of being cleared.
    private boolean[][] isClearing;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        gridMatrix = new Block.BlockType[columns][rows];
        squishFactors = new float[columns][rows];
        isClearing = new boolean[columns][rows];
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

    public Block.BlockType[][] getGridMatrix() {
        return gridMatrix;
    }

    public void addBlock(int x, int y, Block.BlockType type) {
        gridMatrix[x][y] = type;
    }

    public boolean isOccupied(int x, int y) {
        return gridMatrix[x][y] != null || isClearing[x][y];
    }

    /**
     * Draws the grid. Cells marked as clearing are drawn in 50% transparent grey.
     * For recyclable blocks, the drawn height is based on the squish factor.
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
                        if (gridMatrix[col][row] == Block.BlockType.RECYCLABLE) {
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
    }

    /**
     * Checks each row. For the left half (assumed RECYCLABLE) it uses the squish animation;
     * for the right half (assumed NON RECYCLABLE) it uses the cooler (sequential clearing) animation.
     * In both cases, cells are marked as clearing so that they appear grey and 50% transparent.
     */
    public void clearRow() {
        for (int row = 0; row < rows; row++) {
            boolean leftSideFull = true;
            boolean rightSideFull = true;
            Block.BlockType leftSideType = null;
            Block.BlockType rightSideType = null;

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
            if (leftSideFull && leftSideType == Block.BlockType.RECYCLABLE) {
                for (int col = 0; col < columns / 2; col++) {
                    isClearing[col][row] = true;
                }
                final int finalRow = row;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        animateSquishSection(finalRow, 0, columns / 2);
                    }
                }, 0.5f);
            }

            // For non recyclable blocks, mark cells as clearing and use the cooler animation.
            if (rightSideFull && rightSideType == Block.BlockType.UNRECYCLABLE) {
                for (int col = columns / 2; col < columns; col++) {
                    isClearing[col][row] = true;
                }
                final int finalRow = row;
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
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
}
