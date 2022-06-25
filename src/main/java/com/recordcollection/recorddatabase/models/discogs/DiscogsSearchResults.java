package com.recordcollection.recorddatabase.models.discogs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscogsSearchResults {

    private Pagination pagination;
    private List<Result> results;

    public DiscogsSearchResults(Pagination pagination, List<Result> results) {
        this.pagination = pagination;
        this.results = results;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Pagination {
        private Integer per_page;
        private Integer pages;
        private Integer page;
        private Urls urls;
        private Integer items;

        public Pagination(Integer per_page, Integer pages, Integer page, Urls urls, Integer items) {
            this.per_page = per_page;
            this.pages = pages;
            this.page = page;
            this.urls = urls;
            this.items = items;
        }

        public Integer getPer_page() {
            return per_page;
        }

        public void setPer_page(Integer per_page) {
            this.per_page = per_page;
        }

        public Integer getPages() {
            return pages;
        }

        public void setPages(Integer pages) {
            this.pages = pages;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public Urls getUrls() {
            return urls;
        }

        public void setUrls(Urls urls) {
            this.urls = urls;
        }

        public Integer getItems() {
            return items;
        }

        public void setItems(Integer items) {
            this.items = items;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Result {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Urls {
        private String last;
        private String next;

        public Urls(String last, String next) {
            this.last = last;
            this.next = next;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }

        public String getNext() {
            return next;
        }

        public void setNext(String next) {
            this.next = next;
        }
    }
}
