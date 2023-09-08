package com.example.atipieraproject;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.charset.Charset;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "api.github.host=http://localhost:8089/"
})
@AutoConfigureWebTestClient
@RunWith(SpringRunner.class)
public class RepositoryIntegrationTests {

    private static final String LOGIN_NON_EMPTY = "user_1";
    private static final String LOGIN_EMPTY = "user_2";
    private static final String LOGIN_NON_EXISTING = "user_3";
    private static final String REPOSITORY_NAME = "r_name";
    private static final String EXPECTED_JSON_EMPTY_BODY = "{\"repositories\": []}";
    private static final String EXPECTED_JSON_NON_EMPTY_BODY = "{\"repositories\": [" +
            "{\"name\": \"r_name\", \"owner\": \"user_1\", \"branches\": [" +
            "{\"name\": \"BRANCH_NAME\", \"BRANCH_COMMIT_SHA\": \"lastCommitSha_1\"}" +
            "]}" +
            "]}";

    @Autowired
    WebTestClient webTestClient;
    @Autowired
    private ResourceLoader resourceLoader;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);

    @Test
    public void whenWrongAcceptValueThen406ResponseStatusTest() {
        // when
        var exc = webTestClient.get()
                .uri("/repos/{login}", LOGIN_NON_EMPTY)
                .accept(MediaType.APPLICATION_XML)
                .exchange();
        // then
        exc.expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE);
    }

    @Test
    public void whenNonExistingUserThen404ResponseStatusTest() {
        // given
        WireMock.stubFor(WireMock.get(urlEqualTo("/users/" + LOGIN_NON_EXISTING + "/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                )
        );

        // when
        var exc = webTestClient.get()
                .uri("/repos/{login}", LOGIN_NON_EXISTING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
        // then
        exc.expectStatus().isNotFound();
    }

    @Test
    public void whenUserWithNoReposThenEmptyResponseTest() {
        // given
        WireMock.stubFor(WireMock.get(urlEqualTo("/users/" + LOGIN_EMPTY + "/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[]")
                )
        );

        // when
        var exc = webTestClient.get()
                .uri("/repos/{login}", LOGIN_EMPTY)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then
        exc.expectStatus().isOk();
        exc.expectBody().json(EXPECTED_JSON_EMPTY_BODY);
    }

    @Test
    public void whenUserWithReposThenNonEmptyResponse() throws IOException {
        // given
        String branchesJson = resourceLoader.getResource("classpath:branches.json").getContentAsString(Charset.defaultCharset());
        String reposJson = resourceLoader.getResource("classpath:repositories.json").getContentAsString(Charset.defaultCharset());
        String responseJson = resourceLoader.getResource("classpath:expected.json").getContentAsString(Charset.defaultCharset());

        WireMock.stubFor(WireMock.get(urlEqualTo("/users/" + LOGIN_NON_EMPTY + "/repos"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(reposJson)
                )
        );

        WireMock.stubFor(WireMock.get(urlEqualTo("/repos/" + LOGIN_NON_EMPTY + "/" + REPOSITORY_NAME + "/branches"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(branchesJson)
                )
        );

        // when
        var exc = webTestClient.get()
                .uri("/repos/{login}", LOGIN_NON_EMPTY)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        // then
        exc.expectStatus().isOk();
        exc.expectBody().json(responseJson);
    }

}
