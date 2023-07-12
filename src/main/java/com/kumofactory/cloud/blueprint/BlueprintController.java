package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
public class BlueprintController {

    private final AwsBlueprintService awsBlueprintService;

    @PostMapping("/aws")
    public String createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto) {
        awsBlueprintService.store(awsBluePrintDto);
        return "hello-world";
    }
}
