package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class movieDetailed {
    Boolean hidden;
    Integer year;
    String director;
    Double rating;
    Long id;
    String backdropPath;
    String title;
    String posterPath;
    Long numVotes;
    Long budget;
    Long revenue;
    String overview;

    public Long getId() {
        return id;
    }

    public movieDetailed setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public movieDetailed setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public movieDetailed setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public movieDetailed setDirector(String director) {
        this.director = director;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public movieDetailed setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public movieDetailed setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public movieDetailed setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public movieDetailed setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public Long getNumVotes() {
        return numVotes;
    }

    public movieDetailed setNumVotes(Long numVotes) {
        this.numVotes = numVotes;
        return this;
    }

    public Long getBudget() {
        return budget;
    }

    public movieDetailed setBudget(Long budget) {
        this.budget = budget;
        return this;
    }

    public Long getRevenue() {
        return revenue;
    }

    public movieDetailed setRevenue(Long revenue) {
        this.revenue = revenue;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public movieDetailed setOverview(String overview) {
        this.overview = overview;
        return this;
    }
}
