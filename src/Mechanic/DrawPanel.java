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

        g.setColor(new Color(0)); //0xFFCC00
        for (Map.Entry<String, String> cellsSet : listOfAliveCells.entrySet()) {
            g.fillRect(mainFrame.getWidth()/2+cameraCenterCellX*cameraScalePixelsPerCell+cameraCenterCellDoubleX+ Integer.parseInt(cellsSet.getKey().split("_")[0])*cameraScalePixelsPerCell, mainFrame.getHeight()/2+cameraCenterCellY*cameraScalePixelsPerCell+cameraCenterCellDoubleY+ Integer.parseInt(cellsSet.getKey().split("_")[1])*cameraScalePixelsPerCell, cameraScalePixelsPerCell, cameraScalePixelsPerCell);
        }

        g.setColor(new Color(0x70000000, true));
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
}
