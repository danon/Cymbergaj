package danon.Network;

public class StartMessage implements Message {
    private final char playerTeam;

    public StartMessage(char playerTeam) {
        this.playerTeam = playerTeam;
    }

    public char getPlayerTeam() {
        return playerTeam;
    }
}
