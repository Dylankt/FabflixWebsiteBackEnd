package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.Direction;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieOrderBy;
import com.github.klefstad_teaching.cs122b.movies.model.request.SearchByPersonIDRequest;
import com.github.klefstad_teaching.cs122b.movies.model.request.SearchRequest;
import com.github.klefstad_teaching.cs122b.movies.model.response.SearchByMovieIDReponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.SearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.*;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@RestController
public class MovieController
{
    private final MovieRepo repo;

    @Autowired
    public MovieController(MovieRepo repo, Validate validate)
    {
        this.repo = repo;
    }

    @GetMapping("/movie/search")
    public ResponseEntity<SearchResponse> movieSearch(
            @AuthenticationPrincipal @NotNull SignedJWT user, SearchRequest request)
            throws ParseException
    {
        boolean privileged = authenticateUser(user);
        StringBuilder SQL =
                new StringBuilder("SELECT Distinct m.id, m.title, m.year," +
                        " p.name, JSON_ARRAYAGG(g.name), m.rating, m.backdrop_path," +
                        " m.poster_path, m.hidden " +
                        "FROM movies.movie m " +
                        "JOIN movies.movie_genre mg on m.id = mg.movie_id " +
                        "JOIN movies.genre g on g.id = mg.genre_id " +
                        "JOIN movies.person p on m.director_id = p.id");
        MapSqlParameterSource source = new MapSqlParameterSource();
        boolean whereAdded = false;
        if (request.getTitle() != null) {
            SQL.append(" Where m.title LIKE :title");
            String wildSearch = "%" + request.getTitle() + "%";
            source.addValue("title", wildSearch, Types.VARCHAR);
            whereAdded = true;
        }
        if (request.getYear() != null) {
            if (whereAdded) {
                SQL.append(" AND");
            } else {
                SQL.append(" WHERE");
                whereAdded = true;
            }
            SQL.append(" m.year = :year");
            source.addValue("year", request.getYear(), Types.INTEGER);
        }
        if (request.getDirector() != null) {
            if (whereAdded) {
                SQL.append(" AND");
            } else {
                SQL.append(" WHERE");
                whereAdded = true;
            }
            SQL.append(" p.name LIKE :director");
            source.addValue("director", "%" + request.getDirector() + "%", Types.VARCHAR);
        }
        if (request.getGenre() != null) {
            if (whereAdded) {
                SQL.append(" AND");
            } else {
                SQL.append(" WHERE");
                whereAdded = true;
            }
            SQL.append(" g.name LIKE :genre");
            source.addValue("genre", "%" + request.getGenre() + "%", Types.VARCHAR);
        }
        if (!privileged) {
            if (whereAdded) {
                SQL.append(" AND m.hidden = false");
            } else {
                SQL.append(" WHERE m.hidden = false");
            }
        }
        SQL.append(" GROUP BY m.id, m.title, m.year, p.name, m.rating, m.backdrop_path, m.poster_path");
        MovieOrderBy orderBy = MovieOrderBy.fromString(request.getOrderBy());
        SQL.append(orderBy.toSQL());
        Direction direction = Direction.fromString(request.getDirection());
        SQL.append(direction.toSQL());
        SQL.append(", m.id ASC ");
        PersonController.getPage(SQL, source, request.getLimit(), request.getPage());
        List<movie> searchResults = repo.mapMovieSearch(SQL.toString(), source);
        SearchResponse response;
        if (searchResults.size() > 0) {
            response = new SearchResponse()
                    .setMovies(searchResults)
                    .setResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH);
        } else {
            response = new SearchResponse()
                    .setMovies(null)
                    .setResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH);
        }
        return response.toResponse();
    }

    @GetMapping("/movie/search/person/{personId}")
    public ResponseEntity<SearchResponse> movieSearchByPersonID(
            @AuthenticationPrincipal @NotNull SignedJWT user, @PathVariable Long personId, SearchByPersonIDRequest request)
            throws ParseException
    {
        boolean privileged = authenticateUser(user);
        StringBuilder sql =
                new StringBuilder("SELECT Distinct m.id, m.title, m.year, p.name, m.rating, " +
                        "m.backdrop_path, m.poster_path, m.hidden, p.popularity, m.director_id, p.name " +
                        "FROM movies.movie m " +
                        "JOIN movies.movie_person mp on mp.movie_id = m.id " +
                        "JOIN movies.person p on p.id = m.director_id " +
                        "WHERE mp.person_id = :id ");
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", personId, Types.BIGINT);
        if (!privileged) {
            sql.append("AND m.hidden = false ");
        }
        MovieOrderBy orderBy = MovieOrderBy.fromString(request.getOrderBy());
        sql.append(orderBy.toSQL());
        Direction direction = Direction.fromString(request.getDirection());
        sql.append(direction.toSQL());
        sql.append(", m.id ASC ");
        PersonController.getPage(sql, source, request.getLimit(), request.getPage());
        List<movie> searchResults = repo.mapMovieSearch(sql.toString(), source);
        SearchResponse response;
        if (searchResults.size() > 0) {
            response = new SearchResponse()
                    .setMovies(searchResults)
                    .setResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND);
        } else {
            response = new SearchResponse()
                    .setMovies(null)
                    .setResult(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND);
        }
        return response.toResponse();
    }

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<SearchByMovieIDReponse> searchByMovieId(
            @AuthenticationPrincipal SignedJWT user, @PathVariable Long movieId)
            throws ParseException
    {
        boolean privilege = authenticateUser(user);
        String movieSql = "SELECT Distinct m.id, m.title, m.year, p.name, m.rating, m.num_votes, m.budget, " +
                "m.revenue, m.overview, m.backdrop_path, " +
                "m.poster_path, m.hidden " +
                "FROM movies.movie m " +
                "    JOIN movies.person p on p.id = m.director_id " +
                "WHERE m.id = :id ";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", movieId, Types.BIGINT);
        movieDetailed Movie = repo.mapMovie(movieSql,source);
        String genreSql = "SELECT Distinct g.id, g.name " +
                "FROM movies.movie m " +
                "JOIN movies.movie_genre mg on m.id = mg.movie_id " +
                "JOIN movies.genre g on g.id = mg.genre_id " +
                "WHERE m.id = :id " + "ORDER BY g.name";
        List<genre> genres = repo.mapGenres(genreSql, source);
        String personsSQL = "SELECT p.id, p.name, m.title, p.popularity " +
                "FROM movies.person p, movies.movie m, movies.movie_person mp " +
                "WHERE p.id = mp.person_id AND mp.movie_id = m.id AND m.id = :id " +
                "ORDER BY p.popularity DESC";
        List<personSimple> persons = repo.mapPersonSimple(personsSQL, source);
        if(Movie != null) {
            if (!Movie.getHidden() || privilege) {
                return new SearchByMovieIDReponse()
                        .setMovie(Movie)
                        .setGenres(genres)
                        .setPersons(persons)
                        .setResult(MoviesResults.MOVIE_WITH_ID_FOUND)
                        .toResponse();
            } else {
                return new SearchByMovieIDReponse()
                        .setMovie(null)
                        .setGenres(null)
                        .setPersons(null)
                        .setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND)
                        .toResponse();
            }
        } else {
            return new SearchByMovieIDReponse()
                    .setMovie(null)
                    .setGenres(null)
                    .setPersons(null)
                    .setResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND)
                    .toResponse();
        }
    }

    private boolean authenticateUser(@AuthenticationPrincipal @NotNull SignedJWT user) throws ParseException {
        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        boolean privileged = false;
        System.out.print(roles);
        for (String role : roles) {
            if (role.toLowerCase(Locale.ROOT).equals("admin") || role.toLowerCase(Locale.ROOT).equals("employee")) {
                privileged = true;
                break;
            }
        }
        return privileged;
    }
}
