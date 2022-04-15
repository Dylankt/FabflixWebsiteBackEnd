package com.github.klefstad_teaching.cs122b.movies.rest;

import com.github.klefstad_teaching.cs122b.movies.repo.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PersonController
{
    private final MovieRepo repo;

    @Autowired
    public PersonController(MovieRepo repo)
    {
        this.repo = repo;
    }
}
