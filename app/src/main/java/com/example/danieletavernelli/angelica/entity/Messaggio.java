package com.example.danieletavernelli.angelica.entity;

import java.util.Date;

/**
 * Created by Daniele Tavernelli on 2/19/2018.
 *
 */

public class Messaggio  {

    private Long id_messaggio;
    private Long id_mittente,id_destinatario;
    private Date data;
    private String body;
    private int letto;



    public Messaggio() {
    }

    public Long getId_messaggio() {
        return id_messaggio;
    }

    public void setId_messaggio(Long id_messaggio) {
        this.id_messaggio = id_messaggio;
    }

    public Long getId_mittente() {
        return id_mittente;
    }

    public void setId_mittente(Long id_mittente) {
        this.id_mittente = id_mittente;
    }

    public Long getId_destinatario() {
        return id_destinatario;
    }

    public void setId_destinatario(Long id_destinatario) {
        this.id_destinatario = id_destinatario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getLetto() {
        return letto;
    }

    public void setLetto(int letto) {
        this.letto = letto;
    }


}
