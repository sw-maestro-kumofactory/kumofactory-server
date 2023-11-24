package com.kumofactory.cloud.appDeploy.service;

import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;

public interface BuildRequestService {
    void RequestBuild(BuildRequestDto request, String oauthId);

    CfnOutput getMyResources(String blueprintUuid, String oauthId);


    Flux<ServerSentEvent<String>> RequestBuildAsync(BuildRequestDto request, String oauthId);

	Flux<ServerSentEvent<String>> getBuildStatus(String instanceId);
	String getInstanceInfo(String instanceId);

}
