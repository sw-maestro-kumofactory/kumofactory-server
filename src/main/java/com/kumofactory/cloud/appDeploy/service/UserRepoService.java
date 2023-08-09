package com.kumofactory.cloud.appDeploy.service;

import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;

import java.util.List;

public interface UserRepoService {
    public List<GitHubRepoDto.RepoInfoDto> RequestOrgRepoInfo(String org, String oauthId);

    public GitHubRepoDto.UserDto RequestUserRepoInfoAndOrgList(String oauthId);

    public List<String> RequestRepoBranches(String org, String repo, String oauthId);

    public List<String> RequestRepoBranches(String repo, String oauthId);
}
