package com.example.zishan.gallarytask.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;


public class BaseResponse {

    private Photos photos;
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public String getStat() {
        return stat;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Photos {
        private int page;
        private String pages;
        private int perpage;

        private ArrayList<Photo> photo;

        public int getPage() {
            return page;
        }

        public int getPerpage() {
            return perpage;
        }

        public ArrayList<Photo> getPhoto() {
            return photo;
        }

        public String getPages() {
            return pages;
        }
    }
}
