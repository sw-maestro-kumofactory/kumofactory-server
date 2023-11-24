package com.kumofactory.cloud.appDeploy;

import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import com.kumofactory.cloud.appDeploy.dto.GitHubRepoDto;
import com.kumofactory.cloud.appDeploy.service.BuildRequestService;
import com.kumofactory.cloud.appDeploy.service.UserRepoService;
import com.kumofactory.cloud.global.annotation.auth.AuthorizationFromToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/build")
@RequiredArgsConstructor
@Slf4j
public class AppDeployController {
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(AppDeployController.class);

    private final UserRepoService userRepoService;
    private final BuildRequestService buildRequestService;

    @GetMapping("/list/{org}/repo")
    @AuthorizationFromToken
    public List<GitHubRepoDto.RepoInfoDto> listOrgRepo(@PathVariable String org, String userId) {
        return userRepoService.RequestOrgRepoInfo(org, userId);
    }

    @GetMapping("/list")
    @AuthorizationFromToken
    public GitHubRepoDto.UserDto listUserRepoAndOrgs(String userId) {
        return userRepoService.RequestUserRepoInfoAndOrgList(userId);
    }

    @GetMapping("/list/{org}/{repo}/branch")
    @AuthorizationFromToken
    public List<String> listRepoBranches(@PathVariable String org, @PathVariable String repo, String userId) {
        return userRepoService.RequestRepoBranches(org, repo, userId);
    }

    @GetMapping("/list/{repo}/branch")
    @AuthorizationFromToken
    public List<String> listRepoBranches(@PathVariable String repo, String userId) {
        return userRepoService.RequestRepoBranches(repo, userId);
    }

    @PostMapping("/deploy")
    @AuthorizationFromToken
    public ResponseEntity<String> deployRequest(@RequestBody BuildRequestDto request, String userId) {
        buildRequestService.RequestBuild(request, userId);
        return ResponseEntity.ok("success");
    }

    @PostMapping(value = "/deployAsync", produces = "text/event-stream")
    @AuthorizationFromToken
    public Flux<ServerSentEvent<String>> deployRequestAsync(@RequestBody BuildRequestDto request, String userId) {
        return buildRequestService.RequestBuildAsync(request, userId);
    }

    @GetMapping("/resource/{blueprintUuid}")
    public CfnOutput getMyResource(@PathVariable("blueprintUuid") String blueprintUuid, String userId) {
        return buildRequestService.getMyResources(blueprintUuid, userId);
    }

    @GetMapping(value = "/buildStatus/{instanceId}", produces = "text/event-stream")
    public Flux<ServerSentEvent<String>> getBuildStatus(@PathVariable("instanceId") String instanceId) {
        return buildRequestService.getBuildStatus(instanceId);
    }

	@PostMapping("/deployAsync/v2")
	@AuthorizationFromToken
	public ResponseEntity<String> deployRequestAsyncV2(@RequestBody BuildRequestDto request, String userId) {
		CompletableFuture.runAsync(() -> buildRequestService.RequestBuildAsync2(request, userId));
		return ResponseEntity.ok("Request Delivered");
	}

	@GetMapping("/info")
	@AuthorizationFromToken
	public ResponseEntity<String> instanceInfo(@RequestParam("instanceId") String instanceId, String userId) {

		return ResponseEntity.ok(buildRequestService.getInstanceInfo(instanceId));
	}

}