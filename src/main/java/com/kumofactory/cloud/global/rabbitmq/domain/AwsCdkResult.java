package com.kumofactory.cloud.global.rabbitmq.domain;

import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AwsCdkResult {
    private String id;
    private String status;
}
