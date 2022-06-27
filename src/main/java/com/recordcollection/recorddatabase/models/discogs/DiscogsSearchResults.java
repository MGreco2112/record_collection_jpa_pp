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
        private String title;
        private String year;
        private String resource_url;
        private Long id;
        private String cover_image;

        public Result(String title, String year, String resource_url, Long id) {
            this.title = title;
            this.year = year;
            this.resource_url = resource_url;
            this.id = id;
        }

        public Result(String title, String resource_url, Long id, String cover_image) {
            this.title = title;
            this.resource_url = resource_url;
            this.id = id;
            this.cover_image = cover_image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getResource_url() {
            return resource_url;
        }

        public void setResource_url(String resource_url) {
            this.resource_url = resource_url;
        }

        public String getCover_image() {
            return cover_image;
        }

        public void setCover_image(String cover_image) {
            this.cover_image = cover_image;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
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
