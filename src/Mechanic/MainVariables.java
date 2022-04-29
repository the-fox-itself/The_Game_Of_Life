package Mechanic;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;

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

//    public static ArrayList<Cell> listOfAliveCells = new ArrayList<>();
    public static LinkedHashMap<Integer, Cell> listOfAliveCells = new LinkedHashMap<>();
    public static int[] selectedCell = new int[]{0, 0};

    public final static int BRUSH_BRUSH = 1;
    public final static int BRUSH_GLIDER = 2;
    public static int currentBrush = BRUSH_BRUSH;

    public final static int CELL_BRUSH_COMMON = 1;
    public final static int CELL_BRUSH_CONSTANT = 2;
    public static int currentCellBrush = CELL_BRUSH_COMMON;

    public final static int THEME_LIGHT = 1;
    public final static int THEME_DARK = 2;
    public final static int THEME_COLOURFUL = 3;
    public static int currentTheme = THEME_LIGHT;

    public static JPanel southPanel = new JPanel();
    public static DrawPanel drawPanel = new DrawPanel();
    public static JPanel speedPanel = new JPanel();
    public static JPanel scalePanel = new JPanel();

    final public static Image ICON_FRAME = new ImageIcon("resources"+SEPARATOR+"images"+SEPARATOR+"ICON_FRAME.png").getImage();
    public static JFrame mainFrame = getFrame("Game of Life", ICON_FRAME, 700, 700, new BorderLayout(), null, true);

    public static double millisecondsPerUpdate = 1000d / 30;

    public static JButton buttonStart = getButton("Run", f12, null, new Start(), Color.black, new Color(0xFF05FF00, true));
    public static JButton buttonNextStep = getButton("Next step", f12, null, new NextStep(), Color.black, new Color(0xFFFF6F00, true));
    public static JButton buttonClear = getButton("Clear", f12, null, new Clear(), Color.black, Color.white);
    public static JButton buttonSpeedPlus = getButton("+", f12, null, new SpeedPlus(), Color.black, new Color(0xFFFF0000, true));
    public static JButton buttonSpeedMinus = getButton("-", f12, null, new SpeedMinus(), Color.black, new Color(0xFF00F7FF, true));
    public static JButton buttonScalePlus = getButton("+", f12, null, new ScalePlus(), Color.black, new Color(0xFFFF0000, true));
    public static JButton buttonScaleMinus = getButton("-", f12, null, new ScaleMinus(), Color.black, new Color(0xFF00F7FF, true));

    public static JLabel labelSpeed = getLabel("Speed: " + String.format("%.1f",(double)1000/speedMilliseconds) + " steps/s", f12, null, null);
    public static JLabel labelScale = getLabel("Scale: "+ cameraScalePixelsPerCell +" pix/sq", f12, null, null);

    public static Color colorButtonsPanel = new Color(0x52C17D00, true);
    public static Color colorRun = new Color(0x7236EE00, true);
    public static Color colorStop = new Color(0x72EE0000, true);
    public static Color colorNextStep = new Color(0x81FFF300, true);
    public static Color colorClear = new Color(0x72FFFFFF, true);
    public static Color colorSpeedPanel = new Color(0x0C17D00, true);
    public static Color colorSpeedPlus = new Color(0x7200E6EE, true);
    public static Color colorSpeedMinus = new Color(0x720028EE, true);

    public static Color colorAliveCell = new Color(0xFFCC00);
    public static Color colorSelectedCell = new Color(0x20000000, true);
    public static Color colorCentralLine = new Color(165, 106, 0);

    public static void findingSelectedCell() {
        double x = ((mouseX-6)-cameraCenterCellX*cameraScalePixelsPerCell-cameraCenterCellDoubleX-(double)(mainFrame.getWidth())/2)/cameraScalePixelsPerCell;
        double y = ((mouseY-29)-cameraCenterCellY*cameraScalePixelsPerCell-cameraCenterCellDoubleY-(double)(mainFrame.getHeight())/2)/cameraScalePixelsPerCell;

        if (x < 0) {
            x--;
        }
        if (y < 0) {
            y--;
        }
        selectedCell = new int[]{(int) x, (int) y};
    }

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
                case 'r':
                case 'R':
                case 'к':
                case 'К':
                    currentBrush = BRUSH_BRUSH;
                    break;
                case 't':
                case 'T':
                case 'е':
                case 'Е':
                    currentBrush = BRUSH_GLIDER;
                    break;
                case 'f':
                case 'F':
                case 'а':
                case 'А':
                    currentCellBrush = CELL_BRUSH_COMMON;
                    break;
                case 'g':
                case 'G':
                case 'п':
                case 'П':
                    currentCellBrush = CELL_BRUSH_CONSTANT;
                    break;
                case 'u':
                case 'U':
                case 'г':
                case 'Г':
                    currentTheme = THEME_LIGHT;
                    break;
                case 'i':
                case 'I':
                case 'ш':
                case 'Ш':
                    currentTheme = THEME_DARK;
                    break;
                case 'o':
                case 'O':
                case 'щ':
                case 'Щ':
                    currentTheme = THEME_COLOURFUL;
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
            findingSelectedCell();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
            findingSelectedCell();
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
                            cameraCenterCellDoubleY -= cameraScalePixelsPerCell;
                            cameraCenterCellY += 1;
                        }
                        if (cameraCenterCellDoubleY <= -1) {
                            cameraCenterCellDoubleY += cameraScalePixelsPerCell;
                            cameraCenterCellY -= 1;
                        }
                        if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                            cameraCenterCellDoubleX -= cameraScalePixelsPerCell;
                            cameraCenterCellX += 1;
                        }
                        if (cameraCenterCellDoubleX <= -1) {
                            cameraCenterCellDoubleX += cameraScalePixelsPerCell;
                            cameraCenterCellX -= 1;
                        }
                    }
                    break;
                case -1:
                    cameraScalePixelsPerCell += 1;
                    if (cameraCenterCellDoubleY >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleY -= cameraScalePixelsPerCell;
                        cameraCenterCellY += 1;
                    }
                    if (cameraCenterCellDoubleY <= -1) {
                        cameraCenterCellDoubleY += cameraScalePixelsPerCell;
                        cameraCenterCellY -= 1;
                    }
                    if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleX -= cameraScalePixelsPerCell;
                        cameraCenterCellX += 1;
                    }
                    if (cameraCenterCellDoubleX <= -1) {
                        cameraCenterCellDoubleX += cameraScalePixelsPerCell;
                        cameraCenterCellX -= 1;
                    }
                    break;
            }
            findingSelectedCell();
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
            if (speedMilliseconds > 200) {
                speedMilliseconds -= 30;
            } else if (speedMilliseconds > 0) {
                speedMilliseconds -= 5;
            }

            if (speedMilliseconds > 0 && speedMilliseconds < 2000)
                if (speedMilliseconds < 500)
                    labelSpeed.setText("Speed: " + String.format("%.1f",(double)1000/speedMilliseconds) + " steps/s");
                else
                    labelSpeed.setText("Speed: " + String.format("%.2f",(double)1000/speedMilliseconds) + " steps/s");
            else if (speedMilliseconds == 0) {
                labelSpeed.setText("Speed: MAX steps/s");
            } else if (speedMilliseconds == 2000) {
                labelSpeed.setText("Speed: MIN steps/s");
            }
        }
    }
    private static class SpeedMinus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (speedMilliseconds >= 200 && speedMilliseconds < 2000) {
                speedMilliseconds += 30;
            } else if (speedMilliseconds < 2000) {
                speedMilliseconds += 5;
            }
            if (speedMilliseconds > 0 && speedMilliseconds < 2000)
                if (speedMilliseconds < 500)
                    labelSpeed.setText("Speed: " + String.format("%.1f",(double)1000/speedMilliseconds) + " steps/s");
                else
                    labelSpeed.setText("Speed: " + String.format("%.2f",(double)1000/speedMilliseconds) + " steps/s");
            else if (speedMilliseconds == 0) {
                labelSpeed.setText("Speed: MAX steps/s");
            } else if (speedMilliseconds == 2000) {
                labelSpeed.setText("Speed: MIN steps/s");
            }
        }
    }

    private static class ScalePlus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
        }
    }
    private static class ScaleMinus implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
        }
    }
}
