package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.Date;
import java.util.UUID;

/**
 * Clase que representa una ronda del juego, con un tablero e informacion adicional.
 *
 * @author Jorge Cifuentes
 */
public class Round {

    private String title;
    private String date;
    private TableroReversi board;
    private String playerUUID;
    private String roundUUID;

    private String playername;


    public Round(String playerUUID, String playername) {
        this.playerUUID = playerUUID;
        this.roundUUID = UUID.randomUUID().toString();

        this.playername = playername;
        this.title = this.roundUUID.toString().substring(19, 23).toUpperCase();
        this.date = new Date().toString();

        this.board = new TableroReversi();
    }

    public Round(String firstPlayerName, String roundTitle, String roundDate, String playerUUID, String rounduuid) {

        this.setFirstPlayerName(firstPlayerName);
        this.setTitle(roundTitle);
        this.setDate(roundDate);

        this.setPlayerUUID(playerUUID);
        this.setRoundUUID(rounduuid);

        this.board = new TableroReversi();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TableroReversi getBoard() {
        return board;
    }

    public void setBoard(TableroReversi board) {
        this.board = board;
    }

    public int getSize() {
        return this.board.getSize();
    }

    public void setFirstPlayerName(String n) {
        this.playername = n;
    }

    public String getPlayername() {
        return playername;
    }


    //   public void setSecondPlayerName(String n) {
    //  }

    public String getPlayerUUID() {
        return this.playerUUID;
    }


    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setRoundUUID(String roundUUID) {
        this.roundUUID = roundUUID;
    }

    public String getRoundUUID() {
        return roundUUID;
    }
}