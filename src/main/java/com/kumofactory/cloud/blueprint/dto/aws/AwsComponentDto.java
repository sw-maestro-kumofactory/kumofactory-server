package com.kumofactory.cloud.blueprint.dto.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsComponentType;
import com.kumofactory.cloud.blueprint.dto.ComponentPointDto;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class AwsComponentDto {

    @NotNull
    private String id;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    @NotNull
    private AwsComponentType type;

    private List<ComponentPointDto> points;
}
