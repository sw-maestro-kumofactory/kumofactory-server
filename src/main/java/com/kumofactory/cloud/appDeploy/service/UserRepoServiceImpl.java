package com.kumofactory.cloud.appDeploy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Repository
public class UserRepoServiceImpl implements UserRepoService {

    private final Logger logger = LoggerFactory.getLogger(UserRepoService.class);
    private final MemberRepository memberRepository;

    @Override
    public List<GitHubRepoDto.RepoInfoDto> RequestOrgRepoInfo(String org, String oauthId) {
        Member member = memberRepository.findMemberByOauthId(oauthId);
        String token = member.getGithubAccessToken();
        String uri = "https://api.github.com/search/repositories?q=org:" + org;
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri, token);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            List<GitHubRepoDto.RepoInfoDto> repoInfoList = new ArrayList<>();

            for (JsonNode node : responseBody.get("items")) {
                String name = node.get("name").asText();
                String fullName = node.get("full_name").asText();
                Boolean isPrivate = node.get("private").asBoolean();
                Boolean isFork = node.get("fork").asBoolean();
                Integer forksCount = node.get("forks_count").asInt();
                String description = node.get("description").asText();
                String language = node.get("language").asText();
                String stargazers_url = node.get("stargazers_url").asText();
                Integer starCount = node.get("stargazers_count").asInt();
                Integer open_issues_count = node.get("open_issues_count").asInt();
                String visibility = node.get("visibility").asText();
                String updatedAt = node.get("updated_at").asText();
                String createdAt = node.get("created_at").asText();



                GitHubRepoDto.RepoInfoDto repoInfo =
                        new GitHubRepoDto.RepoInfoDto(name, fullName, isPrivate, isFork, forksCount, description, language, stargazers_url, starCount , open_issues_count, visibility, updatedAt, createdAt);
                repoInfoList.add(repoInfo);
            }
            return repoInfoList;
        }
        return null;
    }

    @Override
    public GitHubRepoDto.UserDto RequestUserRepoInfoAndOrgList(String oauthId) {
        Member member = memberRepository.findMemberByOauthId(oauthId);
        String owner = member.getProfileName();
        String token = member.getGithubAccessToken();
        List<GitHubRepoDto.RepoInfoDto> repoInfoList = listUserRepos(owner, token);
        List<String> orgList = new ArrayList<>();
        if (StringUtils.hasText(token)) {
            orgList = listOrganization(token);
        }

        return new GitHubRepoDto.UserDto(repoInfoList.size(), repoInfoList, orgList.size(), orgList);
    }

    private List<GitHubRepoDto.RepoInfoDto> listUserRepos(String user, String token) {
        String uri = "https://api.github.com/search/repositories?q=user:" + user;
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri, token);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            List<GitHubRepoDto.RepoInfoDto> repoInfoList = new ArrayList<>();


            for (JsonNode node : responseBody.get("items")) {
                String name = node.get("name").asText();
                String fullName = node.get("full_name").asText();
                Boolean isPrivate = node.get("private").asBoolean();
                Boolean isFork = node.get("fork").asBoolean();
                Integer forksCount = node.get("forks_count").asInt();
                String description = node.get("description").asText();
                String language = node.get("language").asText();
                String stargazers_url = node.get("stargazers_url").asText();
                Integer starCount = node.get("stargazers_count").asInt();
                Integer open_issues_count = node.get("open_issues_count").asInt();
                String visibility = node.get("visibility").asText();
                String updatedAt = node.get("updated_at").asText();
                String createdAt = node.get("created_at").asText();


                GitHubRepoDto.RepoInfoDto repoInfo =
                        new GitHubRepoDto.RepoInfoDto(name, fullName, isPrivate, isFork, forksCount, description, language, stargazers_url, starCount ,open_issues_count, visibility, updatedAt, createdAt);
                repoInfoList.add(repoInfo);
            }
            return repoInfoList;
        }
        return null;
    }

    private List<String> listOrganization(String token) {
        String uri = "https://api.github.com/user/orgs";
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri, token);

        List<String> organizationList = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            if (responseBody.isArray()) {
                for (JsonNode node : responseBody) {
                    if (node.has("login")) {
                        organizationList.add(node.get("login").asText());
                    }
                }
            }
            return organizationList;
        }
        return organizationList;
    }

    @Override
    public List<String> RequestRepoBranches(String org, String repo, String oauthId) {
        Member member = memberRepository.findMemberByOauthId(oauthId);
        String token = member.getGithubAccessToken();
        String uri = "https://api.github.com/repos/" + org + "/" + repo + "/branches";
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri, token);

        List<String> branchList = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            if (responseBody.isArray()) {
                for (JsonNode node : responseBody) {
                    if (node.has("name")) {
                        branchList.add(node.get("name").asText());
                    }
                }
            }
            return branchList;
        }
        return branchList;
    }

    @Override
    public List<String> RequestRepoBranches(String repo, String oauthId) {
        Member member = memberRepository.findMemberByOauthId(oauthId);
        String profileName = member.getProfileName();
        return RequestRepoBranches(profileName, repo, oauthId);
    }

    private ResponseEntity<JsonNode> RequestGitHubAPIs(String uri, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        if (StringUtils.hasText(token)) {
            headers.setBearerAuth(token);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
    }
}