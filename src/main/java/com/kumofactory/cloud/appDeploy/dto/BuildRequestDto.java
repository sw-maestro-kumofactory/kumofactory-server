package com.kumofactory.cloud.appDeploy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BuildRequestDto(
        @JsonProperty("target-instance") String instanceId, // required
        @JsonProperty("github-token") String gitHubToken,
        @JsonProperty("user") String user, // required
        @JsonProperty("repo") String repo, // required
        @JsonProperty("branch") String branch, // required
        @JsonProperty("Dockerfile") Boolean dockerfile,
        @JsonProperty("Language") String language, // required
        @JsonProperty("env") List<EnvInfoDTO> env) // required
 {
    public record EnvInfoDTO(
            @JsonProperty("key") String key,
            @JsonProperty("value") String value
    ) { }

     public BuildRequestDto setDockerfile(Boolean dockerfile) {
         return new BuildRequestDto(instanceId, gitHubToken, user, repo, branch, dockerfile, language, env);
     }

     public BuildRequestDto setgithubToken(String gitHubToken) {
         return new BuildRequestDto(instanceId, gitHubToken, user, repo, branch, dockerfile, language, env);
     }
}
