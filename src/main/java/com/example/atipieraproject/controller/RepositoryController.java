package com.example.atipieraproject.controller;

import com.example.atipieraproject.dto.RepositoryDTO;
import com.example.atipieraproject.service.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RepositoryController {

    private final RepositoryService repositoryService;

    @Autowired
    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "/repos/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RepositoryDTO> getRepositoriesByLogin(@PathVariable String login) {
        return repositoryService.getRepositoriesByLogin(login);
    }

}
