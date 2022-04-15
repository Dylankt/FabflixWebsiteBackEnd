# CS122B Backend 2 - The Movies Service

#### [Application](#application)
 - [pom.xml](#pomxml)
 - [application.yml](#applicationyml)
 - [Resources](#resources)
 - [Tests](#tests)

#### [Database](#database)
 - [Schemas](#schemas)
 - [Tables](#tables)
 - [Initial Data](#initial-data)

#### [Notes](#notes)
 - [Order of Validation](#order-of-validation)
 - [JsonInclude](#jsoninclude)
 - [Result](#result)
 - [SignedJWT](#signedjwt)
 - [Person VS Director](#person-vs-director)
 - [Substring Search](#substring-search)

#### [Endpoints](#endpoints)

1. [GET: Movie Search](#movie-search)
2. [GET: Movie Search By Person Id](#movie-search-by-person-id)
3. [GET: Movie Get By Movie Id](#movie-get-by-movie-id)
4. [GET: Person Search](#person-search)
5. [GET: Person Get By Person Id](#person-get-by-person-id)

## Application

Our application depends on a lot of files and resources to be able to run correctly. These files have been provided for you and are listed here for your reference. These files should **NEVER** be modified and must be left **AS IS**.

### pom.xml

Maven gets all its settings from a file called `pom.xml`. This file determines the dependencies we will use in our project as well as the plugins we use for compiling, testing, building, ect..

 - [pom.xml](pom.xml)

### application.yml

Spring Boot has a large number of settings that can be set with a file called `application.yml`. We have already created this file for you and have filled it with some settings. There is a file for the main application as well as one for the tests. 

 - [Main application.yml](/src/main/resources/application.yml)
 - [Test application.yml](/src/test/resources/application.yml)

### Resources

There are two folders in this project that contain resources, and application settings, as well as files required for the tests.

 - [Main Resources](/src/main/resources)
 - [Test Resources](/src/test/resources)

### Tests

There is a Single class that contain all of our test cases: 

 - [MoviesServiceTest](/src/test/java/com/github/klefstad_teaching/cs122b/movies/MoviesServiceTest.java)

## Database

### Schemas

<table>
  <thead>
    <tr>
      <th align="left" width="1100">🗄 movies</th>
    </tr>
  </thead>
</table>

### Tables

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">💾 movies.genre</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="175">Column Name</th>
      <th align="left" width="175">Type</th>
      <th align="left">Attributes</th>
    </tr>
    <tr>
      <td>id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code> <code>PRIMARY KEY</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>name</td>
      <td><code>VARCHAR(32)</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
  </tbody>
</table>

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">💾 movies.person</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="175">Column Name</th>
      <th align="left" width="175">Type</th>
      <th align="left">Attributes</th>
    </tr>
    <tr>
      <td>id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code> <code>PRIMARY KEY</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>name</td>
      <td><code>VARCHAR(128)</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>birthday</td>
      <td><code>DATE</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>biography</td>
      <td><code>VARCHAR(8192)</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>birthplace</td>
      <td><code>VARCHAR(128)</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>popularity</td>
      <td><code>DECIMAL</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>profile_path</td>
      <td><code>VARCHAR(32)</code></td>
      <td><code>NULL</code></td>
    </tr>
  </tbody>
</table>

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">💾 movies.movie</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="175">Column Name</th>
      <th align="left" width="175">Type</th>
      <th align="left">Attributes</th>
    </tr>
    <tr>
      <td>id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code> <code>PRIMARY KEY</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>title</td>
      <td><code>VARCHAR(128)</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>year</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>director_id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>rating</td>
      <td><code>DECIMAL</code></td>
      <td><code>NOT NULL</code> <code>DEFAULT 0.0</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>num_votes</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code> <code>DEFAULT 0</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>budget</td>
      <td><code>BIGINT</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>revenue</td>
      <td><code>BIGINT</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>overview</td>
      <td><code>VARCHAR(8192)</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>backdrop_path</td>
      <td><code>VARCHAR(32)</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>poster_path</td>
      <td><code>VARCHAR(32)</code></td>
      <td><code>NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>hidden</td>
      <td><code>BOOLEAN</code></td>
      <td><code>NOT NULL</code> <code>DEFAULT FALSE</code></td>
    </tr>
    <tr>
      <th colspan="3" align="left">Constraints</th>
    </tr>
    <tr>
      <td colspan="3"><code>FOREIGN KEY</code> <code>(director_id)</code> <code>REFERENCES</code> <code>movies.person (id)</code> <code>ON UPDATE CASCADE</code> <code>ON DELETE RESTRICT</code></td>
    </tr>
  </tbody>
</table>

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">💾 movies.movie_person</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="175">Column Name</th>
      <th align="left" width="175">Type</th>
      <th align="left">Attributes</th>
    </tr>
    <tr>
      <td>movie_id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>person_id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr>
      <th colspan="3" align="left">Constraints</th>
    </tr>
    <tr>
      <td colspan="3"><code>PRIMARY KEY</code> <code>(movie_id, person_id)</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td colspan="3"><code>FOREIGN KEY</code> <code>(movie_id)</code> <code>REFERENCES</code> <code>movies.movie (id)</code> <code>ON UPDATE CASCADE</code> <code>ON DELETE CASCADE</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td colspan="3"><code>FOREIGN KEY</code> <code>(person_id)</code> <code>REFERENCES</code> <code>movies.person (id)</code> <code>ON UPDATE CASCADE</code> <code>ON DELETE CASCADE</code></td>
    </tr>
  </tbody>
</table>

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">💾 movies.movie_genre</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="175">Column Name</th>
      <th align="left" width="175">Type</th>
      <th align="left">Attributes</th>
    </tr>
    <tr>
      <td>movie_id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>genre_id</td>
      <td><code>INT</code></td>
      <td><code>NOT NULL</code></td>
    </tr>
    <tr>
      <th colspan="3" align="left">Constraints</th>
    </tr>
    <tr>
      <td colspan="3"><code>PRIMARY KEY</code> <code>(movie_id, genre_id)</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td colspan="3"><code>FOREIGN KEY</code> <code>(movie_id)</code> <code>REFERENCES</code> <code>movies.movie (id)</code> <code>ON UPDATE CASCADE</code> <code>ON DELETE CASCADE</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td colspan="3"><code>FOREIGN KEY</code> <code>(genre_id)</code> <code>REFERENCES</code> <code>movies.genre (id)</code> <code>ON UPDATE CASCADE</code> <code>ON DELETE RESTRICT</code></td>
    </tr>
  </tbody>
</table>

### Initial Data

All the data to initialize your database is found in the `db` folder here: [db folder](/db). They are numbered in the order they should be executed.

# Notes

### Order of Validation
All `❗ 400: Bad Request` Results must be checked first, and returned before any other action is made. \
The order of the checks within `❗ 400: Bad Request` is not tested as each Result is tested individually.

### JsonInclude
In the case of non-successful results, where values are expected, the values should not be included, for example.
```json
{
   "result": {
      "code": 32,
      "message": "Data contains invalid integers"
   },
   "value": null 
}
```
the `value` key should not be included: 
```json
{
   "result": {
      "code": 32,
      "message": "Data contains invalid integers"
   }
}
```
This is done by insuring that all `null` values are dropped by either:
- Having your Model extend `ResponseModel<Model>`, or
- Putting the `@JsonInclude(JsonInclude.Include.NON_NULL)` on your Model class
  
### Result
All `Result` objects are available as static constants inside of the `com.github.klefstad_teaching.cs122b.core.result.MoviesResults` class.
These can be used rather than creating your own.

### SignedJWT
All endpoints in this service are considered 'privilged' as in, the user calling the endpoint must be authorized and as such must included their serialized `SignedJWT` inlcuded in the header of the request under the `Authorization` header. In the test cases you'll see that we are including these headers with JWT's for your convenience when testing.

In Spring there is a way to automatically take this header and turn it into a `SignedJWT` (This is already done for you by a provided filter here: [JWTAuthenticationFilter](https://github.com/klefstad-teaching/CS122B-Core/blob/main/src/main/java/com/github/klefstad_teaching/cs122b/core/security/JWTAuthenticationFilter.java)). There is also a way to "ask" spring for this `SignedJWT` by using the `@AuthenticationPrincipal SignedJWT user` function parameter in the endpoint like so:

```java
@GetMapping("/path")
public ResponseEntity<ResponseModel> endpoint(@AuthenticationPrincipal SignedJWT user)
{
    ...
}
```

### Person VS Director
- Our database schema has a `movie_person` table that has the list of `person` in a `movie` **NOTE** that this list does **NOT** contain the director, the director is only associated with a movie by the `director_id` column. Every movie is guaranteed to have a director **BUT** not every movie has `person` associated with them.
- For the endpoint [GET: Movie Search By Person Id](#movie-search-by-person-id) do not account for director values, search only for `persons` in `movie_person`. This should prevent the SQL Queries from becoming too complex.
 
### Substring Search
 
For queries marked as (Search by [substring](#substring-search)) make sure to have the value surrounded by '%' to allow for search by sub-string. Refer to this section in the activity: [Wildcard String Matching](https://github.com/klefstad-teaching/CS122B-A4-SQL/blob/main/README.md#wildcard-string-matching)
 
# Endpoints

## Movie Search
Returns a list of movies with basic information that match the given search parameters.

### Sorting
Since there can be *ties* in the movie list when giving a <code>ORDER BY</code> parameter we must specify a **Secondary** sorting parameter: This sorting parameter will always be <code>movie.id ASC</code> regardless of the **Primary** parameter. For example if we wanted to do sort the movies by their titles we would have the SQL Clause <code>ORDER BY movie.title, movie.id</code>, note that ASC is the default direction and thus is optional to add.

### Hidden Movies
Each movie in the <code>movies.movie</code> table has a <code>hidden</code> field. This field signifies if the movie is to be hidden away from users that do not have the <code>Admin</code> or <code>Employee</code> Role. If a user without either of these roles calls this endpoint, then all hidden movies must not be included in the results.

### String Search Parameters
For the following string search parameters (<code>title</code>, <code>director</code>, <code>genre</code>), you should use the <code>LIKE</code> operator with the <code>%</code> wildcard on both sides of the value. For example, if a value of 'knight' was given as the <code>title</code> search parameter, then the sql command should look like this: <code>title LIKE '%knight%'</code>

### Path

```http 
GET /movie/search
```

### API

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">📨&nbsp;&nbsp;Headers</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>Authorization</td>
      <td><code>String</code></td>
      <td>User's bearer token</td>
    </tr>
    <tr></tr>
    <tr>
      <td>Transaction-ID</td>
      <td><code>String</code></td>
      <td>Request's transaction id from the gateway service</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📝&nbsp;&nbsp;Query</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>title</td>
      <td><code>String</code></td>
      <td>The movie's title (Search by <a href="#substring-search">substring</a>)</td>
    </tr>
    <tr></tr>
    <tr>
      <td>year</td>
      <td><code>Integer</code></td>
      <td>The movie's release year</td>
    </tr>
    <tr></tr>
    <tr>
      <td>director</td>
      <td><code>String</code></td>
      <td>The movie's director (Search by <a href="#substring-search">substring</a>)</td>
    </tr>
    <tr></tr>
    <tr>
      <td>genre</td>
      <td><code>String</code></td>
      <td>The movie's genre (Search by <a href="#substring-search">substring</a>)</td>
    </tr>
    <tr></tr>
    <tr>
      <td>limit</td>
      <td><code>Integer</code></td>
      <td>Number of movies to list at one time: <code>10</code> (default), <code>25</code>, <code>50</code>, or <code>100</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>page</td>
      <td><code>Integer</code></td>
      <td>The page for pagination: <code>1</code> (default) or any positive number over <code>0</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>orderBy</td>
      <td><code>String</code></td>
      <td>Sorting parameter: <code>title</code> (default) or <code>rating</code> or <code>year</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>direction</td>
      <td><code>String</code></td>
      <td>Sorting direction: <code>asc</code> (default) or <code>desc</code></td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📤&nbsp;&nbsp;Response</th>
    </tr>
    <tr></tr>
    <tr>
      <th colspan="2" align="left">Model </th>
      <th align="left">Example </th>
    </tr>
    <tr>
      <td colspan="2" align="left"><pre lang="yml">
result: Result
    code: Integer
    message: String
movies: Movie[] 
    id: Long
    title: String
    year: Integer
    director: String
    rating: Double
    backdropPath: String
    posterPath: String
    hidden: Boolean</pre></td>
      <td align="left"><pre lang="json">
{
    "result": {
        "code": 2020,
        "message": "Movies with the given search parameters found"
    },
    "movies": [
        {
            "id": 4154796,
            "title": "Avengers: Endgame",
            "year": 2019,
            "director": "Joe Russo",
            "rating": 8.6,
            "backdropPath": "/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg",
            "posterPath": "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
            "hidden": false
        },
        {
            "id": 4154796,
            "title": "The Dark Knight Rises",
            "year": 2012,
            "director": "Christopher Nolan",
            "rating": 8.0,
            "backdropPath": "/f6ljQGv7WnJuwBPty017oPWfqjx.jpg",
            "posterPath": "/dEYnvnUfXrqvqeRSqvIEtmzhoA8.jpg",
            "hidden": false
        }
    ]
}</pre></td>
    </tr>    
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📦&nbsp;&nbsp;Results</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="200">Status</th>
      <th align="left">Code</th>
      <th align="left">Message</th>
    </tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2020</td>
      <td>Movies with the given search parameters found</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2021</td>
      <td>No movies found with the given search parameters</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2000</td>
      <td>Invalid 'orderBy' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2001</td>
      <td>Invalid 'direction' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2002</td>
      <td>Invalid 'limit' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2003</td>
      <td>Invalid 'offset' value given</td>
    </tr>
  </tbody>
</table>

## Movie Search By Person Id
Returns a list of movies with basic information that contain the given <code>personId</code> as a person in the movie.

### Sorting
Since there can be *ties* in the movie list when giving a <code>ORDER BY</code> parameter we must specify a **Secondary** sorting parameter: This sorting parameter will always be <code>movie.id ASC</code> regardless of the **Primary** parameter. For example if we wanted to do sort the movies by their titles we would have the SQL Clause <code>ORDER BY movie.title, movie.id</code>, note that ASC is the default direction and thus is optional to add.

### Hidden Movies
Each movie in the <code>movies.movie</code> table has a <code>hidden</code> field. This field signifies if the movie is to be hidden away from users that do not have the <code>Admin</code> or <code>Employee</code> Role. If a user without either of these roles calls this endpoint, then all hidden movies must not be included in the results.

### Ignore Director
Do not account for director values, search only for `persons` in `movie_person`. This should prevent the SQL Queries from becoming too complex.
  
### Path

```http 
GET /movie/search/person/{personId}
```

### API

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">📨&nbsp;&nbsp;Headers</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>Authorization</td>
      <td><code>String</code></td>
      <td>User's bearer token</td>
    </tr>
    <tr></tr>
    <tr>
      <td>Transaction-ID</td>
      <td><code>String</code></td>
      <td>Request's transaction id from the gateway service</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">🔖&nbsp;&nbsp;Path</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>personId</td>
      <td><code>Long</code></td>
      <td>Person ID for movies to search</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📝&nbsp;&nbsp;Query</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>limit</td>
      <td><code>Integer</code></td>
      <td>Number of movies to list at one time: <code>10</code> (default), <code>25</code>, <code>50</code>, or <code>100</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>page</td>
      <td><code>Integer</code></td>
      <td>The page for pagination: <code>1</code> (default) or any positive number over <code>0</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>orderBy</td>
      <td><code>String</code></td>
      <td>Sorting parameter: <code>title</code> (default) or <code>rating</code> or <code>year</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>direction</td>
      <td><code>String</code></td>
      <td>Sorting direction: <code>asc</code> (default) or <code>desc</code></td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📤&nbsp;&nbsp;Response</th>
    </tr>
    <tr></tr>
    <tr>
      <th colspan="2" align="left">Model </th>
      <th align="left">Example </th>
    </tr>
    <tr>
      <td colspan="2" align="left"><pre lang="yml">
result: Result
    code: Integer
    message: String
movies: Movie[] 
    id: Long
    title: String
    year: Integer
    director: String
    rating: Double
    backdropPath: String
    posterPath: String
    hidden: Boolean</pre></td>
      <td align="left"><pre lang="json">
{
    "result": {
        "code": 2030,
        "message": "Movies found with the personId"
    },
    "movies": [
        {
            "id": 1300854,
            "title": "Iron Man 3",
            "year": 2013,
            "director": "Shane Black",
            "rating": 7.0,
            "backdropPath": "/n9X2DKItL3V0yq1q1jrk8z5UAki.jpg",
            "posterPath": "/7XiGqZE8meUv7L4720L0tIDd7gO.jpg",
            "hidden": false
        },
        {
            "id": 4154796,
            "title": "Avengers: Endgame",
            "year": 2019,
            "director": "Joe Russo",
            "rating": 8.6,
            "backdropPath": "/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg",
            "posterPath": "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
            "hidden": false
        }
    ]
}</pre></td>
    </tr>    
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📦&nbsp;&nbsp;Results</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="200">Status</th>
      <th align="left">Code</th>
      <th align="left">Message</th>
    </tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2030</td>
      <td>Movies found with the personId</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2031</td>
      <td>No Movies found with the personId</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2000</td>
      <td>Invalid 'orderBy' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2001</td>
      <td>Invalid 'direction' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2002</td>
      <td>Invalid 'limit' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2003</td>
      <td>Invalid 'offset' value given</td>
    </tr>
  </tbody>
</table>


## Movie Get By Movie Id
Returns a movie with detailed information for the given <code>movieId</code>. 

### Sorting
The returned <code>genres</code> lists must be returned in **Alphabetical** order of its **name**. \
The returned <code>persons</code> lists must be returned in **Descending Popularity** primary order, then **Ascending Person ID** secondary order.

### Hidden Movies
Each movie in the <code>movies.movie</code> table has a <code>hidden</code> field. This field signifies if the movie is to be hidden away from users that do not have the <code>Admin</code> or <code>Employee</code> Role. If a user without either of these roles calls this endpoint, then all hidden movies must not be included in the results.

### Path

```http 
GET /movie/{movieId}
```

### API

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">📨&nbsp;&nbsp;Headers</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Header</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>Authorization</td>
      <td><code>String</code></td>
      <td>User's bearer token</td>
    </tr>
    <tr></tr>
    <tr>
      <td>Transaction-ID</td>
      <td><code>String</code></td>
      <td>Request's transaction id from the gateway service</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">🔖&nbsp;&nbsp;Path</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Parameter</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>movieId</td>
      <td><code>Long</code></td>
      <td>Movie id of the movie to get</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📤&nbsp;&nbsp;Response</th>
    </tr>
    <tr></tr>
    <tr>
      <th colspan="2" align="left">Model </th>
      <th align="left">Example </th>
    </tr>
    <tr>
      <td colspan="2" align="left"><pre lang="yml">
result: Result
    code: Integer
    message: String
movies: MovieDetail 
    id: Long
    title: String
    year: String
    director: String
    rating: Double
    numVotes: Long
    budget: Long
    revenue: Long
    overview: String
    backdropPath: String
    posterPath: String
    hidden: Boolean
genres: Genre[] 
    genreId: Integer
    name: String
persons: Person[] 
    personId: Integer
    name: String</pre></td>
      <td align="left"><pre lang="json">
{
    "result": {
        "code": 2010,
        "message": "Movie found with the specified ID"
    },
    "movie": {
        "id": 4154796,
        "title": "Avengers: Endgame",
        "year": 2019,
        "director": "Joe Russo",
        "rating": 8.6,
        "numVotes": 5269,
        "budget": 356000000,
        "revenue": 2485499739,
        "overview": "After the devastating events of Avengers: Infinity War... ",
        "backdropPath": "/7RyHsO4yDXtBv1zUU3mTpHeQ0d5.jpg",
        "posterPath": "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
        "hidden": false
    },
    "genres": [
        {
            "id": 28, 
            "name": "Action"
        }
    ],
    "persons": [
        {
            "id": 3223, 
            "name": "Robert Downey Jr."
        }
    ]
}</pre></td>
    </tr>    
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📦&nbsp;&nbsp;Results</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="200">Status</th>
      <th align="left">Code</th>
      <th align="left">Message</th>
    </tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2010</td>
      <td>Movie found with the specified ID</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2011</td>
      <td>No Movie found for the specified ID</td>
    </tr>
  </tbody>
</table>

## Person Search
Returns a list of people with basic information that match the given search parameters.

### Sorting
Since there can be *ties* in the person list when giving a <code>ORDER BY</code> parameter we must specify a **Secondary** sorting parameter: This sorting parameter will always be <code>person.id ASC</code> regardless of the **Primary** parameter. For example if we wanted to do sort the persons by their names we would have the SQL Clause <code>ORDER BY person.name, person.id</code>, note that ASC is the default direction and thus is optional to add.

### String Search Parameters
For the following string search parameters (<code>name</code>, <code>movieTitle</code>), you should use the <code>LIKE</code> operator with the <code>%</code> wildcard on both sides of the value. For example, if a value of 'knight' was given as the <code>movieTitle</code> search parameter, then the sql command should look like this: <code>title LIKE '%knight%'</code>

### Path

```http 
GET /person/search
```

### API

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">📨&nbsp;&nbsp;Headers</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Header</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>Authorization</td>
      <td><code>String</code></td>
      <td>User's bearer token</td>
    </tr>
    <tr></tr>
    <tr>
      <td>Transaction-ID</td>
      <td><code>String</code></td>
      <td>Request's transaction id from the gateway service</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📝&nbsp;&nbsp;Query</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Name</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>name</td>
      <td><code>String</code></td>
      <td>Name of the person to search (Search by <a href="#substring-search">substring</a>)</td>
    </tr>
    <tr></tr>
    <tr>
      <td>birthday</td>
      <td><code>String</code></td>
      <td>Birthday of the person to search</td>
    </tr>
    <tr></tr>
    <tr>
      <td>movieTitle</td>
      <td><code>String</code></td>
      <td>Movies to search the person in (Search by <a href="#substring-search">substring</a>)</td>
    </tr>
    <tr></tr>
    <tr>
      <td>limit</td>
      <td><code>Integer</code></td>
      <td>Number of movies to list at one time: <code>10</code> (default), <code>25</code>, <code>50</code>, or <code>100</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>page</td>
      <td><code>Integer</code></td>
      <td>The page for pagination: <code>1</code> (default) or any positive number over <code>0</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>orderBy</td>
      <td><code>String</code></td>
      <td>Sorting parameter: <code>name</code> (default) or <code>popularity</code> or <code>birthday</code></td>
    </tr>
    <tr></tr>
    <tr>
      <td>direction</td>
      <td><code>String</code></td>
      <td>Sorting direction: <code>asc</code> (default) or <code>desc</code></td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📤&nbsp;&nbsp;Response</th>
    </tr>
    <tr></tr>
    <tr>
      <th colspan="2" align="left">Model </th>
      <th align="left">Example </th>
    </tr>
    <tr>
      <td colspan="2" align="left"><pre lang="yml">
result: Result
    code: Integer
    message: String
persons: id
    personId: Long
    name: String
    birthday: String (nullable)
    biography: String (nullable)
    birthplace: String (nullable)
    popularity: Float (nullable)
    profilePath: String (nullable)</pre></td>
      <td align="left"><pre lang="json">
{
    "result": {
        "code": 2050,
        "message": "Persons with the given search parameters found"
    },
    "persons": [
        {
            "id": 3223,
            "name": "Robert Downey Jr.",
            "birthday": "1965-04-04",
            "biography": "Robert John Downey Jr. (born April 4, 1965)...",
            "birthplace": "Manhattan, New York City, New York, USA",
            "popularity": 18.837,
            "profilePath": "/r7WLn4Kbnqb6oJ8TmSI0e4LkWTj.jpg"
        },
        {
            "id": 3894,
            "name": "Christian Bale",
            "birthday": "1974-01-30",
            "biography": "A Welsh-born English actor...",
            "birthplace": "Haverfordwest, Pembrokeshire, Wales, UK",
            "popularity": 5.0,
            "profilePath": "/vecCvACI2QhSE5fOoANeWDjxGKM.jpg"
        }
    ]
}</pre></td>
    </tr>    
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📦&nbsp;&nbsp;Results</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="200">Status</th>
      <th align="left">Code</th>
      <th align="left">Message</th>
    </tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2050</td>
      <td>Persons with the given search parameters found</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2051</td>
      <td>No Persons found with the given search parameters</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2000</td>
      <td>Invalid 'orderBy' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2001</td>
      <td>Invalid 'direction' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2002</td>
      <td>Invalid 'limit' value given</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>❗ 400: Bad Request</code></td>
      <td>2003</td>
      <td>Invalid 'offset' value given</td>
    </tr>
  </tbody>
</table>



## Person Get by Person Id
Return detailed information for the Person matching the given <code>personId</code>

### Path

```http 
GET /person/{personId}
```

### API

<table>
  <tbody>
    <tr>
      <th colspan="3" align="left" width="1100">📨&nbsp;&nbsp;Headers</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Header</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>Authorization</td>
      <td><code>String</code></td>
      <td>User's bearer token</td>
    </tr>
    <tr></tr>
    <tr>
      <td>Transaction-ID</td>
      <td><code>String</code></td>
      <td>Request's transaction id from the gateway service</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">🔖&nbsp;&nbsp;Path</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left">Parameter</th>
      <th align="left">Type</th>
      <th align="left">Description</th>
    </tr>
    <tr>
      <td>personId</td>
      <td><code>Long</code></td>
      <td>Person id of the person to get</td>
    </tr>
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📤&nbsp;&nbsp;Response</th>
    </tr>
    <tr></tr>
    <tr>
      <th colspan="2" align="left">Model </th>
      <th align="left">Example </th>
    </tr>
    <tr>
      <td colspan="2" align="left"><pre lang="yml">
result: Result
    code: Integer
    message: String
person: PersonDetail
    id: Long
    name: String
    birthday: String (nullable)
    biography: String (nullable)
    birthplace: String (nullable)
    popularity: Float (nullable)
    profilePath: String (nullable)</pre></td>
      <td align="left"><pre lang="json">
{
    "result": {
        "code": 2040,
        "message": "Person found with the specified ID"
    },
    "person": {
        "id": 3223,
        "name": "Robert Downey Jr.",
        "birthday": "1965-04-04",
        "biography": "Robert John Downey Jr. (born April 4, 1965)...",
        "birthplace": "Manhattan, New York City, New York, USA",
        "popularity": 18.837,
        "profilePath": "/r7WLn4Kbnqb6oJ8TmSI0e4LkWTj.jpg"
    } 
}</pre></td>
    </tr>    
    <tr><td colspan="3" ></td></tr>
    <tr></tr>
    <tr>
      <th colspan="3" align="left">📦&nbsp;&nbsp;Results</th>
    </tr>
    <tr></tr>
    <tr>
      <th align="left" width="200">Status</th>
      <th align="left">Code</th>
      <th align="left">Message</th>
    </tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2040</td>
      <td>Person found with the specified ID</td>
    </tr>
    <tr></tr>
    <tr>
      <td><code>✅ 200: Ok</code></td>
      <td>2041</td>
      <td>No Person found for the specified ID</td>
    </tr>
  </tbody>
</table>
