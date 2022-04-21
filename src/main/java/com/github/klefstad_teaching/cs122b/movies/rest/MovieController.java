package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTManager;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieDirection;
import com.github.klefstad_teaching.cs122b.movies.model.data.MovieOrderBy;
import com.github.klefstad_teaching.cs122b.movies.model.request.SearchRequest;
import com.github.klefstad_teaching.cs122b.movies.model.response.SearchResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.movie;
import com.github.klefstad_teaching.cs122b.movies.util.Validate;
import com.nimbusds.jwt.SignedJWT;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<SearchResponse> search(@AuthenticationPrincipal @NotNull SignedJWT user, SearchRequest request) throws ParseException {
        List<String> roles = user.getJWTClaimsSet().getStringListClaim(JWTManager.CLAIM_ROLES);
        boolean privileged = false;
        System.out.print(roles);
        for (String role : roles) {
            if (role.toLowerCase(Locale.ROOT).equals("admin") || role.toLowerCase(Locale.ROOT).equals("employee")) {
                privileged = true;
                break;
            }
        }
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
        MovieDirection direction = MovieDirection.fromString(request.getDirection());
        SQL.append(direction.toSQL());
        SQL.append(", m.id ASC");
        boolean isLimit = false;
        if (request.getLimit() != null) {
            if (request.getLimit() == 10 || request.getLimit() == 25 || request.getLimit() == 50 || request.getLimit() == 100) {
                SQL.append(" LIMIT :limit");
                source.addValue("limit", request.getLimit(), Types.INTEGER);
                isLimit = true;
            } else {
                throw new ResultError(MoviesResults.INVALID_LIMIT);
            }
        } else {
            SQL.append(" LIMIT 10");
        }
        if (request.getPage() != null) {
            if (request.getPage() > 0) {
                if (isLimit) {
                    SQL.append(", :limit");
                    source.addValue("limit", request.getLimit(), Types.INTEGER);
                } else {
                    SQL.append(", 10");
                }
            } else {
                throw new ResultError(MoviesResults.INVALID_PAGE);
            }
        }
        List<movie> searchResults = repo.map(SQL.toString(), source);
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
}
