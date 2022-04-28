package com.github.klefstad_teaching.cs122b.movies.repo.entity;

public class person {
    private String birthday;
    private String birthplace;
    private Float popularity;
    private String name;
    private Long id;
    private String biography;
    private String profilePath;

    public Long getId() {
        return id;
    }

    public person setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public person setName(String name) {
        this.name = name;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public person setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getBiography() {
        return biography;
    }

    public person setBiography(String biography) {
        this.biography = biography.replace("\r", "");
        return this;
    }

    public String getBirthplace() {
        return birthplace;
    }

    public person setBirthplace(String birthplace) {
        this.birthplace = birthplace;
        return this;
    }

    public Float getPopularity() {
        return popularity;
    }

    public person setPopularity(Float popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public person setProfilePath(String profilePath) {
        this.profilePath = profilePath;
        return this;
    }
}
