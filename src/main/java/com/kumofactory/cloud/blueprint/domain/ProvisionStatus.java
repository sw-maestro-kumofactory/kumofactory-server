package com.kumofactory.cloud.blueprint.domain;

public enum ProvisionStatus {
    PENDING, // 아키텍처 다이어그램만 저장
    PROVISIONING, // 아키텍처 배포 중
    SUCCESS, // 배포 성공
    FAIL // 배포 실패
}
