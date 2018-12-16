package gui;

import agent.PlayerAgent;

public interface AgentSelectionDelegate {
    void agentsSelected(PlayerAgent player1, PlayerAgent player2);
}