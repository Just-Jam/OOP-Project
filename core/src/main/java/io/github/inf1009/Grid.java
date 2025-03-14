package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Grid {
    private final int columns;
    private final int rows;
    private int boxcol=0;
    private int db=0;
    private Block.BlockType[][] gridMatrix; //Store Blocktype instead of bool

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

        gridMatrix = new Block.BlockType[columns][rows]; // Initialize as null

    }
    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

//    public boolean[][] getGridMatrix() {
//        return gridMatrix;
//    }
    public Block.BlockType[][] getGridMatrix() {
        return gridMatrix;
    }
//    public void addBlock(int x, int y) {
//        gridMatrix[x][y] = true;
//        //color selector based on height
//        if(y>5) {
//        	db=3;
//        	boxcol=3;
//        }
//        else if(y>3 & boxcol!=3) {
//        	db=2;
//        	boxcol=2;
//        }
//        else if (y>1 & boxcol!=3 & boxcol!=2) {
//        	db=1;
//        	boxcol=1;
//        }
//    }
    public void addBlock(int x, int y, Block.BlockType type) {
        gridMatrix[x][y] = type; // Store block type
    }
    public boolean isOccupied(int x, int y) {
        return gridMatrix[x][y] != null; // Check if a block exists in that cell
    }
    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);

        // Draw categorized grid areas first
        drawCategorizeAreas(shapeRenderer);

        // Draw the blocks after the grid
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (gridMatrix[col][row] != null) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                    if (gridMatrix[col][row] == Block.BlockType.RECYCLABLE) {
                        shapeRenderer.setColor(Color.GREEN);
                    } else {
                        shapeRenderer.setColor(Color.RED);
                    }

                    shapeRenderer.rect(col, row, 1, 1);
                    shapeRenderer.end();
                }
            }
        }
    }

//    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
//        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
//
//
//        drawCategorizeAreas(shapeRenderer);
//        drawBlocks(shapeRenderer);
//
//    }

//    public boolean isOccupied(int x, int y) {
//        return gridMatrix[x][y]; // Check if a block exists in that cell
//    }


//    private void drawBlocks(ShapeRenderer shapeRenderer) {
//        for (int col = 0; col < columns; col++) {
//            for (int row = 0; row < rows; row++) {
//                if (gridMatrix[col][row]) {
//                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//                    //color selector
//                    switch (boxcol) {
//	                    case 1:
//	                    	shapeRenderer.setColor(Color.DARK_GRAY);
//	                    	break;
//	                    case 2:
//	                    	shapeRenderer.setColor(Color.FIREBRICK);
//	                    	break;
//	                    case 3:
//	                    	shapeRenderer.setColor(Color.RED);
//	                    	break;
//	                    default:
//	                    	shapeRenderer.setColor(Color.BLACK);
//	                    	break;
//                    }
//                    shapeRenderer.rect(col, row, 1, 1);
//                    shapeRenderer.end();
//                }
//            }
//        }
//    }

    public void clearRow() {
        for (int row = 0; row < rows; row++) {
            boolean leftSideFull = true;
            boolean rightSideFull = true;
            Block.BlockType leftSideType = null;
            Block.BlockType rightSideType = null;

            // Check the left (green) section
            for (int col = 0; col < columns / 2; col++) {
                if (gridMatrix[col][row] == null) {
                    leftSideFull = false;
                    break;
                }
                if (leftSideType == null) {
                    leftSideType = gridMatrix[col][row];
                }
                if (gridMatrix[col][row] != leftSideType) {
                    leftSideFull = false; // The row is mixed with different colors
                    break;
                }
            }

            // Check the right (red) section
            for (int col = columns / 2; col < columns; col++) {
                if (gridMatrix[col][row] == null) {
                    rightSideFull = false;
                    break;
                }
                if (rightSideType == null) {
                    rightSideType = gridMatrix[col][row];
                }
                if (gridMatrix[col][row] != rightSideType) {
                    rightSideFull = false; // The row is mixed with different colors
                    break;
                }
            }

            // Clear the left section only if it's completely filled with green blocks
            if (leftSideFull && leftSideType == Block.BlockType.RECYCLABLE) {
                clearSpecificSection(row, 0, columns / 2);
            }

            // Clear the right section only if it's completely filled with red blocks
            if (rightSideFull && rightSideType == Block.BlockType.UNRECYCLABLE) {
                clearSpecificSection(row, columns / 2, columns);
            }
        }
    }

    // Helper method to clear only a section of the row
    private void clearSpecificSection(int row, int startCol, int endCol) {
        // Clear only the relevant section
        for (int col = startCol; col < endCol; col++) {
            gridMatrix[col][row] = null;
        }

        // Shift down only within the cleared section
        for (int r = row; r < rows - 1; r++) {
            for (int col = startCol; col < endCol; col++) {
                gridMatrix[col][r] = gridMatrix[col][r + 1]; // Move row above down
            }
        }

        // Clear the top row after shifting
        for (int col = startCol; col < endCol; col++) {
            gridMatrix[col][rows - 1] = null;
        }
    }



    public void drawCategorizeAreas(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw vertical grid lines
        for (int x = 0; x <= columns; x++) {
            if (x <= columns / 2) {
                shapeRenderer.setColor(new Color(0, 1, 0, 0.5f)); // Semi-transparent Green
            } else {
                shapeRenderer.setColor(new Color(1, 0, 0, 0.5f)); // Semi-transparent Red
            }
            shapeRenderer.line(x, 0, x, rows); // Use line instead of rect for better rendering
        }

        // Draw horizontal grid lines
        for (int y = 0; y <= rows; y++) {
            shapeRenderer.setColor(new Color(0, 1, 0, 0.5f)); // Semi-transparent Green
            shapeRenderer.line(0, y, columns / 2, y);

            shapeRenderer.setColor(new Color(1, 0, 0, 0.5f)); // Semi-transparent Red
            shapeRenderer.line(columns / 2, y, columns, y);
        }

        shapeRenderer.end();
    }


}
