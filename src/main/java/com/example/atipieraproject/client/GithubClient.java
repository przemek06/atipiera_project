package com.example.atipieraproject.client;

import com.example.atipieraproject.error.GithubUserNotFoundException;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;


@FeignClient(value = "github", url = "https://api.github.com/", configuration = { })
public interface GithubClient {

    @RequestMapping(method = RequestMethod.GET, value = "/users/{login}/repos", produces = "application/json")
    List<Repository> getRepositoriesByOwnerLogin(@PathVariable String login) throws GithubUserNotFoundException;

    @RequestMapping(method = RequestMethod.GET, value = "/repos/{login}/{name}/branches", produces = "application/json")
    List<Branch> getBranchesByRepositoryName(@PathVariable String login, @PathVariable String name);
}