package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;
import agent.*;
import genetic.*;
import neuralnet.*;

public class Main extends JFrame implements AgentSelectionDelegate {

    private Board gameBoard;
    private PlayerAgent[] players;

    private static Cell[][] boardCells = new Cell[Board.ROWS][Board.COLUMNS];
    private static JButton[] dropButtons = new JButton[Board.COLUMNS];
    private static Color whosTurn = Color.RED; // player 1 is red, player 2 is yellow
    private static JLabel whosTurnLabel = new JLabel("", SwingConstants.CENTER);

    private int animationDidFinish;
    
    private static JFrame endFrame = new JFrame();
    private static JLabel winnerLabel = new JLabel();

    public Main() {
        super();

        // set up main game frame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Connect Four AI");
        setSize(507, 448);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // make a frame for the end of game
        endFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        endFrame.setTitle("Game Over");
        endFrame.setSize(270, 100);
        endFrame.setLocationRelativeTo(null);
        endFrame.setLayout(new BorderLayout());
        endFrame.setResizable(false);

        // add ok button to end of game frame
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener () {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        endFrame.add(buttonPanel, BorderLayout.SOUTH);

        // add winner label to end of game frame
        winnerLabel.setFont(new Font("Tomaha", Font.ITALIC, 20));
        JPanel labelPanel = new JPanel();
        labelPanel.add(winnerLabel);
        endFrame.add(labelPanel, BorderLayout.CENTER);

        // first, open agent selection frame (welcome frame)
        AgentSelection agentSelector = new AgentSelection(this);
        agentSelector.setVisible(true);
    }

    public static void main(String ... args) {
        new Main();
    }

    public void agentsSelected(PlayerAgent player1, PlayerAgent player2) {
        players = new PlayerAgent[2];
        players[0] = player1;
        players[1] = player2;

        gameBoard = new Board();
        renderBoard();

        setVisible(true);

        if (!players[0].displayName().equals("Human")) {
            // if player 1 is not a human, disable buttons and let it think
            disableButtons();
            int delay = 500;
            Timer timer = new Timer(delay, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int firstMove = players[0].getNextMove(gameBoard, 1);
                    dropIntoColumn(firstMove); // play AI's move
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }

    private void renderBoard() {
        // set up whos turn label
        whosTurnLabel.setFont(new Font("Tomaha", Font.ITALIC, 16));
        whosTurnLabel.setPreferredSize(new Dimension(507, 21));
        whosTurnLabel.setText("Player 1 (" + players[0].displayName() + ") is thinking...");
        add(whosTurnLabel, BorderLayout.NORTH);
        
        // set up board panel
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Board.ROWS, Board.COLUMNS, 0, 0));
        for (int i = 0; i < boardCells.length; i++) {
            for (int j = 0; j < boardCells[0].length; j++) {
                boardCells[i][j] = new Cell();
                boardPanel.add(boardCells[i][j]);
            }
        }
        boardPanel.setBorder(BorderFactory.createEtchedBorder(Color.BLUE, Color.BLACK));
        add(boardPanel, BorderLayout.CENTER);

        // set up drop buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, Board.COLUMNS, 5, 5));
        for (int i = 0; i < dropButtons.length; i++) {
            dropButtons[i] = new JButton("Drop");
            dropButtons[i].setFont(new Font("Tomaha", Font.PLAIN, 10));
            dropButtons[i].setOpaque(true);
            dropButtons[i].setBorderPainted(false);
            dropButtons[i].setActionCommand("" + i);
            dropButtons[i].setBackground(Color.BLACK);
            dropButtons[i].setForeground(Color.RED);
            dropButtons[i].addActionListener(new ActionListener () {
                public void actionPerformed(ActionEvent e) {
                    int column = Integer.parseInt(e.getActionCommand());
                    dropIntoColumn(column);
                }
            });
            buttonsPanel.add(dropButtons[i]);
        }
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private final void dropIntoColumn(final int column)  {
        final int playerNum = whosTurn.equals(Color.RED) ? 1 : 2;
        final int rowToDrop = gameBoard.whichRow(column);
        gameBoard.makeMove(playerNum, column);

        animationDidFinish = 0;
        disableButtons();

        whosTurnLabel.setText("Player " + playerNum + 
                              " (" + players[playerNum-1].displayName() + ")" + 
                              " is playing column " + (column + 1));

        // animate dropping of disk
        final DropAnimation task = new DropAnimation(rowToDrop, column);
        task.execute();
        task.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent event) {
                if (task.isDone() && ++animationDidFinish <= 1) {
                    boardCells[rowToDrop][column].setColor(whosTurn);

                    if (gameBoard.isGameOver() == playerNum) {
                        winnerLabel.setText("Player " + playerNum + 
                                            " (" + players[playerNum-1].displayName() + ")" + 
                                            " wins!");
                        endFrame.setVisible(true);
                    } else if (gameBoard.isFull()) {
                        winnerLabel.setText("It's a tie!");
                        endFrame.setVisible(true);
                    } else {
                        whosTurn = whosTurn.equals(Color.RED) ? Color.YELLOW: Color.RED;
                        colorButtons(whosTurn);
                        enableButtons();

                        int nextPlayer = whosTurn.equals(Color.RED) ? 1 : 2;
                        if (!players[nextPlayer-1].displayName().equals("Human")) {
                            // if next player is not a human, disable buttons and let it think
                            disableButtons();
                            int delay = 500;
                            Timer timer = new Timer(delay, new ActionListener() {
                                public void actionPerformed(ActionEvent e) {
                                    int nextMove = players[nextPlayer-1].getNextMove(gameBoard, nextPlayer);
                                    dropIntoColumn(nextMove); // play AI's move
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();
                        }

                        whosTurnLabel.setText("Player " + nextPlayer + 
                                              " (" + players[nextPlayer-1].displayName() + ")" + 
                                              " is thinking...");
                    }
                }
            }
        });
    }

    private void disableButtons() {
        for (int column = 0; column < Board.COLUMNS; column++)
            dropButtons[column].setEnabled(false);
    }

    private void enableButtons()  {
        for (int column = 0; column < Board.COLUMNS; column++) {
            if (!gameBoard.columnFull(column))
                dropButtons[column].setEnabled(true);
        }
    }

    private void colorButtons(Color whatColor) {
        for (int i = 0; i < dropButtons.length; i++)  {
            dropButtons[i].setForeground(whatColor);
        }
    }

    private static class Cell extends JPanel {
        Color playerColor = new Color(240, 240, 240); // default is gray (not played)
        public void setColor(Color c) {
            this.playerColor = c;
            repaint();
        }
        public void removeColor() {
            this.playerColor = new Color(240, 240, 240);
            repaint();
        }
        public void paint(Graphics graphics) {
            super.paint(graphics);
            super.setBackground(Color.BLUE);
            Graphics2D g = (Graphics2D)graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke());
            g.drawOval(5, 5, 50, 50);
            g.setColor(this.playerColor);
            g.fillOval(6, 6, 49, 49);
        }
    }

    private static class DropAnimation extends SwingWorker<Integer, Integer> {
        int rowToDrop;
        int column;
        public DropAnimation(int rowToDrop, int column)  {
            this.rowToDrop = rowToDrop;
            this.column = column;
        }
        protected Integer doInBackground() throws Exception  {
            for (int i = 0; i < rowToDrop; i++) {
                boardCells[i][column].setColor(whosTurn);
                Thread.sleep(75);
                boardCells[i][column].removeColor();
            }
            this.done();
            return 0;
        }
    }
}