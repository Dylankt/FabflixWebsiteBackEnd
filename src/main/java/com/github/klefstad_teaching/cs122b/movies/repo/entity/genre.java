package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class genre {
    private String name;
    private Long id;

    public Long getId() {
        return id;
    }

    public genre setId(Long genreID) {
        this.id = genreID;
        return this;
    }

    public String getName() {
        return name;
    }

    public genre setName(String name) {
        this.name = name;
        return this;
    }
}
