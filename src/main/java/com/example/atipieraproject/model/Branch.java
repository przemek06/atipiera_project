package com.example.atipieraproject.model;

import com.example.atipieraproject.dto.BranchDTO;

public record Branch(String name, Commit commit) {

    public BranchDTO toDTO() {
        return new BranchDTO(name, commit.sha());
    }
}
