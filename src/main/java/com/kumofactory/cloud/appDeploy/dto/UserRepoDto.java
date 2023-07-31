package com.kumofactory.cloud.appDeploy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record UserRepoDto(
        @JsonProperty("personal_repo_count") Integer personalRepoCount,
        @JsonProperty("personal_repo") List<RepoInfoDto> personalRepo,
        @JsonProperty("organization_count") Integer organizationCount,
        @JsonProperty("organization") List<OrganizationDto> organization
) {
    public record RepoInfoDto(
            @JsonProperty("name") String name,
            @JsonProperty("full_name") String fullName,
            @JsonProperty("private") Boolean isPrivate,
            @JsonProperty("fork") Boolean isFork
    ) { }
    public record OrganizationDto(
            @JsonProperty("organization_name") String organizationName,
            @JsonProperty("organization_repo_count") Integer organizationRepoCount,
            @JsonProperty("organization_repo") List<RepoInfoDto> organizationRepo
    ) { }
}