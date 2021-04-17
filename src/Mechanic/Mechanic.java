package Mechanic;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static Libraries.Methods.*;
import static Mechanic.MainVariables.*;

public class Mechanic {
    void preparation() {
        int[][] ints = new int[1][2];

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

//        speedHelpPanel.setLayout(new BoxLayout(speedHelpPanel, BoxLayout.Y_AXIS));
//        speedHelpPanel.setBackground(new Color(0x52C17D00, true));
//        speedPanel.add(speedHelpPanel);

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
                iterationForMove++;
            }
        }
        public void handleInput() {
            if (iterationForMove >= 1) {
                if (w) {
                    cameraCenterCellDoubleY += 1;
                    if (cameraCenterCellDoubleY >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleY = 0;
                        cameraCenterCellY += 1;
                    }
                } else if (s) {
                    cameraCenterCellDoubleY -= 1;
                    if (cameraCenterCellDoubleY <= -1) {
                        cameraCenterCellDoubleY = cameraScalePixelsPerCell-1;
                        cameraCenterCellY -= 1;
                    }
                }
                if (a) {
                    cameraCenterCellDoubleX += 1;
                    if (cameraCenterCellDoubleX >= cameraScalePixelsPerCell) {
                        cameraCenterCellDoubleX = 0;
                        cameraCenterCellX += 1;
                    }
                } else if (d) {
                    cameraCenterCellDoubleX -= 1;
                    if (cameraCenterCellDoubleX <= -1) {
                        cameraCenterCellDoubleX = cameraScalePixelsPerCell-1;
                        cameraCenterCellX -= 1;
                    }
                }
                iterationForMove = 0;
            }
        }
        public void updateGameStats() {
            if ((started && speedMilliseconds < iterationForNextStep * (1000d / 30)) || numOfNextSteps > 0) {
                //removing alive cells with wrong number of neighbours.
                ArrayList<int[]> listOfDeathCells = new ArrayList<>();
                for (int[] ints : listOfAliveCells) {
                    int neighbours = returnNumberOfNeighbours(ints);
                    if (!(neighbours >= lowestPossibleNeighbourNumberForAliveCell && neighbours <= highestPossibleNeighbourNumberForAliveCell)) {
                        listOfDeathCells.add(ints);
                    }
                }

                //adding new cells where it is right number of neighbours.
                ArrayList<int[]> listOfNewCells = new ArrayList<>();
                for (int[] ints : listOfAliveCells) {
                    System.out.println("Searching around cell: " + Arrays.toString(ints));
                    int[] search = new int[]{ints[0]-1, ints[1]-1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0]+1, ints[1]-1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0]-1, ints[1]+1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0]+1, ints[1]+1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0], ints[1]-1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0], ints[1]+1};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0]-1, ints[1]};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    search = new int[]{ints[0]+1, ints[1]};
                    System.out.println("Checking death cell: " + Arrays.toString(search));
                    if (!contains(listOfAliveCells, search) && !contains(listOfNewCells, search)) {
                        System.out.println("Neighbours: " + returnNumberOfNeighbours(search));
                        if (returnNumberOfNeighbours(search) == neighbourNumberForNewCell) {
                            System.out.println("Adding...");
                            listOfNewCells.add(search);
                        }
                        System.out.println();
                    }
                    System.out.println("--------------------------------");
                }


                if (!listOfDeathCells.isEmpty()) {
                    listOfAliveCells.removeAll(listOfDeathCells);
                }
                if (!listOfNewCells.isEmpty()) {
                    listOfAliveCells.addAll(listOfNewCells);
                }

                if (numOfNextSteps > 0) {
                    numOfNextSteps--;
                } else {
                    iterationForNextStep = 1;
                }
                System.out.println("Alive cells:");
                for (int[] el : listOfAliveCells) {
                    System.out.println("x: " + el[0] + ", y: " + el[1]);
                }
                System.out.println("END OF ITERATION\n========================================");
            }
            if (!started) {
                iterationForNextStep = 1;
            }
        }

        public int returnNumberOfNeighbours(int[] ints) {
            int neighbours = 0;

            for (int[] el : listOfAliveCells) {
                if (ints[0]-1 == el[0] && ints[1]-1 == el[1])
                    neighbours++;
                if (ints[0]+1 == el[0] && ints[1]-1 == el[1])
                    neighbours++;
                if (ints[0]-1 == el[0] && ints[1]+1 == el[1])
                    neighbours++;
                if (ints[0]+1 == el[0] && ints[1]+1 == el[1])
                    neighbours++;
                if (ints[0] == el[0] && ints[1]-1 == el[1])
                    neighbours++;
                if (ints[0] == el[0] && ints[1]+1 == el[1])
                    neighbours++;
                if (ints[0]-1 == el[0] && ints[1] == el[1])
                    neighbours++;
                if (ints[0]+1 == el[0] && ints[1] == el[1])
                    neighbours++;
            }

            return neighbours;
        }
        public boolean contains(ArrayList<int[]> list, int[] ints) {
            for (int[] el : list) {
                if (ints[0] == el[0] && ints[1] == el[1]) {
                    return true;
                }
            }
            return false;
        }
    }
}
