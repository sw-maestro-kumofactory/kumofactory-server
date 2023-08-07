package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AwsBluePrintListDto {
    private Long id;
    private String uuid;
    private String name;
    private Date createdAt;
    private ProvisionStatus status;
}
