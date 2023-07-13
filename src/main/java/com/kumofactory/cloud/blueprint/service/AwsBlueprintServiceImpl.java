package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

import com.kumofactory.cloud.blueprint.dto.aws.AwsComponentDto;
import com.kumofactory.cloud.blueprint.repository.ComponentDotRepository;
import com.kumofactory.cloud.blueprint.repository.ComponentLineRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsComponentRepository;
import com.kumofactory.cloud.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Repository
public class AwsBlueprintServiceImpl implements AwsBlueprintService {

    private final MemberRepository memberRepository;
    private final AwsBluePrintRepository awsBluePrintRepository;
    private final AwsComponentRepository awsComponentRepository;
    private final ComponentLineRepository componentLineRepository;
    private final ComponentDotRepository componentDotRepository;
    private final Logger logger = LoggerFactory.getLogger(AwsBlueprintServiceImpl.class);


    @Override
    public AwsBluePrintDto getAwsBlueprint() {
        AwsBluePrint awsBluePrintById = awsBluePrintRepository.findAwsBluePrintById(1L);
        logger.info("awsBluePrintById: {}", awsBluePrintById);
        if (awsBluePrintById == null) {
            throw new RuntimeException("awsBluePrintById is null");
        }

        List<AwsComponent> awsComponents = awsComponentRepository.findAllByBluePrint(awsBluePrintById);
        List<ComponentLine> componentLines = componentLineRepository.findAllByBluePrint(awsBluePrintById);
        AwsBluePrintDto awsBluePrintDto = new AwsBluePrintDto();
        awsBluePrintDto.setName(awsBluePrintById.getName());
        awsBluePrintDto.setComponents(AwsBluePrintDto.awsComponentDtosMapper(awsComponents));
        awsBluePrintDto.setLinks(AwsBluePrintDto.componentLinkDtoListMapper(componentLines));
        return awsBluePrintDto;
    }

    @Override
    public List<AwsBluePrintDto> getMyAwsBlueprints() {
        return null;
    }

    @Override
    public void store(AwsBluePrintDto awsBluePrintDto) {
//        Member member = memberRepository.findMemberById(1L);

        // BluePrint 저장
        AwsBluePrint awsBluePrint = new AwsBluePrint();
        awsBluePrint.setName(awsBluePrintDto.getName());
//        awsBluePrint.setMember(member);
        AwsBluePrint savedBlueprint = awsBluePrintRepository.save(awsBluePrint);
        logger.info("savedBlueprint: {}", savedBlueprint);

        List<AwsComponent> components = new ArrayList<>();
        List<ComponentLineDto> links = awsBluePrintDto.getLinks();

        // Components 저장
        for (AwsComponentDto component : awsBluePrintDto.getComponents()) {
            AwsComponent awsComponent = AwsComponent.createAwsComponent(component, savedBlueprint);
            components.add(awsComponent);
        }
        awsComponentRepository.saveAll(components);

        // Lines 저장
        List<ComponentLine> componentLines = new ArrayList<>();
        for (ComponentLineDto link : links) {
            ComponentLine componentLink = ComponentLine.createComponentLink(link, savedBlueprint);
            componentLines.add(componentLink);
        }
        componentLineRepository.saveAll(componentLines);
    }
}
