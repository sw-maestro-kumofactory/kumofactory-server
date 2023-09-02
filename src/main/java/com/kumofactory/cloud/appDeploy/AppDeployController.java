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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

	@GetMapping("/resource/{blueprintUuid}")
	public CfnOutput getMyResource(@PathVariable("blueprintUuid") String blueprintUuid, String userId) {
		return buildRequestService.getMyResources(blueprintUuid, userId);
	}

}