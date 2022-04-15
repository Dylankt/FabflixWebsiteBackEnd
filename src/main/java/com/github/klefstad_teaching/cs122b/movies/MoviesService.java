package com.github.klefstad_teaching.cs122b.movies;

import com.github.klefstad_teaching.cs122b.core.spring.SecuredStackService;
import com.github.klefstad_teaching.cs122b.movies.config.MoviesServiceConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@SecuredStackService
@EnableConfigurationProperties({
    MoviesServiceConfig.class
})
public class MoviesService
{
    public static void main(String[] args)
    {
        SpringApplication.run(MoviesService.class, args);
    }
}
