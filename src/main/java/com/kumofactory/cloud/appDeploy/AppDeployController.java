package com.kumofactory.cloud.appDeploy;

import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;
import com.kumofactory.cloud.appDeploy.service.UserRepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
public class AppDeployController {

    private final UserRepoService userRepoService = new UserRepoService();
//    @GetMapping("/list")
//    public UserRepoDto list() {
//        return appDeployService.RequestUserRepoInfo();
//    }

    @GetMapping("/list/{org}/repo")
    public List<GitHubRepoDto.RepoInfoDto> listOrgRepo(@PathVariable String org) {
        return userRepoService.RequestOrgRepoInfo(org);
    }

    @GetMapping("/list/{user}")
    public GitHubRepoDto.UserDto listUserRepoAndOrgs(@PathVariable String user) {
        return userRepoService.RequestUserRepoInfoAndOrgList(user);
    }

    @GetMapping("/list/{user}/{repo}/branch")
    public String listRepoBranches(@PathVariable String user, @PathVariable String repo) {
        return "listRepoBranches";
    }

    @PostMapping("/deploy")
    public BuildRequestDto deployRequest(@RequestBody BuildRequestDto request) {
        return request;
    }
}