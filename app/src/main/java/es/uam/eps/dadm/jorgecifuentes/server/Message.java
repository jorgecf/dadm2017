package es.uam.eps.dadm.jorgecifuentes.server;

/**
 * Created by jorgecf on 13/05/17.
 */

public class Message {

    private int id;
    private String msg;
    private String date;
    private String sender;

    public Message(int id, String msg, String date, String sender) {
        this.id = id;
        this.msg = msg;
        this.date = date;
        this.sender = sender;
    }
}
