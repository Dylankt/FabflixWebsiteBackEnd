package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class director {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public director setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public director setName(String name) {
        this.name = name;
        return this;
    }
}
