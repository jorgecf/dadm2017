package es.uam.eps.dadm.jorgecifuentes.model;

import java.util.Date;
import java.util.UUID;

/**
 * Clase que representa una ronda del juego, con un tablero y demas informacion adicional.
 *
 * @author Jorge Cifuentes
 */
public class Round {

    private String id;
    private String title;
    private String date;
    private TableroReversi board;
    private String playerUUID;
    private String roundUUID;

    private String playername;

    public Round() {
        this.id = UUID.randomUUID().toString();
        this.playerUUID = UUID.randomUUID().toString(); //TODO ???
        this.roundUUID = UUID.randomUUID().toString();

        this.title = "ROUND " + " " + id.toString().substring(19, 23).toUpperCase();
        this.date = new Date().toString();
        this.board = new TableroReversi();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public void setSecondPlayerName(String n) {
    }

    public String getPlayerUUID() {
        return this.playerUUID;
    }

    public String getRoundUUID() {
        return roundUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }
}