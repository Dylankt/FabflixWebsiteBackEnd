package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class personSimple {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public personSimple setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public personSimple setName(String name) {
        this.name = name;
        return this;
    }
}
