package Mechanic;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static Libraries.Methods.*;
import static Mechanic.MainVariables.*;

public class Mechanic {
    void preparation() {
        visTrue(mainFrame);

        drawPanel.addKeyListener(new FrameKeyListener());
        buttonStart.addKeyListener(new FrameKeyListener());
        buttonNextStep.addKeyListener(new FrameKeyListener());
        buttonClear.addKeyListener(new FrameKeyListener());
        buttonSpeedPlus.addKeyListener(new FrameKeyListener());
        buttonSpeedMinus.addKeyListener(new FrameKeyListener());
        buttonScalePlus.addKeyListener(new FrameKeyListener());
        buttonScaleMinus.addKeyListener(new FrameKeyListener());
        mainFrame.addMouseListener(new FrameMouseListener());
        mainFrame.addMouseMotionListener(new FrameMouseMotionListener());
        mainFrame.addMouseWheelListener(new FrameMouseWheelListener());

        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        southPanel.setBackground(new Color(0x2F5A5A5A, true));
        southPanel.add(buttonStart);
        southPanel.add(buttonNextStep);
        southPanel.add(buttonClear);

        southPanel.add(speedPanel);
        BoxLayout boxLayout = new BoxLayout(speedPanel, BoxLayout.Y_AXIS);
        speedPanel.setLayout(boxLayout);
        speedPanel.setBackground(new Color(0x0000000, true));

        speedPanel.add(labelSpeed);
        speedPanel.add(Box.createRigidArea(new Dimension(0,10)));
        speedPanel.add(buttonSpeedPlus);
        speedPanel.add(Box.createRigidArea(new Dimension(0,5)));
        speedPanel.add(buttonSpeedMinus);

        southPanel.add(scalePanel);
        BoxLayout boxLayout1 = new BoxLayout(scalePanel, BoxLayout.Y_AXIS);
        scalePanel.setLayout(boxLayout1);
        scalePanel.setBackground(new Color(0x0000000, true));

        scalePanel.add(labelScale);
        scalePanel.add(Box.createRigidArea(new Dimension(0,10)));
        scalePanel.add(buttonScalePlus);
        scalePanel.add(Box.createRigidArea(new Dimension(0,5)));
        scalePanel.add(buttonScaleMinus);

        mainFrame.getContentPane().add(southPanel, BorderLayout.SOUTH);
//        mainFrame.getContentPane().add(northPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(drawPanel, BorderLayout.CENTER);

        visTrue(mainFrame);

        new GameLoop().start();

        mainMenu();

        /* Ideas:
        * Themes - light, dark and colourful
        * Drawing instruments:
        *   Dot
        *   Brush
        *   Line (straight with Shift)
        *   Rectangle (square with Shift)
        *   Ruler (measures and prolongs the distance in squares)
        *   Copy and paste
        *   Reset to the position before the start
        * Templates (can be placed like one dot):
        *   Glider
        *   Lightweight spaceship
        * Types of cells:
        *   Cells which cannot be destroyed (immune cells) */
    }

    static void mainMenu() {
        drawPanel.requestFocus();
        visFalseAll();
        if (interfaceVisible) {
            visTrue(southPanel);
            visTrue(buttonStart);
            visTrue(buttonNextStep);
            visTrue(buttonClear);
            visTrue(labelSpeed);
            visTrue(buttonSpeedPlus);
            visTrue(buttonSpeedMinus);
            visTrue(labelScale);
            visTrue(buttonScalePlus);
            visTrue(buttonScaleMinus);
        }
    }

    private static void visFalseAll() {
        visFalse(southPanel);
        visFalse(buttonStart);
        visFalse(buttonNextStep);
        visFalse(buttonClear);
        visFalse(labelSpeed);
        visFalse(buttonSpeedPlus);
        visFalse(buttonSpeedMinus);
        visFalse(labelScale);
        visFalse(buttonScalePlus);
        visFalse(buttonScaleMinus);
    }

    public static void start() {
        if (started) {
            buttonStart.setText("Run");
            buttonStart.setForeground(new Color(0xFF05FF00, true));
            started = false;
        } else {
            buttonStart.setText("Stop");
            buttonStart.setForeground(new Color(0xFFFF2525, true));
            started = true;
        }
    }

