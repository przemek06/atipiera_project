package com.example.atipieraproject.service;

import com.example.atipieraproject.client.GithubClient;
import com.example.atipieraproject.dto.BranchDTO;
import com.example.atipieraproject.dto.RepositoryDTO;
import com.example.atipieraproject.dto.RepositoryListDTO;
import com.example.atipieraproject.error.ServiceNotRespondingException;
import com.example.atipieraproject.model.Branch;
import com.example.atipieraproject.model.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryService {

    private final GithubClient githubClient;

    public RepositoryService(GithubClient githubClient) {
        this.githubClient = githubClient;
    }


    public RepositoryListDTO getRepositoriesByLogin(String login) throws ServiceNotRespondingException {
        List<Repository> repositories = githubClient.getRepositoriesByOwnerLogin(login);

        if (repositories == null) {
            return new RepositoryListDTO(Collections.emptyList());
        }

        List<Repository> repositoriesFiltered = repositories.stream()
                .filter(r -> !r.fork())
                .toList();

        List<RepositoryDTO> repositoryDTOs = new ArrayList<>();

        for (var repository: repositoriesFiltered) {
            List<BranchDTO> branchDTOs = getBranchesByLoginAndRepository(login, repository.name());
            RepositoryDTO repositoryDTO = repository.toDTO(branchDTOs);
            repositoryDTOs.add(repositoryDTO);
        }

        return new RepositoryListDTO(repositoryDTOs);
    }

    private List<BranchDTO> getBranchesByLoginAndRepository(String login, String name) throws ServiceNotRespondingException {
        List<Branch> branches = githubClient.getBranchesByRepositoryName(login, name);

        if (branches == null) {
            return Collections.emptyList();
        }

        return branches.stream()
                .map(Branch::toDTO)
                .collect(Collectors.toList());
    }
}
