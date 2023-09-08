package com.example.atipieraproject.dto;

import java.util.List;

public record RepositoryDTO (String name, String owner, List<BranchDTO> branches) {
}
