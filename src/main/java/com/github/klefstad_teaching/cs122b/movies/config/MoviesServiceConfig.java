package com.github.klefstad_teaching.cs122b.movies.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "movie")
public class MoviesServiceConfig
{
}
