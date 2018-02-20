package com.example.danieletavernelli.angelica.entity;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

/**
 * Created by Daniele Tavernelli on 2/5/2018.
 * user class
 */

public class ViewUtente implements Serializable {


    private Long id_utente, idRuolo;
    private String username,password,nome_ruolo,descFunc;

    public ViewUtente() {
        super();
    }

    public ViewUtente(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public ViewUtente(Long idRuolo, String username, String password,
                      String nome_ruolo) {
        super();
        this.idRuolo = idRuolo;
        this.username = username;
        this.password = password;
        this.nome_ruolo = nome_ruolo;
    }



    public ViewUtente(Long id_utente, Long idRuolo, String username,
                      String password, String nome_ruolo) {
        super();
        this.id_utente = id_utente;
        this.idRuolo = idRuolo;
        this.username = username;
        this.password = password;
        this.nome_ruolo = nome_ruolo;
    }

    public Long getId_utente() {
        return id_utente;
    }

    public void setId_utente(Long id_utente) {
        this.id_utente = id_utente;
    }

    public Long getIdRuolo() {
        return idRuolo;
    }

    public void setIdRuolo(Long idRuolo) {
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

    public String getNome_ruolo() {
        return nome_ruolo;
    }

    public void setNome_ruolo(String nome_ruolo) {
        this.nome_ruolo = nome_ruolo;
    }

    public String getDescFunc() {
        return descFunc;
    }

    public void setDescFunc(String descFunc) {
        this.descFunc = descFunc;
    }

    @Override
    public String toString() {
        return "id: "+getId_utente()+"; username: "+getUsername();
    }


}