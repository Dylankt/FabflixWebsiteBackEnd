package com.github.klefstad_teaching.cs122b.movies.repo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MovieRepo
{
    @Autowired
    public MovieRepo(ObjectMapper objectMapper, NamedParameterJdbcTemplate template)
    {
    }
}
