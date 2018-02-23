package com.example.danieletavernelli.angelica.entity;

import java.io.Serializable;

/**
 * Created by Daniele Tavernelli on 2/5/2018.
 * user class
 */

public class ViewUtente implements Serializable {


    private Integer idUtente, idRuolo;
    private String username,password, nomeRuolo,descFunc;

    public ViewUtente() {
        super();
    }

    public ViewUtente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ViewUtente(Integer idRuolo, String username, String password,
                      String nomeRuolo) {
        super();
        this.idRuolo = idRuolo;
        this.username = username;
        this.password = password;
        this.nomeRuolo = nomeRuolo;
    }



    public ViewUtente(Integer idUtente, Integer idRuolo, String username,
                      String password, String nomeRuolo) {
        super();
        this.idUtente = idUtente;
        this.idRuolo = idRuolo;
        this.username = username;
        this.password = password;
        this.nomeRuolo = nomeRuolo;
    }

    public Integer getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(Integer idUtente) {
        this.idUtente = idUtente;
    }

    public Integer getIdRuolo() {
        return idRuolo;
    }

    public void setIdRuolo(Integer idRuolo) {
        this.idRuolo = idRuolo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNomeRuolo() {
        return nomeRuolo;
    }

    public void setNomeRuolo(String nomeRuolo) {
        this.nomeRuolo = nomeRuolo;
    }

    public String getDescFunc() {
        return descFunc;
    }

    public void setDescFunc(String descFunc) {
        this.descFunc = descFunc;
    }

    @Override
    public String toString() {
        return "id: "+ getIdUtente()+"; username: "+getUsername();
    }


}