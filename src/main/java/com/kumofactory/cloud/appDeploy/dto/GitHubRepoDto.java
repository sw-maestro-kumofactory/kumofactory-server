package com.kumofactory.cloud.appDeploy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GitHubRepoDto {
    public record RepoInfoDto(
            @JsonProperty("name") String name,
            @JsonProperty("fullName") String fullName,
            @JsonProperty("private") Boolean isPrivate,
            @JsonProperty("fork") Boolean isFork,
            @JsonProperty("description") String description,
            @JsonProperty("languagesUrl") String languagesUrl,
            @JsonProperty("stargazersUrl") String stargazersUrl,
            @JsonProperty("openIssuesCount") Integer openIssuesCount

    ) { }

    public record UserDto(
//            @JsonProperty("personal_repo_count") Integer personalRepoCount,
//            @JsonProperty("personal_repo") List<RepoInfoDto> personalRepo,
//            @JsonProperty("organization_count") Integer organizationCount,
//            @JsonProperty("organization") List<String> organizationList
            @JsonProperty("repoCount") Integer personalRepoCount,
            @JsonProperty("repoInfo") List<RepoInfoDto> personalRepo,
            @JsonProperty("orgCount") Integer organizationCount,
            @JsonProperty("orgList") List<String> organizationList
    ) { }
}
