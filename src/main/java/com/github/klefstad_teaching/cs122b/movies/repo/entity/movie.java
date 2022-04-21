package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class movie {
    Boolean hidden;
    Integer year;
    String director;
    Double rating;
    Long id;
    String backdropPath;
    String title;
    String posterPath;

    public Long getId() {
        return id;
    }

    public movie setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public Integer getYear() {
        return year;
    }

    public movie setYear(Integer year) {
        this.year = year;
        return this;
    }

    public String getDirector() {
        return director;
    }

    public movie setDirector(String director) {
        this.director = director;
        return this;
    }

    public Double getRating() {
        return rating;
    }

    public movie setRating(Double rating) {
        this.rating = rating;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public movie setHidden(Boolean hidden) {
        this.hidden = hidden;
        return this;
    }
}
