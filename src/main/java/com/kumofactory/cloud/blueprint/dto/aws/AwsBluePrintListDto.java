package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Schema(name = "AwsBluePrintListDto", description = "AwsBluePrintListDto")
public class AwsBluePrintListDto {

    private Long id;
    private String uuid;
    private String name;
    private String description;
    private Long downloadCount;
    private BluePrintScope scope;
    private Date createdAt;
    private String presignedUrl;
    private ProvisionStatus status;
}
