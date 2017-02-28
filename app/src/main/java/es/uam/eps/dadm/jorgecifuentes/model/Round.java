package es.uam.eps.dadm.jorgecifuentes.model;

/**
 * Created by jorgecf on 26/02/17.
 */

import java.util.Date;
import java.util.UUID;

import es.uam.eps.dadm.jorgecifuentes.model.TableroReversi;
import es.uam.eps.multij.Tablero;

public class Round {

    // private int size;
    private String id;
    private String title;
    private String date;
    private TableroReversi board;


    public Round() {
        // this.size = size;
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase(); //TODO blancas(4) vs negras(17) o algo asi
        date = new Date().toString();
        board = new TableroReversi(); //TODO random board
    }

    /*
        public int getSize() {
            return size;
        }
    */
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