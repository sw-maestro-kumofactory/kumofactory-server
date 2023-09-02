package com.kumofactory.cloud.appDeploy.service;

import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;

public interface BuildRequestService {
	void RequestBuild(BuildRequestDto request, String oauthId);

	CfnOutput getMyResources(String blueprintUuid, String oauthId);
}
