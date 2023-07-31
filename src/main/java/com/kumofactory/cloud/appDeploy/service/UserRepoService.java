package com.kumofactory.cloud.appDeploy.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserRepoService {

    private final Logger logger = LoggerFactory.getLogger(UserRepoService.class);
    private String token;

    public List<GitHubRepoDto.RepoInfoDto> RequestOrgRepoInfo(String org) {
        String uri = "https://api.github.com/search/repositories?q=org:" + org;
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            List<GitHubRepoDto.RepoInfoDto> repoInfoList = new ArrayList<>();

            for (JsonNode node : responseBody.get("items")) {
                String name = node.get("name").asText();
                String fullName = node.get("full_name").asText();
                Boolean isPrivate = node.get("private").asBoolean();
                Boolean isFork = node.get("fork").asBoolean();

                GitHubRepoDto.RepoInfoDto repoInfo = new GitHubRepoDto.RepoInfoDto(name, fullName, isPrivate, isFork);
                repoInfoList.add(repoInfo);
            }
            logger.info("response : {}", responseBody);
            return repoInfoList;
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return null;
    }

    public GitHubRepoDto.UserDto RequestUserRepoInfoAndOrgList(String user) {
        List<GitHubRepoDto.RepoInfoDto> repoInfoList = listUserRepos(user);
        List<String> orgList = new ArrayList<>();
        if( StringUtils.hasText(token) ) {
            orgList = listOrganization();
        }

        return new GitHubRepoDto.UserDto(repoInfoList.size(), repoInfoList, orgList.size(), orgList);
    }

    public List<GitHubRepoDto.RepoInfoDto> listUserRepos(String user) {
        String uri = "https://api.github.com/search/repositories?q=user:" + user;
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri);

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();

            List<GitHubRepoDto.RepoInfoDto> repoInfoList = new ArrayList<>();

            for (JsonNode node : responseBody.get("items")) {
                String name = node.get("name").asText();
                String fullName = node.get("full_name").asText();
                Boolean isPrivate = node.get("private").asBoolean();
                Boolean isFork = node.get("fork").asBoolean();

                GitHubRepoDto.RepoInfoDto repoInfo = new GitHubRepoDto.RepoInfoDto(name, fullName, isPrivate, isFork);
                repoInfoList.add(repoInfo);
            }
            logger.info("response : {}", responseBody);
            return repoInfoList;
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return null;
    }

    public List<String> listOrganization() {
        String uri = "https://api.github.com/user/orgs";
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri);

        List<String> organizationList = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();
            logger.info("response : {}", responseBody);

            if (responseBody.isArray()) {
                for (JsonNode node : responseBody) {
                    if (node.has("login")) {
                        organizationList.add(node.get("login").asText());
                    }
                }
            }
            return organizationList;
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return organizationList;
    }

    private ResponseEntity<JsonNode> RequestGitHubAPIs(String uri) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        if( StringUtils.hasText(token) ) {
            headers.setBearerAuth(token);
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        return new RestTemplate().exchange(uri, HttpMethod.GET, httpEntity, JsonNode.class);
    }

    public List<String> RequestRepoBranches(String owner, String repo) {
        String uri = "https://api.github.com/repos/" + owner + "/" + repo + "/branches";
        ResponseEntity<JsonNode> response = RequestGitHubAPIs(uri);

        List<String> branchList = new ArrayList<>();

        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseBody = response.getBody();
            logger.info("response : {}", responseBody);

            if (responseBody.isArray()) {
                for (JsonNode node : responseBody) {
                    if (node.has("name")) {
                        branchList.add(node.get("name").asText());
                    }
                }
            }
            return branchList;
        }
        logger.error("response : {}", response.getBody());
        logger.error("response : {}", response.getStatusCode());
        return branchList;
    }
}