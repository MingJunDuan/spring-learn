package com.mjduan.project.spring5reactiveclient;

import java.util.Date;
import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class Spring5ReactiveClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(Spring5ReactiveClientApplication.class, args);
    }

    @Bean
    WebClient webClient() {
        return WebClient.create("http://localhost:8080/movies");
        /*return WebClient.create("http://localhost:8080/movies")
                .options().exchange().filter(t->ExchangeFilterFunctions.basicAuthentication("mjduan", "password"));*/
        /*return WebClient.create("http://localhost:8080/movies")
                .options().exchange().f*/
    }

    @Bean
    CommandLineRunner demo(WebClient webClient) {
        return args -> {
            webClient.get().uri("").retrieve()
                    .bodyToFlux(Movie.class)
                    .filter(movie -> movie.getTitle().equalsIgnoreCase("movie4"))
                    .flatMap(movie -> webClient.get().uri("/{movieId}/events",movie.getId()).retrieve().bodyToFlux(MovieEvent.class))
                    .subscribe(System.out::println);
        };
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class MovieEvent {
    private Movie movie;
    private Date when;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Movie {
    private String id;
    private String title;

}
