package com.github.klefstad_teaching.cs122b.movies.model.request;

public class SearchByMovieIdRequest {
    private Long movieId;

    public Long getMovieId() {
        return movieId;
    }

    public SearchByMovieIdRequest setMovieId(Long movieId) {
        this.movieId = movieId;
        return this;
    }
}
