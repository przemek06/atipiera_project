package com.example.atipieraproject.controller;

import com.example.atipieraproject.dto.RepositoryListDTO;
import com.example.atipieraproject.error.ServiceNotRespondingException;
import com.example.atipieraproject.service.RepositoryService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {

    private final RepositoryService repositoryService;

    public RepositoryController(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @GetMapping(value = "/repos/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    public RepositoryListDTO getRepositoriesByLogin(@PathVariable String login) throws ServiceNotRespondingException {
        return repositoryService.getRepositoriesByLogin(login);
    }

}
