package com.example.atipieraproject.model;

import com.example.atipieraproject.dto.BranchDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch {
    private String name;
    private String lastCommitSha;

    @JsonProperty("commit")
    public void setLastCommitSha(Map<String, String> commit) {
        lastCommitSha = commit.get("sha");
    }

    public BranchDTO toDTO() {
        return BranchDTO.builder()
                .name(name)
                .lastCommitSha(lastCommitSha)
                .build();
    }
}
