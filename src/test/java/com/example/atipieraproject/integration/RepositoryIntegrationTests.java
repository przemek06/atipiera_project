package com.example.atipieraproject.integration;

import com.example.atipieraproject.client.GithubClient;
import com.example.atipieraproject.error.GithubUserNotFoundException;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.regex.Pattern;


@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RepositoryIntegrationTests {

    private static final String LOGIN_NO_FORK = "user_1";
    private static final String LOGIN_FORK = "user_2";
    private static final String INVALID_LOGIN = "user_3";

    private static final String REPOSITORY_NAME = "r_name";
    private static final String BRANCH_NAME = "b_name";
    private static final String BRANCH_COMMIT_SHA = "b_sha";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    GithubClient githubClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private Repository mockNotForkedRepository() {
        return new Repository(REPOSITORY_NAME, LOGIN_NO_FORK, false);
    }

    private Branch mockBranch() {
        return new Branch(BRANCH_NAME, BRANCH_COMMIT_SHA);
    }

    private Repository mockForkedRepository() {
        return new Repository(REPOSITORY_NAME, LOGIN_FORK, true);
    }

    @Test
    public void whenWrongAcceptValueThen406ResponseStatusTest() throws Exception {
        // given
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/repos/" + LOGIN_NO_FORK)
                .accept(MediaType.APPLICATION_XML);

        // when
        ResultActions action = mockMvc.perform(requestBuilder);

        // then
        action.andExpect(MockMvcResultMatchers.status().isNotAcceptable());
    }

    @Test
    public void whenNonExistingUserThen404ResponseStatusTest() throws Exception {
        // given
        Mockito.when(githubClient.getRepositoriesByOwnerLogin(INVALID_LOGIN))
                .thenThrow(GithubUserNotFoundException.class);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/repos/" + INVALID_LOGIN)
                .accept(MediaType.APPLICATION_JSON);

        // when
        ResultActions action = mockMvc.perform(requestBuilder);

        // then
        action.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void whenNotForkedRepositoryThenNonEmptyResponse() throws Exception {
        // given
        Mockito.when(githubClient.getBranchesByRepositoryName(LOGIN_NO_FORK, REPOSITORY_NAME))
                .thenReturn(List.of(mockBranch()));

        Mockito.when(githubClient.getRepositoriesByOwnerLogin(LOGIN_NO_FORK))
                .thenReturn(List.of(mockNotForkedRepository()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/repos/" + LOGIN_NO_FORK)
                .accept(MediaType.APPLICATION_JSON);


        // when
        ResultActions action = mockMvc.perform(requestBuilder);

        // then
        action
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String contentBody = action.andReturn().getResponse().getContentAsString();
        assert (Pattern.compile("\\[.+]").matcher(contentBody).find());
    }

    @Test
    public void whenForkedRepositoryThenEmptyResponse() throws Exception {
        // given
        Mockito.when(githubClient.getRepositoriesByOwnerLogin(LOGIN_FORK))
                .thenReturn(List.of(mockForkedRepository()));

        Mockito.when(githubClient.getBranchesByRepositoryName(LOGIN_FORK, REPOSITORY_NAME))
                .thenReturn(List.of(mockBranch()));

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/repos/" + LOGIN_NO_FORK)
                .accept(MediaType.APPLICATION_JSON);


        // when
        ResultActions action = mockMvc.perform(requestBuilder);

        // then
        action
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[]"))
                .andReturn();
    }
}
