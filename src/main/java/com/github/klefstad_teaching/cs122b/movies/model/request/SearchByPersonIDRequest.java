package com.github.klefstad_teaching.cs122b.movies.model.request;

public class SearchByPersonIDRequest {
    private Integer limit;
    private Integer page;
    private String orderBy;
    private String direction;

    public Integer getLimit() {
        return limit;
    }

    public SearchByPersonIDRequest setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public SearchByPersonIDRequest setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public SearchByPersonIDRequest setOrderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public SearchByPersonIDRequest setDirection(String direction) {
        this.direction = direction;
        return this;
    }
}
