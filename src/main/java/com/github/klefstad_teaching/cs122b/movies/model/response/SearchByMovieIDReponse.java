package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.*;

import java.util.List;

public class SearchByMovieIDReponse extends ResponseModel<SearchByMovieIDReponse> {
    movieDetailed movie;
    List<genre> genres;
    List<personSimple> persons;

    public movieDetailed getMovie() {
        return movie;
    }

    public SearchByMovieIDReponse setMovie(movieDetailed movie) {
        this.movie = movie;
        return this;
    }

    public List<genre> getGenres() {
        return genres;
    }

    public SearchByMovieIDReponse setGenres(List<genre> genres) {
        this.genres = genres;
        return this;
    }

    public List<personSimple> getPersons() {
        return persons;
    }

    public SearchByMovieIDReponse setPersons(List<personSimple> persons) {
        this.persons = persons;
        return this;
    }
}
