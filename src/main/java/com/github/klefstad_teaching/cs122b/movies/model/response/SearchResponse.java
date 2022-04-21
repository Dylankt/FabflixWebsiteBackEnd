package com.github.klefstad_teaching.cs122b.movies.model.response;

import com.github.klefstad_teaching.cs122b.core.base.ResponseModel;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.movie;

import java.util.List;

public class SearchResponse  extends ResponseModel<SearchResponse> {
    List<movie> movies;

    public List<movie> getMovies() {
        return movies;
    }

    public SearchResponse setMovies(List<movie> movie) {
        movies = movie;
        return this;
    }
}
