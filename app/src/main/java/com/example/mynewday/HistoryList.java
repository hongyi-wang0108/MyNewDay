package com.example.mynewday;
class HistoryList {
    String year,title;

    public HistoryList(String year, String title) {
        this.year = year;
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Event{" +
                "year='" + year + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
