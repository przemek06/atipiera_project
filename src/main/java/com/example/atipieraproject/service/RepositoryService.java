package com.example.atipieraproject.service;

import com.example.atipieraproject.client.GithubClient;
import com.example.atipieraproject.dto.BranchDTO;
import com.example.atipieraproject.dto.RepositoryDTO;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private final GithubClient githubClient;

    @Autowired
    public RepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }

    public List<RepositoryDTO> getRepositoriesByLogin(String login) {
        List<Repository> repositories = githubClient.getRepositoriesByOwnerLogin(login);

        return repositories.stream()
                .filter(r -> !r.getFork())
                .map(r -> r.toDTO(getBranchesByLoginAndRepository(login, r.getName())))
                .collect(Collectors.toList());
    }

    private List<BranchDTO> getBranchesByLoginAndRepository(String login, String name) {
        List<Branch> branches = githubClient.getBranchesByRepositoryName(login, name);
        return branches.stream()
                .map(Branch::toDTO)
                .collect(Collectors.toList());
    }
}