    public static class GameLoop extends Thread {
        public void run() {
            double previous = new Date().getTime();
            double steps = 0;
            while (true) {
                double loopStartTime = new Date().getTime();
                double elapsed = loopStartTime - previous;
                previous = new Date().getTime();
                steps += elapsed;

                handleInput();

                while (steps >= millisecondsPerUpdate) {
                    updateGameStats();
                    steps -= millisecondsPerUpdate;
                }

                mainFrame.repaint();

                double loopSlot = 10;
                double endTime = loopStartTime + loopSlot;
                while (new Date().getTime() < endTime) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ignored) { }
                }
                iterationForNextStep++;
            }
        }
        public void handleInput() {
            if (!s && w) {
                cameraCenterCellDoubleY += 1;
                if (cameraCenterCellDoubleY >= cameraScalePixelsPerCell) {
                    cameraCenterCellY += cameraCenterCellDoubleY/cameraScalePixelsPerCell;
                    cameraCenterCellDoubleY = 0;
                }
            } else if (!w && s) {
                cameraCenterCellDoubleY -= 1;
                if (cameraCenterCellDoubleY <= -1) {
                    cameraCenterCellY -= (-cameraCenterCellDoubleY)/cameraScalePixelsPerCell+1;
                    cameraCenterCellDoubleY = cameraScalePixelsPerCell-1;
                }
            }
            if (!d && a) {
                cameraCenterCellDoubleX += 1;
                if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                    cameraCenterCellX += cameraCenterCellDoubleX/cameraScalePixelsPerCell;
                    cameraCenterCellDoubleX = 0;
                }
            } else if (!a && d) {
                cameraCenterCellDoubleX -= 1;
                if (cameraCenterCellDoubleX <= -1) {
                    cameraCenterCellX -= (-cameraCenterCellDoubleX)/cameraScalePixelsPerCell+1;
                    cameraCenterCellDoubleX = cameraScalePixelsPerCell-1;
                }
            }
            if (button1) {
                double x = ((mouseX-6)-cameraCenterCellX*cameraScalePixelsPerCell-cameraCenterCellDoubleX-(double)(mainFrame.getWidth())/2)/cameraScalePixelsPerCell;
                double y = ((mouseY-29)-cameraCenterCellY*cameraScalePixelsPerCell-cameraCenterCellDoubleY-(double)(mainFrame.getHeight())/2)/cameraScalePixelsPerCell;

                if (x < 0)
                    x--;
                if (y < 0)
                    y--;

                int finalX = (int) x;
                int finalY = (int) y;

                if (currentBrush == BRUSH_BRUSH) {
                    if (!listOfAliveCells.containsKey((finalX+"_"+finalY).hashCode())) {
                        listOfAliveCells.put((finalX+"_"+finalY).hashCode(), new Cell(finalX, finalY, currentCellBrush));
                        System.out.println(finalX + " " + finalY + " " + (finalX+"_"+finalY).hashCode() + "  " + listOfAliveCells.keySet());
                    }
                } else if (currentBrush == BRUSH_GLIDER) {
                    if (!listOfAliveCells.containsKey(((finalX-1)+"_"+finalY).hashCode()) &&
                            !listOfAliveCells.containsKey((finalX+"_"+(finalY+1)).hashCode()) &&
                            !listOfAliveCells.containsKey(((finalX+1)+"_"+(finalY+1)).hashCode()) &&
                            !listOfAliveCells.containsKey(((finalX+1)+"_"+finalY).hashCode()) &&
                            !listOfAliveCells.containsKey(((finalX+1)+"_"+(finalY-1)).hashCode())) {
                        listOfAliveCells.put(((finalX-1)+"_"+finalY).hashCode(), new Cell(finalX-1, finalY, currentCellBrush));
                        listOfAliveCells.put((finalX+"_"+(finalY+1)).hashCode(), new Cell(finalX, finalY+1, currentCellBrush));
                        listOfAliveCells.put(((finalX+1)+"_"+(finalY+1)).hashCode(), new Cell(finalX+1, finalY+1, currentCellBrush));
                        listOfAliveCells.put(((finalX+1)+"_"+finalY).hashCode(), new Cell(finalX+1, finalY, currentCellBrush));
                        listOfAliveCells.put(((finalX+1)+"_"+(finalY-1)).hashCode(), new Cell(finalX+1, finalY-1, currentCellBrush));
                    }
                }
            } else if (button3) {
                double x = ((mouseX-6)-cameraCenterCellX*cameraScalePixelsPerCell-cameraCenterCellDoubleX-(double)(mainFrame.getWidth())/2)/cameraScalePixelsPerCell;
                double y = ((mouseY-29)-cameraCenterCellY*cameraScalePixelsPerCell-cameraCenterCellDoubleY-(double)(mainFrame.getHeight())/2)/cameraScalePixelsPerCell;

                if (x < 0)
                    x--;
                if (y < 0)
                    y--;

                int finalX = (int) x;
                int finalY = (int) y;

                if (listOfAliveCells.containsKey((finalX+"_"+finalY).hashCode())) {
                    listOfAliveCells.remove((finalX + "_" + finalY).hashCode());
                    System.out.println((finalX+"_"+finalY).hashCode() + " removed");
                }
            }
        }
        public void updateGameStats() {
            if (((started && speedMilliseconds < iterationForNextStep * (millisecondsPerUpdate))) || numOfNextSteps > 0) {
                iterationGoing = true;
                LinkedHashMap<Integer, Cell> listOfAliveCellsCopy = listOfAliveCells;

                //removing alive cells with wrong number of neighbours.
                ArrayList<Cell> listOfDeadCells = new ArrayList<>();
                for (Map.Entry<Integer, Cell> aliveCellsSet : listOfAliveCellsCopy.entrySet()) {
                    Cell cell = aliveCellsSet.getValue();
                    if (cell.type == Cell.TYPE_COMMON) {
                        int neighbours = returnNumberOfNeighbours(cell.x, cell.y, listOfAliveCellsCopy);
                        if (!(neighbours >= lowestPossibleNeighbourNumberForAliveCell && neighbours <= highestPossibleNeighbourNumberForAliveCell)) {
                            listOfDeadCells.add(cell);
                        }
                    }
                }

                //adding new cells where it is right number of neighbours.
                LinkedHashMap<Integer, Cell> listOfNewCells = new LinkedHashMap<>();
                for (Map.Entry<Integer, Cell> aliveCallsSet : listOfAliveCellsCopy.entrySet()) {
                    Cell cell = aliveCallsSet.getValue();
                    System.out.println("Searching around cell: " + Arrays.toString(new int[]{cell.x, cell.y}));

                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x-1, cell.y-1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x-1)+"_"+(cell.y-1)).hashCode(), new Cell(cell.x-1, cell.y-1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x+1, cell.y-1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x+1)+"_"+(cell.y-1)).hashCode(), new Cell(cell.x+1, cell.y-1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x-1, cell.y+1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x-1)+"_"+(cell.y+1)).hashCode(), new Cell(cell.x-1, cell.y+1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x+1, cell.y+1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x+1)+"_"+(cell.y+1)).hashCode(), new Cell(cell.x+1, cell.y+1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x, cell.y-1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put((cell.x+"_"+(cell.y-1)).hashCode(), new Cell(cell.x, cell.y-1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x, cell.y+1)) {
                        System.out.println("Adding...");
                        listOfNewCells.put((cell.x+"_"+(cell.y+1)).hashCode(), new Cell(cell.x, cell.y+1, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x-1, cell.y)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x-1)+"_"+cell.y).hashCode(), new Cell(cell.x-1, cell.y, Cell.TYPE_COMMON));
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, cell.x+1, cell.y)) {
                        System.out.println("Adding...");
                        listOfNewCells.put(((cell.x+1)+"_"+cell.y).hashCode(), new Cell(cell.x+1, cell.y, Cell.TYPE_COMMON));
                    }
                    System.out.println("--------------------------------");
                }


                if (!listOfDeadCells.isEmpty()) {
                    for (Cell cell : listOfDeadCells)
                        listOfAliveCellsCopy.remove((cell.x+"_"+cell.y).hashCode());
                }
                if (!listOfNewCells.isEmpty()) {
                    for (Map.Entry<Integer, Cell> newCellsSet : listOfNewCells.entrySet()) {
                        Cell cell = newCellsSet.getValue();
                        listOfAliveCellsCopy.put((cell.x+"_"+cell.y).hashCode(), cell);
                        System.out.println(cell.x + "_" + cell.y + " was added to the list of alive cells");
                    }
                }

                if (numOfNextSteps > 0) {
                    numOfNextSteps--;
                } else {
                    iterationForNextStep = 1;
                }
                System.out.println("END OF ITERATION\n========================================");

                iterationGoing = false;
            }
            if (!started) {
                iterationForNextStep = 1;
            }
        }

        public int returnNumberOfNeighbours(int x, int y, LinkedHashMap<Integer, Cell> listOfAliveCellsCopy) {
            int neighbours = 0;

            if (listOfAliveCellsCopy.containsKey(((x-1)+"_"+(y-1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey(((x+1)+"_"+(y-1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey(((x-1)+"_"+(y+1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey(((x+1)+"_"+(y+1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((x+"_"+(y-1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((x+"_"+(y+1)).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey(((x-1)+"_"+y).hashCode()))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey(((x+1)+"_"+y).hashCode()))
                neighbours++;

            System.out.println("Searching neighbours ended. " + x + " " + y + " has " + neighbours + " neighbours");
            return neighbours;
        }

        public boolean checkForNewCell(LinkedHashMap<Integer, Cell> listOfAliveCellsCopy, LinkedHashMap<Integer, Cell> listOfNewCells, int x, int y) {
            if (!listOfAliveCellsCopy.containsKey((x+"_"+y).hashCode()) && !listOfNewCells.containsKey((x+"_"+y).hashCode())) {
                return returnNumberOfNeighbours(x, y, listOfAliveCellsCopy) == neighbourNumberForNewCell;
            }
            return false;
        }
    }
}
