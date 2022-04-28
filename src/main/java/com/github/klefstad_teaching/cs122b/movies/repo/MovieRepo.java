package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

    public List<movie> mapMovieSearch(String sql, MapSqlParameterSource source) {
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

    public List<person> mapPerson(String sql, MapSqlParameterSource source) {
        return this.template.query(
                sql,
                source,
                (rs, rowNum) ->
                        new person()
                                .setId(rs.getLong("id"))
                                .setName(rs.getString("name"))
                                .setBirthday(rs.getString("birthday"))
                                .setBiography(rs.getString("biography"))
                                .setBirthplace(rs.getString("birthplace"))
                                .setPopularity(rs.getFloat("popularity"))
                                .setProfilePath(rs.getString("profile_path"))
        );
    }

    public director mapDirector(String sql, MapSqlParameterSource source) {
        try {
            return this.template.queryForObject(
                    sql,
                    source,
                    (rs, rowNum) ->
                            new director()
                                    .setId(rs.getLong("p.id"))
                                    .setName(rs.getString("p.name"))
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public movieDetailed mapMovie(String sql, MapSqlParameterSource source) {
        try {
            List<movieDetailed> movie = this.template.query(
                    sql,
                    source,
                    (rs, rowNum) ->
                            new movieDetailed()
                                    .setId(rs.getLong("id"))
                                    .setTitle(rs.getString("title"))
                                    .setYear(rs.getInt("year"))
                                    .setDirector(rs.getString("p.name"))
                                    .setRating(rs.getDouble("rating"))
                                    .setNumVotes(rs.getLong("num_votes"))
                                    .setBudget(rs.getLong("budget"))
                                    .setRevenue(rs.getLong("revenue"))
                                    .setOverview(rs.getString("overview"))
                                    .setBackdropPath(rs.getString("backdrop_path"))
                                    .setPosterPath(rs.getString("poster_path"))
                                    .setHidden(rs.getBoolean("hidden"))
            );
            if(movie.size() == 1) {
                return movie.get(0);
            } else {
                return null;
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<genre> mapGenres(String sql, MapSqlParameterSource source)
    {
        return this.template.query(
                sql,
                source,
                (rs, rowNum) ->
                        new genre()
                                .setId(rs.getLong("id"))
                                .setName(rs.getString("name"))
        );
    }

    public List<personSimple> mapPersonSimple(String sql, MapSqlParameterSource source)
    {
        return this.template.query(
                sql,
                source,
                (rs, rowNum) ->
                        new personSimple()
                                .setId(rs.getLong("id"))
                                .setName(rs.getString("name"))
        );
    }

    public person mapPersonByID(String sql, MapSqlParameterSource source) {
        try {
            return this.template.queryForObject(
                    sql,
                    source,
                    (rs, rowNum) ->
                            new person()
                                    .setId(rs.getLong("id"))
                                    .setName(rs.getString("name"))
                                    .setBirthday(rs.getString("birthday"))
                                    .setBiography(rs.getString("biography"))
                                    .setBirthplace(rs.getString("birthplace"))
                                    .setPopularity(rs.getFloat("popularity"))
                                    .setProfilePath(rs.getString("profile_path"))
            );
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
