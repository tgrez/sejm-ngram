package org.sejmngram.database.fetcher.json.datamodel;

/**
 * Created by michalsiemionczyk on 26/07/14.
 */
public class Meta {

    int total_occurences_number;
    int current_page;
    int total_pages;
    int current_limit_per_page;

    public int getTotal_occurences_number() {
        return total_occurences_number;
    }

    public void setTotal_occurences_number(int total_occurences_number) {
        this.total_occurences_number = total_occurences_number;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getCurrent_limit_per_page() {
        return current_limit_per_page;
    }

    public void setCurrent_limit_per_page(int current_limit_per_page) {
        this.current_limit_per_page = current_limit_per_page;
    }



    public Meta(int total_occurences_number, int current_page, int total_pages, int current_limit_per_page) {
        this.total_occurences_number = total_occurences_number;
        this.current_page = current_page;
        this.total_pages = total_pages;
        this.current_limit_per_page = current_limit_per_page;
    }
}
