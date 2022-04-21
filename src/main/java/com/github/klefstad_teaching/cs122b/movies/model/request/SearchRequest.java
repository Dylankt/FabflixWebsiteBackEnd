package com.github.klefstad_teaching.cs122b.movies.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SearchRequest {
    private String authorization;
    private String transactionID;
    private String title;
    private Integer year;
    private String director;
    private String genre;
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public String getAuthorization() {
        return authorization;
    }

    public SearchRequest setAuthorization(String authorization) {
        this.authorization = authorization;
        return this;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public SearchRequest setTransactionID(String transactionID) {
        this.transactionID = transactionID;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public SearchRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public SearchRequest setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public SearchRequest setDirector(String director) {
        this.director = director;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public SearchRequest setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public Integer getLimit() {
        return limit;
    }

    public SearchRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public SearchRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public SearchRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public SearchRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
