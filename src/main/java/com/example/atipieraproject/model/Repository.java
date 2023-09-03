package com.example.atipieraproject.model;

import com.example.atipieraproject.dto.BranchDTO;
import com.example.atipieraproject.dto.RepositoryDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Repository {
    private String name;
    private String ownerName;
    private Boolean fork;

    @JsonProperty("owner")
    public void setOwner(Map<String, String> owner) {
        this.ownerName = owner.get("login");
    }
    public RepositoryDTO toDTO(List<BranchDTO> branchDTOs) {
        return RepositoryDTO.builder()
                .name(name)
                .owner(ownerName)
                .branches(branchDTOs)
                .build();
    }
}
