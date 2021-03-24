package com.example.mynewday;

import androidx.annotation.NonNull;

class BookType {
    private int imageid;
    private String name;

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookType(int imageid, String name) {
        this.imageid = imageid;
        this.name = name;
    }

}
