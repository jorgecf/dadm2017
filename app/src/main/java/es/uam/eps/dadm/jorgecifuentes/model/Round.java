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

    public Round() {
        this.id = UUID.randomUUID().toString();
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

}