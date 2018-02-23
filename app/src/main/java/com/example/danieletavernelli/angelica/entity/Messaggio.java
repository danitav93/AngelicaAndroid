package com.example.danieletavernelli.angelica.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Daniele Tavernelli on 2/19/2018.
 *
 */

public class Messaggio implements Serializable {

    private Integer idMessaggio;
    private Integer idMittente, idDestinatario;
    private Date data;
    private String body;
    private int letto;



    public Messaggio() {
    }

    public Integer getIdMessaggio() {
        return idMessaggio;
    }

    public void setIdMessaggio(Integer idMessaggio) {
        this.idMessaggio = idMessaggio;
    }

    public Integer getIdMittente() {
        return idMittente;
    }

    public void setIdMittente(Integer idMittente) {
        this.idMittente = idMittente;
    }

    public Integer getIdDestinatario() {
        return idDestinatario;
    }

    public void setIdDestinatario(Integer idDestinatario) {
        this.idDestinatario = idDestinatario;
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
