package com.kumofactory.cloud.appDeploy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record BuildRequestDto(
        @JsonProperty("targetInstance") String instanceId, // required
        @JsonProperty("githubToken") String gitHubToken,
        @JsonProperty("user") String user, // required
        @JsonProperty("repo") String repo, // required
        @JsonProperty("branch") String branch, // required
        @JsonProperty("Dockerfile") Boolean Dockerfile,
        @JsonProperty("language") String language, // required
        @JsonProperty("env") List<EnvInfoDTO> env) // required
{
    public BuildRequestDto setDockerfile(Boolean Dockerfile) {
        return new BuildRequestDto(instanceId, gitHubToken, user, repo, branch, Dockerfile, language, env);
    }

    public BuildRequestDto setgithubToken(String gitHubToken) {
        return new BuildRequestDto(instanceId, gitHubToken, user, repo, branch, Dockerfile, language, env);
    }

    public record EnvInfoDTO(
            @JsonProperty("key") String key,
            @JsonProperty("value") String value
    ) {
    }
}
