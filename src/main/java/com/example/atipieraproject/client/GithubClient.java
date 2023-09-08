package com.example.atipieraproject.client;

import com.example.atipieraproject.error.APIRateExceededException;
import com.example.atipieraproject.error.GithubUserNotFoundException;
import com.example.atipieraproject.error.ServiceNotRespondingException;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;

import java.util.List;


public interface GithubClient {

    List<Repository> getRepositoriesByOwnerLogin(String login) throws ServiceNotRespondingException;

    List<Branch> getBranchesByRepositoryName(String login, String name) throws ServiceNotRespondingException;
}