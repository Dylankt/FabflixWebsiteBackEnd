package com.github.klefstad_teaching.cs122b.movies.model.data;

import com.github.klefstad_teaching.cs122b.core.error.ResultError;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;

import java.util.Locale;

public enum PersonOrderBy {
    NAME(" ORDER BY p.name"),
    POPULARITY(" ORDER BY p.popularity"),
    BIRTHDAY(" ORDER BY p.birthday");

    private final String sql;

    PersonOrderBy(String sql) {
        this.sql = sql;
    }

    public String toSQL() {
        return sql;
    }

    public static PersonOrderBy fromString(String orderBy) {
        if (orderBy == null) {
            return NAME;
        }
        switch (orderBy.toUpperCase(Locale.ROOT)) {
            case "NAME":
                return NAME;
            case "POPULARITY":
                return POPULARITY;
            case "BIRTHDAY":
                return BIRTHDAY;
            default:
                throw new ResultError(MoviesResults.INVALID_ORDER_BY);
        }
    }
}
