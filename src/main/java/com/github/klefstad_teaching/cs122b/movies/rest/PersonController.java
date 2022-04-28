package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.movies.model.data.Direction;
import com.github.klefstad_teaching.cs122b.movies.model.data.PersonOrderBy;
import com.github.klefstad_teaching.cs122b.movies.model.request.PersonRequest;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonByPersonIDResponse;
import com.github.klefstad_teaching.cs122b.movies.model.response.PersonResponse;
import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import com.github.klefstad_teaching.cs122b.movies.repo.entity.person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Types;
import java.util.List;

@RestController
public class PersonController
{
    private final MovieRepo repo;

    @Autowired
    public PersonController(MovieRepo repo)
    {
        this.repo = repo;
    }

    @GetMapping("/person/search")
    public ResponseEntity<PersonResponse> personSearch(
            PersonRequest request)
    {
        StringBuilder SQL =
                new StringBuilder("SELECT DISTINCT p.id, p.name, p.birthday, " +
                        "p.biography, p.birthplace, p.popularity, p.profile_path " +
                        "FROM movies.person p ");
        boolean whereAdded = false;
        MapSqlParameterSource source = new MapSqlParameterSource();
        if(request.getMovieTitle() != null) {
            SQL.append("JOIN movies.movie_person mp on p.id = mp.person_id " +
                    "JOIN movies.movie m on mp.movie_id = m.id " +
                    "WHERE m.title LIKE :title");
            source.addValue("title","%" + request.getMovieTitle() + "%", Types.VARCHAR);
            whereAdded = true;
        }
        if(request.getName() != null) {
            if (whereAdded) {
                SQL.append(" AND");
            } else {
                SQL.append(" WHERE");
                whereAdded = true;
            }
            SQL.append(" p.name LIKE :name");
            source.addValue("name", "%" + request.getName() + "%", Types.VARCHAR);
        }
        if(request.getBirthday() != null) {
            if (whereAdded) {
                SQL.append(" AND");
            } else {
                SQL.append(" WHERE");
            }
            SQL.append(" p.birthday = :birthday");
            source.addValue("birthday", request.getBirthday(), Types.VARCHAR);
        }
        PersonOrderBy orderBy = PersonOrderBy.fromString(request.getOrderBy());
        SQL.append(orderBy.toSQL());
        Direction direction = Direction.fromString(request.getDirection());
        SQL.append(direction.toSQL());
        SQL.append(", p.id ASC ");
        getPage(SQL, source, request.getLimit(), request.getPage());
        List<person> searchResults = repo.mapPerson(SQL.toString(), source);
        if (searchResults.size() > 0) {
            return new PersonResponse()
                    .setPersons(searchResults)
                    .setResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH)
                    .toResponse();
        } else {
            return new PersonResponse()
                    .setPersons(null)
                    .setResult(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH)
                    .toResponse();
        }
    }

    @GetMapping("/person/{personId}")
    public ResponseEntity<PersonByPersonIDResponse> personSearchByID(
            @PathVariable Long personId)
    {
        String sql = "SELECT p.id, p.name, p.birthday, p.biography, p.birthplace, p.popularity, p.profile_path " +
                "FROM movies.person p " +
                "WHERE p.id = :id";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("id", personId, Types.BIGINT);
        person Person = repo.mapPersonByID(sql, source);
        if (Person != null) {
            return new PersonByPersonIDResponse()
                    .setPerson(Person)
                    .setResult(MoviesResults.PERSON_WITH_ID_FOUND)
                    .toResponse();
        } else {
            return new PersonByPersonIDResponse()
                    .setPerson(null)
                    .setResult(MoviesResults.NO_PERSON_WITH_ID_FOUND)
                    .toResponse();
        }
    }

    static void getPage(StringBuilder SQL, MapSqlParameterSource source, Integer limit, Integer page) {
        boolean limitAdded = false;
        if (page != null) {
            if (page > 0) {
                if (limit != null) {
                    if (limit == 10 || limit == 25 || limit == 50 || limit == 100) {
                        SQL.append("Limit :page, :limit");
                        source.addValue("page", (limit*(page - 1)), Types.INTEGER)
                                .addValue("limit", limit, Types.INTEGER);
                        limitAdded = true;
                    } else {
                        throw new ResultError(MoviesResults.INVALID_LIMIT);
                    }
                } else {
                    SQL.append("Limit :page, 10");
                    source.addValue("page", (10*(page - 1)), Types.INTEGER);
                    limitAdded = true;
                }
            } else {
                throw new ResultError(MoviesResults.INVALID_PAGE);
            }
        }
        if (!limitAdded) {
            if (limit != null) {
                if (limit == 10 || limit == 25 || limit == 50 || limit == 100) {
                    SQL.append(" LIMIT :limit");
                    source.addValue("limit", limit, Types.INTEGER);
                } else {
                    throw new ResultError(MoviesResults.INVALID_LIMIT);
                }
            } else {
                SQL.append(" LIMIT 10");
            }
        }

    }
}
