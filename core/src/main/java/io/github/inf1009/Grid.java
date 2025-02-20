package io.github.inf1009;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Grid {
    private final int columns;
    private final int rows;
    private int boxcol=0;

    private boolean[][] gridMatrix;

    public Grid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;

        gridMatrix = new boolean[columns][rows];

    }

    public boolean[][] getGridMatrix() {
        return gridMatrix;
    }

    public void addBlock(int x, int y) {
        gridMatrix[x][y] = true;
        //color selector based on height
        if(y>5) {
        	boxcol=3;
        }
        else if(y>3 & boxcol!=3) {
        	boxcol=2;
        }
        else if (y>1 & boxcol!=3 & boxcol!=2) {
        	boxcol=1;
        }
    }

    public void draw(ShapeRenderer shapeRenderer, FitViewport fitViewport) {
        shapeRenderer.setProjectionMatrix(fitViewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        
        for (int x = 0; x <= columns; x++) {
            shapeRenderer.rect(x - 0.02f, 0, 0.04f, rows);
        }
        for (int y = 0; y <= rows; y++) {
            shapeRenderer.rect(0, y - 0.02f, columns, 0.04f);
        }
        shapeRenderer.end();

        drawBlocks(shapeRenderer);
    }
    
    public boolean isOccupied(int x, int y) {
        return gridMatrix[x][y]; // Check if a block exists in that cell
    }


    private void drawBlocks(ShapeRenderer shapeRenderer) {
        for (int col = 0; col < columns; col++) {
            for (int row = 0; row < rows; row++) {
                if (gridMatrix[col][row]) {
                    shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                    //color selector
                    switch (boxcol) {                    
	                    case 1:
	                    {
	                    	shapeRenderer.setColor(Color.DARK_GRAY);
	                    	break;
	                    }
	                    case 2:
	                    {
	                    	shapeRenderer.setColor(Color.FIREBRICK);
	                    	break;
	                    }
	                    case 3:
	                    {
	                    	shapeRenderer.setColor(Color.RED);
	                    	break;
	                    }
	                    default:
	                    {
	                    	shapeRenderer.setColor(Color.BLACK);
	                    	break;
	                    }
                    }
                    
                    shapeRenderer.rect(col, row, 1, 1);
                    shapeRenderer.end();
                }
            }
        }
    }

    public void clearRow() {
        for (int row = 0; row < rows; row++) {
            boolean rowComplete = true;
            for (int col = 0; col < columns; col++) {
                if (!gridMatrix[col][row]) {
                    rowComplete = false;
                    break;
                }
            }
            if (rowComplete) {
                for (int col = 0; col < columns; col++) {
                    gridMatrix[col][row] = false;
                }
            }
        }
    }
}
