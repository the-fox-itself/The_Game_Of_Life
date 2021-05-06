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
        mainFrame.addMouseListener(new FrameMouseListener());
        mainFrame.addMouseWheelListener(new FrameMouseWheelListener());

        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 10));
        southPanel.setBackground(new Color(0x52C17D00, true));
        southPanel.add(buttonStart);
        buttonStart.setBackground(new Color(0x7236EE00, true));
        southPanel.add(buttonNextStep);
        buttonNextStep.setBackground(new Color(0x81FFF300, true));
        southPanel.add(buttonClear);
        buttonClear.setBackground(new Color(0x72FFFFFF, true));

        southPanel.add(speedPanel);
        BoxLayout boxLayout = new BoxLayout(speedPanel, BoxLayout.Y_AXIS);
        speedPanel.setLayout(boxLayout);
        speedPanel.setBackground(new Color(0x0C17D00, true));

        speedPanel.add(labelSpeed);
        speedPanel.add(Box.createRigidArea(new Dimension(0,5)));
        speedPanel.add(buttonSpeedPlus);
        buttonSpeedPlus.setBackground(new Color(0x7200E6EE, true));
        speedPanel.add(Box.createRigidArea(new Dimension(0,5)));
        speedPanel.add(buttonSpeedMinus);
        buttonSpeedMinus.setBackground(new Color(0x720028EE, true));

        southPanel.add(labelScale);

        mainFrame.getContentPane().add(southPanel, BorderLayout.SOUTH);
        mainFrame.getContentPane().add(drawPanel, BorderLayout.CENTER);

        visTrue(mainFrame);

        new GameLoop().start();

        mainMenu();
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
    }

    public static void start() {
        if (started) {
            buttonStart.setText("Run");
            buttonStart.setBackground(new Color(0x7236EE00, true));
            started = false;
        } else {
            buttonStart.setText("Stop");
            buttonStart.setBackground(new Color(0x72EE0000, true));
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

                while (steps >= 1000d / 30) {
                    updateGameStats();
                    steps -= 1000d / 30;
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
//                iterationForMove++;
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
        }
        public void updateGameStats() {
            if (((started && speedMilliseconds < iterationForNextStep * (1000d / 30))) || numOfNextSteps > 0) {
                iterationGoing = true;
                LinkedHashMap<String, String> listOfAliveCellsCopy = listOfAliveCells;

                //removing alive cells with wrong number of neighbours.
                ArrayList<int[]> listOfDeadCells = new ArrayList<>();
                for (Map.Entry<String, String> cellsSet : listOfAliveCellsCopy.entrySet()) {
                    int cellX = Integer.parseInt(cellsSet.getKey().split("_")[0]);
                    int cellY = Integer.parseInt(cellsSet.getKey().split("_")[1]);
                    int neighbours = returnNumberOfNeighbours(new int[]{cellX, cellY}, listOfAliveCellsCopy);
                    if (!(neighbours >= lowestPossibleNeighbourNumberForAliveCell && neighbours <= highestPossibleNeighbourNumberForAliveCell)) {
                        listOfDeadCells.add(new int[]{cellX, cellY});
                    }
                }

                //adding new cells where it is right number of neighbours.
                ArrayList<int[]> listOfNewCells = new ArrayList<>();
                for (Map.Entry<String, String> cellsSet : listOfAliveCellsCopy.entrySet()) {
                    int cellX = Integer.parseInt(cellsSet.getKey().split("_")[0]);
                    int cellY = Integer.parseInt(cellsSet.getKey().split("_")[1]);
                    System.out.println("Searching around cell: " + Arrays.toString(new int[]{cellX, cellY}));

                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX-1, cellY-1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX-1, cellY-1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX+1, cellY-1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX+1, cellY-1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX-1, cellY+1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX-1, cellY+1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX+1, cellY+1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX+1, cellY+1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX, cellY-1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX, cellY-1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX, cellY+1})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX, cellY+1});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX-1, cellY})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX-1, cellY});
                    }
                    if (checkForNewCell(listOfAliveCellsCopy, listOfNewCells, new int[]{cellX+1, cellY})) {
                        System.out.println("Adding...");
                        listOfNewCells.add(new int[]{cellX+1, cellY});
                    }
                    System.out.println("--------------------------------");
                }


                if (!listOfDeadCells.isEmpty()) {
                    for (int[] listSet : listOfDeadCells)
                        listOfAliveCellsCopy.remove(listSet[0]+"_"+listSet[1]);
                }
                if (!listOfNewCells.isEmpty()) {
                    for (int[] listSet : listOfNewCells) {
                        listOfAliveCellsCopy.put(listSet[0] + "_" + listSet[1], "");
                        System.out.println(listSet[0] + "_" + listSet[1] + " was added to the list of alive cells");
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

        public int returnNumberOfNeighbours(int[] ints, LinkedHashMap<String, String> listOfAliveCellsCopy) {
            int neighbours = 0;

            if (listOfAliveCellsCopy.containsKey((ints[0]-1)+"_"+(ints[1]-1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0]+1)+"_"+(ints[1]-1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0]-1)+"_"+(ints[1]+1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0]+1)+"_"+(ints[1]+1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0])+"_"+(ints[1]-1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0])+"_"+(ints[1]+1)))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0]-1)+"_"+(ints[1])))
                neighbours++;
            if (listOfAliveCellsCopy.containsKey((ints[0]+1)+"_"+(ints[1])))
                neighbours++;

            return neighbours;
        }

        public boolean checkForNewCell(LinkedHashMap<String, String> listOfAliveCellsCopy, ArrayList<int[]> listOfNewCells, int[] searchCell) {
            System.out.println("Checking for new cell: " + Arrays.toString(searchCell));
            if (!listOfAliveCellsCopy.containsKey(searchCell[0]+"_"+searchCell[1]) && !listOfNewCells.contains(searchCell)) {
                return returnNumberOfNeighbours(searchCell, listOfAliveCellsCopy) == neighbourNumberForNewCell;
            }
            return false;
        }
    }
}
