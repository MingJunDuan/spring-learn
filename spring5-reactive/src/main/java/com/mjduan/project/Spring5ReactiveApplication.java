package com.mjduan.project;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.MapUserDetailsRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

interface MovieRepository extends ReactiveMongoRepository<Movie, String> {

}

@SpringBootApplication
public class Spring5ReactiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring5ReactiveApplication.class, args);
    }

    @Bean
    RouterFunction<?> route(MovieHandler handler) {
        return RouterFunctions.route(GET("/movies"), handler::all)
                .andRoute(GET("/movies/{movieId}"), handler::byId)
                .andRoute(GET("/movies/{movieId}/events"), handler::events);
    }

    @Component
    public static class MovieHandler {

        private final MovieService movieService;

        public MovieHandler(MovieService movieService) {
            this.movieService = movieService;
        }

        public Mono<ServerResponse> all(ServerRequest serverRequest) {
            return ServerResponse.ok().body(movieService.all(), Movie.class);
        }

        public Mono<ServerResponse> byId(ServerRequest serverRequest) {
            String movidId = serverRequest.pathVariable("movieId");
            return ServerResponse.ok().body(movieService.byId(movidId), Movie.class);
        }

        public Mono<ServerResponse> events(ServerRequest serverRequest) {
            String movidId = serverRequest.pathVariable("movieId");
            return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM).body(movieService.events(movidId), MovieEvent.class);
        }
    }

}

/*@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    UserDetailsRepository userDetailsRepository() {
        //curl -v http://localhost:8080/movies
        //curl -ujoshLong:password -v http://localhost:8080/movies
        //curl -umjduan:password -v http://localhost:8080/movies | json_pp
        UserDetails joshLong = User.withUsername("joshLong").roles("USER", "ADMIN").password("password").build();
        UserDetails mjduan = User.withUsername("mjduan").roles("USER").password("password").build();
        return new MapUserDetailsRepository(joshLong, mjduan);
    }

}*/

@Component
class MovieCLR implements CommandLineRunner {

    private final MovieRepository movieRepository;

    MovieCLR(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        movieRepository.deleteAll().thenMany(
                Flux.just("Movie1", "Movie2", "Movie3", "Movie4", "Movie5", "Movie6", "Movie7", "Movie8", "Movie9", "Movie10")
                        .map(title -> new Movie(UUID.randomUUID().toString(), title))
                        .flatMap(movieRepository::save))
                .subscribe(null, null, () -> movieRepository.findAll().subscribe(System.out::println));
    }
}

/*
@RestController
@RequestMapping("/movies")
class MovieRestController {
    private final MovieService movieService;


    MovieRestController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Flux<Movie> all() {
        return movieService.all();
    }

    @GetMapping("/{movieId}")
    public Mono<Movie> byId(@PathVariable String movieId) {
        return movieService.byId(movieId);
    }

    @GetMapping(path = "/{movieId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<MovieEvent> events(@PathVariable String movieId) {
        return movieService.events(movieId);
    }
}*/

@Service
class MovieService {

    private final MovieRepository movieRepository;

    MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Flux<Movie> all() {
        return movieRepository.findAll();
    }

    public Mono<Movie> byId(String id) {
        System.out.println("byId method");
        return movieRepository.findById(id);
    }

    public Flux<MovieEvent> events(String movieId) {
        return byId(movieId).flatMapMany(movie -> {
            Flux<Long> interval = Flux.interval(Duration.ofSeconds(1));
            Flux<MovieEvent> movieEventFlux = Flux.fromStream(Stream.generate(() -> new MovieEvent(movie, new Date())));
            Flux<Tuple2<Long, MovieEvent>> tuple2Flux = Flux.zip(interval, movieEventFlux);

            return tuple2Flux.map(Tuple2::getT2);
        });
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MovieEvent {
    private Movie movie;
    private Date when;
}

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
class Movie {
    @Id
    private String id;
    private String title;

}
