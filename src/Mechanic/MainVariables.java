package Mechanic;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;

import static Libraries.Methods.*;
import static Mechanic.Mechanic.start;

public class MainVariables {
    //game laws
    public static int neighbourNumberForNewCell = 3;
    public static int lowestPossibleNeighbourNumberForAliveCell = 2;
    public static int highestPossibleNeighbourNumberForAliveCell = 3;

    public static int speedMilliseconds = 200;

    public static int iterationForNextStep = 1;
    public static int numOfNextSteps = 0;

    public static int cameraCenterCellX = 0;
    public static int cameraCenterCellY = 0;
    public static int cameraCenterCellDoubleX = 0;
    public static int cameraCenterCellDoubleY = 0;
    public static int cameraScalePixelsPerCell = 100;

    public static boolean w = false;
    public static boolean a = false;
    public static boolean s = false;
    public static boolean d = false;

    public static boolean button1 = false;
    public static boolean button3 = false;

    public static int mouseX;
    public static int mouseY;

    public static boolean interfaceVisible = true;
    public static boolean started = false;
    public static boolean iterationGoing = false;

    public static LinkedHashMap<String, String> listOfAliveCells = new LinkedHashMap<>();

    public static JPanel southPanel = new JPanel();
    public static DrawPanel drawPanel = new DrawPanel();
    public static JPanel speedPanel = new JPanel();

    public static JFrame mainFrame = getFrame("Game of Life", null, 700, 700, new BorderLayout(), null, true);

    public static JButton buttonStart = getButton("Run", f10, null, new Start(), null, null);
    public static JButton buttonNextStep = getButton("Next step", f10, null, new NextStep(), null, null);
    public static JButton buttonClear = getButton("Clear", f10, null, new Clear(), null, null);
    public static JButton buttonSpeedPlus = getButton("+", f10, null, new SpeedPlus(), null, null);
    public static JButton buttonSpeedMinus = getButton("-", f10, null, new SpeedMinus(), null, null);

    public static JLabel labelSpeed = getLabel("Speed: " + 1000/speedMilliseconds + " steps/s", f10, null, null);
    public static JLabel labelScale = getLabel("Scale: "+ cameraScalePixelsPerCell +" pix/sq", f10, null, null);

    public static Color colorButtonsPanel = new Color(0x52C17D00, true);
    public static Color colorRun = new Color(0x7236EE00, true);
    public static Color colorStop = new Color(0x72EE0000, true);
    public static Color colorNextStep = new Color(0x81FFF300, true);
    public static Color colorClear = new Color(0x72FFFFFF, true);
    public static Color colorSpeedPanel = new Color(0x0C17D00, true);
    public static Color colorSpeedPlus = new Color(0x7200E6EE, true);
    public static Color colorSpeedMinus = new Color(0x720028EE, true);


    public static class FrameKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'w':
                case 'W':
                case 'ц':
                case 'Ц':
                    if (!w)
                        w = true;
                    break;
                case 'a':
                case 'A':
                case 'ф':
                case 'Ф':
                    if (!a)
                        a = true;
                    break;
                case 'd':
                case 'D':
                case 'в':
                case 'В':
                    if (!d)
                        d = true;
                    break;
                case 's':
                case 'S':
                case 'ы':
                case 'Ы':
                    if (!s)
                        s = true;
                    break;
                case '-':
                    interfaceVisible = !interfaceVisible;
                    Mechanic.mainMenu();
                    break;
                default:
                    System.out.println(e.getKeyChar());
                    break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            switch (e.getKeyChar()) {
                case 'w':
                case 'W':
                case 'ц':
                case 'Ц':
                    w = false;
                    break;
                case 'a':
                case 'A':
                case 'ф':
                case 'Ф':
                    a = false;
                    break;
                case 'd':
                case 'D':
                case 'в':
                case 'В':
                    d = false;
                    break;
                case 's':
                case 'S':
                case 'ы':
                case 'Ы':
                    s = false;
                    break;
            }
        }
    }

    public static class FrameMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    button1 = true;
                    break;
                case MouseEvent.BUTTON3:
                    button3 = true;
                    break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            switch (e.getButton()) {
                case MouseEvent.BUTTON1:
                    button1 = false;
                    break;
                case MouseEvent.BUTTON3:
                    button3 = false;
                    break;
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static class FrameMouseMotionListener implements MouseMotionListener {
        @Override
        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

    public static class FrameMouseWheelListener implements MouseWheelListener {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            switch (e.getWheelRotation()) {
                case 1:
                    if (cameraScalePixelsPerCell > 1) {
                        cameraScalePixelsPerCell -= 1;
                        if (cameraCenterCellDoubleY >= cameraScalePixelsPerCell) {
                            cameraCenterCellDoubleY = 0;
                            cameraCenterCellY += 1;
                        }
                        if (cameraCenterCellDoubleY <= -1) {
                            cameraCenterCellDoubleY = cameraScalePixelsPerCell-1;
                            cameraCenterCellY -= 1;
                        }
                        if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                            cameraCenterCellDoubleX = 0;
                            cameraCenterCellX += 1;
                        }
                        if (cameraCenterCellDoubleX <= -1) {
                            cameraCenterCellDoubleX = cameraScalePixelsPerCell-1;
                            cameraCenterCellX -= 1;
                        }
                    }
                    break;
                case -1:
                    cameraScalePixelsPerCell += 1;
                    if (cameraCenterCellDoubleY >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleY = 0;
                        cameraCenterCellY += 1;
                    }
                    if (cameraCenterCellDoubleY <= -1) {
                        cameraCenterCellDoubleY = cameraScalePixelsPerCell-1;
                        cameraCenterCellY -= 1;
                    }
                    if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleX = 0;
                        cameraCenterCellX += 1;
                    }
                    if (cameraCenterCellDoubleX <= -1) {
                        cameraCenterCellDoubleX = cameraScalePixelsPerCell-1;
                        cameraCenterCellX -= 1;
                    }
                    break;
            }
        }
    }

    private static class Start implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            start();
        }
    }
    private static class NextStep implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            numOfNextSteps++;
        }
    }
    private static class Clear implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (started) {
                start();
            }
            listOfAliveCells = new LinkedHashMap<>();
        }
    }
    private static class SpeedPlus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (speedMilliseconds > 20) {
                speedMilliseconds -= 20;
                labelSpeed.setText("Speed: " + 1000/speedMilliseconds + " steps/s");
            }
        }
    }
    private static class SpeedMinus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (speedMilliseconds < 1000) {
                speedMilliseconds += 20;
                labelSpeed.setText("Speed: " + 1000/speedMilliseconds + " steps/s");
            }
        }
    }
}
