package io.github.inf1009;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.inf1009.manager.SoundManager;

public class Grid {
    private int playerScore;
    private final int columns;
    private final int rows;
    private final SoundManager soundManager;

    private BlockShape.BlockType[][] gridMatrix;
    private float[][] squishFactors;
    private boolean[][] isClearing;
    private final Array<FireAnimation> fireAnimations;

    public Grid(int columns, int rows, SoundManager soundManager) {
        this.columns = columns;
        this.rows = rows;
        this.soundManager = soundManager;
        this.gridMatrix = new BlockShape.BlockType[columns][rows];
        this.squishFactors = new float[columns][rows];
        this.isClearing = new boolean[columns][rows];
        this.fireAnimations = new Array<>();
        initialize();
    }

    private void initialize() {
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                squishFactors[col][row] = 1.0f;
                isClearing[col][row] = false;
            }
        }
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void addToScore(int value) {
        playerScore += value;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public boolean isOccupied(int x, int y) {
        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            return true; // Treat out-of-bounds as occupied
        }
        return gridMatrix[x][y] != null || isClearing[x][y];
    }

    public void addBlock(int x, int y, BlockShape.BlockType type) {
        if (x < 0 || x >= columns || y < 0 || y >= rows) {
            System.err.println("⚠️ Warning: Attempted to place block out of bounds: (" + x + ", " + y + ")");
            return;
        }
        gridMatrix[x][y] = type;
        soundManager.playPlaceBlockSound();
    }


    public BlockShape.BlockType[][] getGridMatrix() {
        return gridMatrix;
    }

    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
        drawCategorizeAreas(shapeRenderer);

        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (gridMatrix[col][row] != null || isClearing[col][row]) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    setCellColor(shapeRenderer, col, row);
                    float height = (col < columns / 2) ? squishFactors[col][row] : 1.0f;
                    shapeRenderer.rect(col, row, 1, height);
                    shapeRenderer.end();
                }
            }
        }

        float delta = Gdx.graphics.getDeltaTime();
        updateFireAnimations(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (FireAnimation fire : fireAnimations) {
            fire.draw(shapeRenderer);
        }
        shapeRenderer.end();
    }

    private void setCellColor(ShapeRenderer shapeRenderer, int col, int row) {
        if (isClearing[col][row]) {
            shapeRenderer.setColor(new Color(0.5f, 0.5f, 0.5f, 0.5f));
        } else {
            shapeRenderer.setColor(gridMatrix[col][row] == BlockShape.BlockType.RECYCLABLE ? Color.GREEN : Color.RED);
        }
    }

    public void drawCategorizeAreas(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (int x = 0; x <= columns; x++) {
            shapeRenderer.setColor(x <= columns / 2 ? new Color(0, 1, 0, 0.5f) : new Color(1, 0, 0, 0.5f));
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

    public void clearRow() {
        int clearedRows = 0;
        for (int row = 0; row < rows; row++) {
            if (checkAndAnimateSection(row, 0, columns / 2, BlockShape.BlockType.RECYCLABLE, true)) clearedRows++;
            if (checkAndAnimateSection(row, columns / 2, columns, BlockShape.BlockType.UNRECYCLABLE, false)) clearedRows++;
        }
        if (clearedRows >= 2) addToScore(100);
    }

    private boolean checkAndAnimateSection(int row, int startCol, int endCol, BlockShape.BlockType expectedType, boolean squish) {
        BlockShape.BlockType type = null;
        for (int col = startCol; col < endCol; col++) {
            if (gridMatrix[col][row] == null) return false;
            if (type == null) type = gridMatrix[col][row];
            if (gridMatrix[col][row] != type) return false;
        }
        if (type == expectedType) {
            for (int col = startCol; col < endCol; col++) {
                isClearing[col][row] = true;
            }
            if (squish) {
                addToScore(50);
                soundManager.playCrushSound();
                Timer.schedule(new Timer.Task() {
                    @Override public void run() {
                        animateSquishSection(row, startCol, endCol);
                    }
                }, 0.5f);
            } else {
                addToScore(25);
                soundManager.playBurningSound();
                fireAnimations.add(new FireAnimation(row, startCol, endCol));
                Timer.schedule(new Timer.Task() {
                    @Override public void run() {
                        animateClearSection(row, startCol, endCol);
                    }
                }, 0.5f);
            }
            return true;
        }
        return false;
    }

    private void animateSquishSection(final int row, final int startCol, final int endCol) {
        for (int col = startCol; col < endCol; col++) animateSquishCell(col, row);
        Timer.schedule(new Timer.Task() {
            @Override public void run() {
                for (int r = row; r < rows - 1; r++) {
                    for (int col = startCol; col < endCol; col++) {
                        gridMatrix[col][r] = gridMatrix[col][r + 1];
                        squishFactors[col][r] = squishFactors[col][r + 1];
                        isClearing[col][r] = false;
                    }
                }
                for (int col = startCol; col < endCol; col++) {
                    gridMatrix[col][rows - 1] = null;
                    squishFactors[col][rows - 1] = 1.0f;
                    isClearing[col][rows - 1] = false;
                }
                applyGravityToSection(startCol, endCol);
            }
        }, 0.7f);
    }

    private void animateSquishCell(final int col, final int row) {
        final int steps = 5;
        final float stepDuration = 0.1f;
        for (int i = 1; i <= steps; i++) {
            final int currentStep = i;
            Timer.schedule(new Timer.Task() {
                @Override public void run() {
                    squishFactors[col][row] = 1.0f - ((float) currentStep / steps);
                }
            }, currentStep * stepDuration);
        }
        Timer.schedule(new Timer.Task() {
            @Override public void run() {
                gridMatrix[col][row] = null;
                squishFactors[col][row] = 1.0f;
                isClearing[col][row] = false;
            }
        }, (steps + 1) * stepDuration);
    }

    private void animateClearSection(final int row, final int startCol, final int endCol) {
        float cellDelay = 0.1f;
        int numCells = endCol - startCol;
        for (int col = startCol; col < endCol; col++) {
            final int currentCol = col;
            Timer.schedule(new Timer.Task() {
                @Override public void run() {
                    isClearing[currentCol][row] = true;
                }
            }, (currentCol - startCol) * cellDelay);
            Timer.schedule(new Timer.Task() {
                @Override public void run() {
                    gridMatrix[currentCol][row] = null;
                    isClearing[currentCol][row] = false;
                }
            }, (currentCol - startCol) * cellDelay + 0.05f);
        }
        Timer.schedule(new Timer.Task() {
            @Override public void run() {
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
                applyGravityToSection(startCol, endCol);
            }
        }, numCells * cellDelay);
    }

    private void applyGravityToSection(int startCol, int endCol) {
        for (int col = startCol; col < endCol; col++) {
            int writeRow = 0;
            for (int readRow = 0; readRow < rows; readRow++) {
                if (gridMatrix[col][readRow] != null) {
                    if (writeRow != readRow) {
                        gridMatrix[col][writeRow] = gridMatrix[col][readRow];
                        gridMatrix[col][readRow] = null;
                    }
                    writeRow++;
                }
            }
        }
    }

    private void updateFireAnimations(float delta) {
        for (int i = fireAnimations.size - 1; i >= 0; i--) {
            FireAnimation fire = fireAnimations.get(i);
            fire.update(delta);
            if (fire.isFinished()) {
                fireAnimations.removeIndex(i);
            }
        }
    }
}
