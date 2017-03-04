package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Created by jorgecf on 26/02/17.
 */

import java.util.Date;
import java.util.UUID;

import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.Tablero;

public class Round {

    private String id;
    private String title;
    private String date;
    private TableroReversi board;

    public Round() {
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new TableroReversi();
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