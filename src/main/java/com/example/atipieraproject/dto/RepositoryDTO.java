package com.example.atipieraproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryDTO {
    private String name;
    private String owner;
    private List<BranchDTO> branches;
}
