package br.com.arthursena.filmesfamosos.model;

public class ReviewDbResponse {

    private int page;
    private int total_results;
    private int total_pages;
    private ReviewDb[] results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public ReviewDb[] getResults() {
        return results;
    }

    public void setResults(ReviewDb[] results) {
        this.results = results;
    }
}
