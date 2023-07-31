package com.kumofactory.cloud.appDeploy;

import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;
import com.kumofactory.cloud.appDeploy.service.BuildRequestService;
import com.kumofactory.cloud.appDeploy.service.UserRepoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
public class AppDeployController {
    Logger logger = org.slf4j.LoggerFactory.getLogger(AppDeployController.class);

    private final UserRepoService userRepoService = new UserRepoService();
    private final BuildRequestService buildRequestService = new BuildRequestService();

    @GetMapping("/list/{org}/repo")
    public List<GitHubRepoDto.RepoInfoDto> listOrgRepo(@PathVariable String org) {
        return userRepoService.RequestOrgRepoInfo(org);
    }

    @GetMapping("/list/{user}")
    public GitHubRepoDto.UserDto listUserRepoAndOrgs(@PathVariable String user) {
        return userRepoService.RequestUserRepoInfoAndOrgList(user);
    }

    @GetMapping("/list/{owner}/{repo}/branch")
    public List<String> listRepoBranches(@PathVariable String owner, @PathVariable String repo) {
        return userRepoService.RequestRepoBranches(owner, repo);
    }

    @PostMapping("/deploy")
    public ResponseEntity<String> deployRequest(@RequestBody BuildRequestDto request) {
        logger.info("request : {}", "message");
        buildRequestService.RequestBuild(request);
//        logger.info("request : {}", request);

        return ResponseEntity.ok("success");
    }

    @GetMapping("/health")
    public ResponseEntity<String> checkHealth() {
        return ResponseEntity.ok("success");
    }
}