package com.example.atipieraproject.client;

import com.example.atipieraproject.error.APIRateExceededException;
import com.example.atipieraproject.error.GithubUserNotFoundException;
import com.example.atipieraproject.error.ServiceNotRespondingException;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@Component
public class GithubClientImpl implements GithubClient {

    private final WebClient webClient;

    public GithubClientImpl(WebClient.Builder webClientBuilder, @Value("${api.github.host}") final String baseUrl) {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public List<Repository> getRepositoriesByOwnerLogin(String login) throws ServiceNotRespondingException {
        ResponseEntity<List<Repository>> response = webClient.get()
                .uri("/users/{login}/repos", login)
                .retrieve()
                .onStatus(code -> code.isSameCodeAs(HttpStatus.NOT_FOUND), r -> Mono.just(new GithubUserNotFoundException()))
                .onStatus(code -> code.isSameCodeAs(HttpStatus.FORBIDDEN), r -> Mono.just(new APIRateExceededException()))
                .toEntityList(Repository.class)
                .blockOptional()
                .orElseThrow(ServiceNotRespondingException::new);

        return response.getBody();
    }

    @Override
    public List<Branch> getBranchesByRepositoryName(String login, String name) throws ServiceNotRespondingException {
        ResponseEntity<List<Branch>> response = webClient.get()
                .uri("/repos/{login}/{name}/branches", login, name)
                .retrieve()
                .onStatus(code -> code.isSameCodeAs(HttpStatus.NOT_FOUND), r -> Mono.just(new GithubUserNotFoundException()))
                .onStatus(code -> code.isSameCodeAs(HttpStatus.FORBIDDEN), r -> Mono.just(new APIRateExceededException()))
                .toEntityList(Branch.class)
                .blockOptional()
                .orElseThrow(ServiceNotRespondingException::new);

        return response.getBody();
    }


}
