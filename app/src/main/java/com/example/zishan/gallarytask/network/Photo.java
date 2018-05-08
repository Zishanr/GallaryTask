package com.example.zishan.gallarytask.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Photo extends RealmObject implements Serializable{
    @PrimaryKey
    private String id;
    private int farm;
    private String server;
    private String secret;
    private String searchString;


    public int getFarm() {
        return farm;
    }

    public String getServer() {
        return server;
    }

    public String getId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }


    public void setFarm(int farm) {
        this.farm = farm;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
