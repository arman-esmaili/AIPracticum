package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.*;

import agent.*;

public class AgentSelection extends JFrame {

    public AgentSelection(AgentSelectionDelegate delegate) {
        super();
    
        // set up frame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Connect Four AI");
        setSize(507, 448);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        // add welcome label
        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setFont(new Font("Tomaha", Font.ITALIC, 23));
        welcomeLabel.setText("<html><body><p style='text-align: center'><br/>"+
                             "Welcome to Arman, Ethan,<br/>"+
                             "Meghna, Violet, and Jessica's<br/>"+
                             "CS 4701 Practicum!</p><br/><br/></body></html>");
        JPanel welcomePanel = new JPanel();
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);

        // add player agent radio buttons
        JPanel player1Panel = createPanelForPlayer(1);
        JPanel player2Panel = createPanelForPlayer(2);
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BorderLayout());
        playersPanel.add(player1Panel, BorderLayout.NORTH);
        playersPanel.add(player2Panel, BorderLayout.CENTER);
        add(playersPanel, BorderLayout.CENTER);

        // add continue button
        JButton continueButton = new JButton("Continue");
        continueButton.addActionListener(new ActionListener ()  {
            public void actionPerformed(ActionEvent e) {
                delegate.agentsSelected(agentForPlayer(1), agentForPlayer(2));
                setVisible(false);
            }
        });
        continueButton.setFont(new Font("Tomaha", Font.PLAIN, 20));
        continueButton.setPreferredSize(new Dimension(500, 60));
        JPanel continuePanel = new JPanel();
        continuePanel.add(continueButton);
        add(continuePanel, BorderLayout.SOUTH);
    }

    public static final String[] AGENTS = {"Human", "Minimax", "Neural Net"};
    public static JRadioButton[] player1Buttons = new JRadioButton[AGENTS.length];
    public static JRadioButton[] player2Buttons = new JRadioButton[AGENTS.length];

    private PlayerAgent agentForPlayer(int playerNum) {
        JRadioButton[] agentButtons = playerNum == 1 ? player1Buttons : player2Buttons;
        for (int i = 0; i < agentButtons.length; i++) {
            if (agentButtons[i].isSelected()) {
                switch(i) {
                    case 0: return new HumanAgent();
                    case 1: return new MinimaxAgent();
                    case 2: return new NeuralNetAgent();
                }
            }
        }
        return null;
    }

    public JPanel createPanelForPlayer(int playerNum)
    {
        JPanel playerPanel = new JPanel();
        playerPanel.setPreferredSize(new Dimension(500, 50));
        playerPanel.setLayout(new FlowLayout());

        JLabel playerLabel = new JLabel("Player " + playerNum + " Agent: ");
        playerLabel.setFont(new Font("Tomaha", Font.PLAIN, 20));
        playerPanel.add(playerLabel);

        JRadioButton[] agentButtons = playerNum == 1 ? player1Buttons : player2Buttons;

        ButtonGroup agentButtonGroup = new ButtonGroup();
        for (int i = 0; i < AGENTS.length; i++)
        {
            JRadioButton agentButton = new JRadioButton(AGENTS[i]);
            agentButtonGroup.add(agentButton);
            agentButtons[i] = agentButton;
            playerPanel.add(agentButton);
        }
    
        agentButtons[0].setSelected(true); // default selection is human
    
        return playerPanel;
    }
}