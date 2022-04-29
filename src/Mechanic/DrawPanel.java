package Mechanic;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static Mechanic.MainVariables.*;

public class DrawPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.white);
        labelScale.setText("Scale: "+ cameraScalePixelsPerCell +" pix/1 sq");

        if (currentTheme == THEME_DARK) {
            g.setColor(new Color(0));
            g.fillRect(0, 0, mainFrame.getWidth(), getHeight());
        }
        switch (currentTheme) {
            case THEME_LIGHT:
                g.setColor(new Color(0));
                break;
            case THEME_DARK:
                g.setColor(new Color(0xFFFFFF));
                break;
            case THEME_COLOURFUL:
                g.setColor(colorAliveCell);
                break;
        }
        for (Map.Entry<Integer, Cell> aliveCallsSet : listOfAliveCells.entrySet()) {
            Cell cell = aliveCallsSet.getValue();
            if (cell.type == Cell.TYPE_CONSTANT) {
                g.setColor(new Color(0x330000));
                g.fillRect(mainFrame.getWidth()/2+cameraCenterCellX*cameraScalePixelsPerCell+cameraCenterCellDoubleX+ cell.x*cameraScalePixelsPerCell, mainFrame.getHeight()/2+cameraCenterCellY*cameraScalePixelsPerCell+cameraCenterCellDoubleY+ cell.y*cameraScalePixelsPerCell, cameraScalePixelsPerCell, cameraScalePixelsPerCell);
                g.setColor(new Color(0));
            } else {
                g.fillRect(mainFrame.getWidth() / 2 + cameraCenterCellX * cameraScalePixelsPerCell + cameraCenterCellDoubleX + cell.x * cameraScalePixelsPerCell, mainFrame.getHeight() / 2 + cameraCenterCellY * cameraScalePixelsPerCell + cameraCenterCellDoubleY + cell.y * cameraScalePixelsPerCell, cameraScalePixelsPerCell, cameraScalePixelsPerCell);
            }
        }
        if (currentCellBrush == CELL_BRUSH_COMMON) {
            g.setColor(new Color(0x20000000, true));
        } else if (currentCellBrush == CELL_BRUSH_CONSTANT) {
            g.setColor(new Color(0x41000000, true));
        }
        if (currentBrush == BRUSH_BRUSH)
            drawSelectedSquare(g, 0, 0);
        else if (currentBrush == BRUSH_GLIDER) {
            drawSelectedSquare(g, -1, 0);
            drawSelectedSquare(g, 0, 1);
            drawSelectedSquare(g, 1, 1);
            drawSelectedSquare(g, 1, 0);
            drawSelectedSquare(g, 1, -1);
        }

        if (currentTheme == THEME_LIGHT)
            g.setColor(new Color(0x70000000, true));
        else if (currentTheme == THEME_DARK)
            g.setColor(new Color(0xFFFFFF));
        if (cameraScalePixelsPerCell > 3) {
            for (int y = mainFrame.getHeight()/2 + cameraCenterCellDoubleY; y <= mainFrame.getHeight(); y += cameraScalePixelsPerCell) {
                g.drawLine(0, y, mainFrame.getWidth(), y);
            }
            for (int y = mainFrame.getHeight()/2 - cameraScalePixelsPerCell + cameraCenterCellDoubleY; y >= 0; y -= cameraScalePixelsPerCell) {
                g.drawLine(0, y, mainFrame.getWidth(), y);
            }
            for (int x = mainFrame.getWidth()/2 + cameraCenterCellDoubleX; x <= mainFrame.getWidth(); x += cameraScalePixelsPerCell) {
                g.drawLine(x, 0, x, mainFrame.getHeight());
            }
            for (int x = mainFrame.getWidth()/2- cameraScalePixelsPerCell + cameraCenterCellDoubleX; x >= 0; x -= cameraScalePixelsPerCell) {
                g.drawLine(x, 0, x, mainFrame.getHeight());
            }

            g.setColor(new Color(165, 106, 0));
            g.drawLine(0, mainFrame.getHeight()/2 + cameraCenterCellDoubleY, mainFrame.getWidth(), mainFrame.getHeight()/2 + cameraCenterCellDoubleY);
            g.drawLine(0, mainFrame.getHeight()/2 + cameraCenterCellDoubleY+1, mainFrame.getWidth(), mainFrame.getHeight()/2 + cameraCenterCellDoubleY+1);
            g.drawLine(mainFrame.getWidth()/2 + cameraCenterCellDoubleX, 0, mainFrame.getWidth()/2 + cameraCenterCellDoubleX, mainFrame.getHeight()*2);
            g.drawLine(mainFrame.getWidth()/2 + cameraCenterCellDoubleX+1, 0, mainFrame.getWidth()/2 + cameraCenterCellDoubleX+1, mainFrame.getHeight()*2);
        }
    }

    public static void drawSelectedSquare(Graphics g, int xDifference, int yDifference) {
        g.fillRect(mainFrame.getWidth()/2+cameraCenterCellX*cameraScalePixelsPerCell+cameraCenterCellDoubleX+
                (selectedCell[0]+xDifference)*cameraScalePixelsPerCell, mainFrame.getHeight()/2+cameraCenterCellY*cameraScalePixelsPerCell+
                cameraCenterCellDoubleY+ (selectedCell[1]+yDifference)*cameraScalePixelsPerCell, cameraScalePixelsPerCell, cameraScalePixelsPerCell);
    }
}
