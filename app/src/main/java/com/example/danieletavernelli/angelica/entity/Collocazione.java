package com.example.danieletavernelli.angelica.entity;

/**
 *  Created by Daniele Tavernelli on 2/9/2018.
 */

public class Collocazione {

    private int idCollocazione;

    private String collocazione,piano,stanza,denominazione,note;



    public Collocazione() {
    }



    public Collocazione(String collocazione, String piano, String stanza,
                        String denominazione, String note) {
        super();
        this.collocazione = collocazione;
        this.piano = piano;
        this.stanza = stanza;
        this.denominazione = denominazione;
        this.note = note;
    }



    public Collocazione(int idCollocazione, String collocazione,
                        String piano, String stanza, String denominazione,
                        String note) {
        super();
        this.idCollocazione = idCollocazione;
        this.collocazione = collocazione;
        this.piano = piano;
        this.stanza = stanza;
        this.denominazione = denominazione;
        this.note = note;
    }



    public int getIdCollocazione() {
        return idCollocazione;
    }

    public void setIdCollocazione(int idCollocazione) {
        this.idCollocazione = idCollocazione;
    }

    public String getCollocazione() {
        return collocazione;
    }

    public void setCollocazione(String collocazione) {
        this.collocazione = collocazione;
    }

    public String getPiano() {
        return piano;
    }

    public void setPiano(String piano) {
        this.piano = piano;
    }

    public String getStanza() {
        return stanza;
    }

    public void setStanza(String stanza) {
        this.stanza = stanza;
    }

    public String getDenominazione() {
        return denominazione;
    }

    public void setDenominazione(String denominazione) {
        this.denominazione = denominazione;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Collocazione: "+collocazione+"    Piano: "+piano+"    Stanza: "+stanza+"    Denominazione: "+denominazione+"    Note: "+note;
    }

    public String toUri() {
        return "Collocazione: "+collocazione+"#Piano: "+piano+"#Stanza: "+stanza+"#Denominazione: "+denominazione+"#Note: "+note;
    }


}
