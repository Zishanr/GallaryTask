package com.example.zishan.gallarytask.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


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

        private int perpage;

        private List<Photo> photo;

        public int getPage() {
            return page;
        }

        public int getPerpage() {
            return perpage;
        }

        public List<Photo> getPhoto() {
            return photo;
        }
    }
}
