package com.kumofactory.cloud.appDeploy.service;

import com.kumofactory.cloud.appDeploy.dto.BuildRequestDto;

public interface BuildRequestService {
    public void RequestBuild(BuildRequestDto request, String oauthId);
}
