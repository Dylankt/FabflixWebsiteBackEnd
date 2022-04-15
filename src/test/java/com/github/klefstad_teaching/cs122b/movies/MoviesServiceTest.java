package com.github.klefstad_teaching.cs122b.movies;

import com.github.klefstad_teaching.cs122b.core.result.Result;
import com.github.klefstad_teaching.cs122b.core.result.MoviesResults;
import com.github.klefstad_teaching.cs122b.core.security.JWTAuthenticationFilter;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MoviesServiceTest
{
    private static final String MOVIE_BY_ID_PATH            = "/movie/{movieId}";
    private static final String MOVIE_SEARCH_PATH           = "/movie/search";
    private static final String MOVIE_SEARCH_BY_PERSON_PATH = "/movie/search/person/{personId}";
    private static final String PERSON_BY_ID_PATH           = "/person/{personId}";
    private static final String PERSON_SEARCH_PATH          = "/person/search";

    private static final String EXPECTED_MODELS_FILE_NAME = "expected-models.json";
    private static final String USERS_FILE_NAME           = "users.json";

    private static final String LIMIT_QUERY       = "limit";
    private static final String PAGE_QUERY        = "page";
    private static final String ORDER_BY_QUERY    = "orderBy";
    private static final String DIRECTION_QUERY   = "direction";
    private static final String TITLE_QUERY       = "title";
    private static final String YEAR_QUERY        = "year";
    private static final String DIRECTOR_QUERY    = "director";
    private static final String GENRE_QUERY       = "genre";
    private static final String NAME_QUERY        = "name";
    private static final String BIRTHDAY_QUERY    = "birthday";
    private static final String MOVIE_TITLE_QUERY = "movieTitle";

    private static final long ROBERT_DOWNEY_JR_ID = 3223;
    private static final long PAGE_MASTER_ACTOR_ID = 35243;

    private static final long PAGE_MASTER_ID = 110763;
    private static final long AVENGERS_ENDGAME_ID = 4154796;

    private static final long NOT_A_REAL_MOVIE_ID  = Integer.MAX_VALUE;
    private static final long NOT_A_REAL_PERSON_ID = Integer.MAX_VALUE;

    private final MockMvc    mockMvc;
    private final JSONObject expectedModels;
    private final JSONObject users;

    private final String adminHeader;
    private final String employeeHeader;
    private final String premiumHeader;

    private final Long adminId;
    private final Long employeeId;
    private final Long premiumId;

    @Autowired
    public MoviesServiceTest(MockMvc mockMvc)
    {
        this.mockMvc = mockMvc;

        this.expectedModels = createModel(EXPECTED_MODELS_FILE_NAME);
        this.users = createModel(USERS_FILE_NAME);

        this.adminHeader = getToken("Admin@example.com");
        this.employeeHeader = getToken("Employee@example.com");
        this.premiumHeader = getToken("Premium@example.com");

        this.adminId = getId("Admin@example.com");
        this.employeeId = getId("Employee@example.com");
        this.premiumId = getId("Premium@example.com");
    }

    private String getToken(String email)
    {
        return JWTAuthenticationFilter.BEARER_PREFIX +
               ((JSONObject) this.users.get(email)).getAsString("token");
    }

    private Long getId(String email)
    {
        return ((JSONObject) this.users.get(email)).getAsNumber("id").longValue();
    }

    private JSONObject createModel(String fileName)
    {
        try {
            File file = ResourceUtils.getFile(
                ResourceUtils.CLASSPATH_URL_PREFIX + fileName
            );

            return (JSONObject) new JSONParser(JSONParser.MODE_STRICTEST)
                .parse(new FileReader(file));

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private ResultMatcher[] isResult(Result result)
    {
        return new ResultMatcher[]{
            status().is(result.status().value()),
            jsonPath("result.code").value(result.code()),
            jsonPath("result.message").value(result.message())
        };
    }

    private <T> T getModel(String modelIdentifier, Class<T> clazz)
    {
        return clazz.cast(getModel(modelIdentifier));
    }

    private Object getModel(String modelIdentifier)
    {
        String[] identifiers = modelIdentifier.split("\\.");

        if (identifiers.length == 1) {
            return expectedModels.get(modelIdentifier);
        }

        Object model = expectedModels;

        for (String identifier : identifiers) {
            model = ((JSONObject) model).get(identifier);
        }

        return model;
    }

    @Test
    public void applicationLoads()
    {
    }

    // Movie Search Tests

    @Test
    public void moviesSearchInvalidLimit()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "5"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_LIMIT))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchInvalidOrderBy()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(ORDER_BY_QUERY, "director"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_ORDER_BY))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchInvalidDirection()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(DIRECTION_QUERY, "up"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_DIRECTION))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchInvalidPage()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(PAGE_QUERY, "0"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_PAGE))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchFullDefault()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchFullDefault")));
    }

    @Test
    public void moviesSearchFullDefaultLimit()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "25"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchFullDefaultLimit")));
    }

    @Test
    public void moviesSearchFullDefaultLimitPage()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "25")
                                 .queryParam(PAGE_QUERY, "2"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchFullDefaultLimitPage")));
    }

    @Test
    public void moviesSearchByTitle()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(TITLE_QUERY, "knight"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByTitle")));
    }

    @Test
    public void moviesSearchByYear()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(YEAR_QUERY, "2010"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByYear")));
    }

    @Test
    public void moviesSearchByYearDesc()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(YEAR_QUERY, "2010")
                                 .queryParam(DIRECTION_QUERY, "desc"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByYearDesc")));
    }

    @Test
    public void moviesSearchByDirector()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(DIRECTOR_QUERY, "Anthony Russo"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByDirector")));
    }

    @Test
    public void moviesSearchByGenre()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(GENRE_QUERY, "action"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByGenre")));
    }

    @Test
    public void moviesSearchByGenreRatingDesc()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(GENRE_QUERY, "action")
                                 .queryParam(ORDER_BY_QUERY, "rating")
                                 .queryParam(DIRECTION_QUERY, "desc"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchByGenreRatingDesc")));
    }

    @Test
    public void moviesSearchNoneFound()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam("title", "there is no movie with this title"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchAdminCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .queryParam("title", "page")
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchAdminCanSeeHidden")));
    }

    @Test
    public void moviesSearchEmployeeCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .queryParam("title", "page")
                                 .header(HttpHeaders.AUTHORIZATION, employeeHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchEmployeeCanSeeHidden")));
    }

    @Test
    public void moviesSearchPremiumCanNotSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .queryParam("title", "page")
                                 .header(HttpHeaders.AUTHORIZATION, premiumHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPremiumCanNotSeeHidden")));
    }

    // Movie Search Person Tests

    @Test
    public void moviesSearchPersonInvalidLimit()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "5"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_LIMIT))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchPersonInvalidOrderBy()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(ORDER_BY_QUERY, "director"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_ORDER_BY))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchPersonInvalidDirection()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(DIRECTION_QUERY, "up"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_DIRECTION))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchPersonInvalidPage()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(PAGE_QUERY, "0"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_PAGE))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchPerson()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPerson")));
    }

    @Test
    public void moviesSearchPersonPage()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(PAGE_QUERY, "2"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonPage")));
    }

    @Test
    public void moviesSearchPersonRatingDesc()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(ORDER_BY_QUERY, "rating")
                                 .queryParam(DIRECTION_QUERY, "desc"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonRatingDesc")));
    }

    @Test
    public void moviesSearchPersonYearDesc()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(ORDER_BY_QUERY, "year")
                                 .queryParam(DIRECTION_QUERY, "desc"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonYearDesc")));
    }

    @Test
    public void moviesSearchPersonNoneFound()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, NOT_A_REAL_PERSON_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").doesNotHaveJsonPath());
    }

    @Test
    public void moviesSearchPersonAdminCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, PAGE_MASTER_ACTOR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonAdminCanSeeHidden")));
    }

    @Test
    public void moviesSearchPersonEmployeeCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, PAGE_MASTER_ACTOR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, employeeHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonEmployeeCanSeeHidden")));
    }

    @Test
    public void moviesSearchPersonPremiumCanNotSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_SEARCH_BY_PERSON_PATH, PAGE_MASTER_ACTOR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, premiumHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIES_WITH_PERSON_ID_FOUND))
                    .andExpect(jsonPath("movies").value(getModel("moviesSearchPersonPremiumCanNotSeeHidden")));
    }

    // Movie By ID Tests

    @Test
    public void movieById()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_BY_ID_PATH, AVENGERS_ENDGAME_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIE_WITH_ID_FOUND))
                    .andExpect(jsonPath("movie").value(getModel("movieById.movie")))
                    .andExpect(jsonPath("genres").value(getModel("movieById.genres")))
                    .andExpect(jsonPath("persons").value(getModel("movieById.persons")));
    }

    @Test
    public void movieByIdNoneFound()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_BY_ID_PATH, NOT_A_REAL_MOVIE_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND))
                    .andExpect(jsonPath("movie").doesNotHaveJsonPath())
                    .andExpect(jsonPath("genres").doesNotHaveJsonPath())
                    .andExpect(jsonPath("persons").doesNotHaveJsonPath());
    }



    @Test
    public void moviesByIdAdminCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_BY_ID_PATH, PAGE_MASTER_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIE_WITH_ID_FOUND))
                    .andExpect(jsonPath("movie").value(getModel("movieByIdPageMaster.movie")))
                    .andExpect(jsonPath("genres").value(getModel("movieByIdPageMaster.genres")))
                    .andExpect(jsonPath("persons").value(getModel("movieByIdPageMaster.persons")));
    }

    @Test
    public void moviesByIdEmployeeCanSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_BY_ID_PATH, PAGE_MASTER_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, employeeHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.MOVIE_WITH_ID_FOUND))
                    .andExpect(jsonPath("movie").value(getModel("movieByIdPageMaster.movie")))
                    .andExpect(jsonPath("genres").value(getModel("movieByIdPageMaster.genres")))
                    .andExpect(jsonPath("persons").value(getModel("movieByIdPageMaster.persons")));
    }

    @Test
    public void moviesByIdPremiumCanNotSeeHidden()
        throws Exception
    {
        this.mockMvc.perform(get(MOVIE_BY_ID_PATH, PAGE_MASTER_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, premiumHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_MOVIE_WITH_ID_FOUND))
                    .andExpect(jsonPath("movie").doesNotHaveJsonPath())
                    .andExpect(jsonPath("genres").doesNotHaveJsonPath())
                    .andExpect(jsonPath("persons").doesNotHaveJsonPath());
    }

    // Person Search Tests

    @Test
    public void personSearchInvalidLimit()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "5"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_LIMIT))
                    .andExpect(jsonPath("person").doesNotHaveJsonPath());
    }

    @Test
    public void personSearchInvalidOrderBy()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(ORDER_BY_QUERY, "director"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_ORDER_BY))
                    .andExpect(jsonPath("person").doesNotHaveJsonPath());
    }

    @Test
    public void personSearchInvalidDirection()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(DIRECTION_QUERY, "up"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_DIRECTION))
                    .andExpect(jsonPath("person").doesNotHaveJsonPath());
    }

    @Test
    public void personSearchInvalidPage()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(PAGE_QUERY, "0"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.INVALID_PAGE))
                    .andExpect(jsonPath("person").doesNotHaveJsonPath());
    }

    @Test
    public void personSearchDefault()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchDefault")));
    }

    @Test
    public void personSearchDefaultLimit()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "25"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchDefaultLimit")));
    }

    @Test
    public void personSearchDefaultLimitPage()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(LIMIT_QUERY, "25")
                                 .queryParam(PAGE_QUERY, "2"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchDefaultLimitPage")));
    }

    @Test
    public void personSearchByName()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(NAME_QUERY, "robert"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchByName")));
    }

    @Test
    public void personSearchByBirthday()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(BIRTHDAY_QUERY, "1974-01-30"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchByBirthday")));
    }

    @Test
    public void personSearchByMovieTitlePage()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(MOVIE_TITLE_QUERY, "endgame")
                                 .queryParam(PAGE_QUERY, "3"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel("personSearchByMovieTitlePage")));
    }

    @Test
    public void personSearchByMovieTitlePopularityDesc()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam(MOVIE_TITLE_QUERY, "endgame")
                                 .queryParam(ORDER_BY_QUERY, "popularity")
                                 .queryParam(DIRECTION_QUERY, "desc"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").value(getModel(
                        "personSearchByMovieTitlePopularityDesc")));
    }

    @Test
    public void personSearchNoneFound()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_SEARCH_PATH)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader)
                                 .queryParam("name", "there is no person with this name"))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_PERSONS_FOUND_WITHIN_SEARCH))
                    .andExpect(jsonPath("persons").doesNotHaveJsonPath());
    }

    // Person By ID Tests

    @Test
    public void personById()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_BY_ID_PATH, ROBERT_DOWNEY_JR_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.PERSON_WITH_ID_FOUND))
                    .andExpect(jsonPath("person").value(getModel("personById")));
    }

    @Test
    public void personByIdNoneFound()
        throws Exception
    {
        this.mockMvc.perform(get(PERSON_BY_ID_PATH, NOT_A_REAL_PERSON_ID)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .header(HttpHeaders.AUTHORIZATION, adminHeader))
                    .andDo(print())
                    .andExpectAll(isResult(MoviesResults.NO_PERSON_WITH_ID_FOUND))
                    .andExpect(jsonPath("person").doesNotHaveJsonPath());
    }
}
