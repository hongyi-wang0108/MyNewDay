package com.example.mynewday;

class Day {
    String title;
    String date;
    String type;
    int imageid;
    int datediffer;

    public Day() {
    }

    public Day(String title, String date, String type, int imageid) {
        this.title = title;
        this.date = date;
        this.type = type;
        this.imageid = imageid;
    }

    public int getDatediffer() {
        return datediffer;
    }

    public void setDatediffer(int datediffer) {
        this.datediffer = datediffer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    @Override
    public String toString() {
        return "Day{" +
                "title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", imageid=" + imageid +
                ", datediffer=" + datediffer +
                '}';
    }
}
