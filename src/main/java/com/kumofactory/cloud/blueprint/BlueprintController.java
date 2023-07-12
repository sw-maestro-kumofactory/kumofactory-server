package com.kumofactory.cloud.blueprint;

import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;
import com.kumofactory.cloud.blueprint.service.AwsBlueprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/blueprint")
public class BlueprintController {

    private final AwsBlueprintService awsBlueprintService;

    @GetMapping("/aws")
    public AwsBluePrintDto getAwsBlueprint() {
        return "hello-world";
    }

    @PostMapping("/aws")
    public String createAwsBlueprint(@RequestBody AwsBluePrintDto awsBluePrintDto) {
        awsBlueprintService.store(awsBluePrintDto);
        return "hello-world";
    }
}
