package com.example.atipieraproject.model;

import com.example.atipieraproject.dto.BranchDTO;
import com.example.atipieraproject.dto.RepositoryDTO;

import java.util.List;

public record Repository(String name,
                         Owner owner,
                         Boolean fork) {

    public RepositoryDTO toDTO(List<BranchDTO> branchDTOs) {
        return new RepositoryDTO(name, owner.login(), branchDTOs);
    }
}
