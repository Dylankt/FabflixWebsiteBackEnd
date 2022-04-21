package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieRepo
{
    private final NamedParameterJdbcTemplate template;
    private final ObjectMapper objectMapper;
    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
        this.template = template;
        this.objectMapper = objectMapper;
    }

    public List<movie> map(String sql, MapSqlParameterSource source) {
        return this.template.query(
                sql,
                source,
                (rs, rowNum) ->
                        new movie()
                                .setId(rs.getLong("id"))
                                .setTitle(rs.getString("title"))
                                .setYear(rs.getInt("year"))
                                .setDirector(rs.getString("p.name"))
                                .setRating(rs.getDouble("rating"))
                                .setBackdropPath(rs.getString("backdrop_path"))
                                .setPosterPath(rs.getString("poster_path"))
                                .setHidden(rs.getBoolean("hidden"))
                );
    }

}
