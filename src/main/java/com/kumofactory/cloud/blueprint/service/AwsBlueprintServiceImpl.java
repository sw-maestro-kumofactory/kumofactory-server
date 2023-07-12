package com.kumofactory.cloud.blueprint.service;

import com.kumofactory.cloud.blueprint.domain.ComponentPoint;
import com.kumofactory.cloud.blueprint.domain.PointLink;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentPointDto;
import com.kumofactory.cloud.blueprint.dto.PointLinkDto;
import com.kumofactory.cloud.blueprint.dto.aws.AwsBluePrintDto;

import com.kumofactory.cloud.blueprint.dto.aws.AwsComponentDto;
import com.kumofactory.cloud.blueprint.repository.ComponentPointRepository;
import com.kumofactory.cloud.blueprint.repository.PointLinkRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsComponentRepository;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    private final ComponentPointRepository componentPointRepository;
    private final PointLinkRepository pointLinkRepository;
    private final Logger logger = LoggerFactory.getLogger(AwsBlueprintServiceImpl.class);


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
        List<PointLinkDto> links = awsBluePrintDto.getLinks();

        for (AwsComponentDto component : awsBluePrintDto.getComponents()) {
            AwsComponent awsComponent = AwsComponent.createAwsComponent(component, savedBlueprint);

            List<ComponentPoint> componentPoints = new ArrayList<>();
            List<ComponentPointDto> points = component.getPoints();
            if (points.size() != 0) {
                for (ComponentPointDto point : points) {
                    ComponentPoint componentPoint = ComponentPoint.createComponentPoint(point,
                            awsComponent);
                    componentPoints.add(componentPoint);
                }
            }
            awsComponent.setComponentPoint(componentPoints);
            components.add(awsComponent);
        }
        awsComponentRepository.saveAll(components);
        
        // Component 저장
        if (links.size() != 0) {
            // PointLink 저장
            List<PointLink> toSaveLink = new ArrayList<>();
            for (PointLinkDto link : links) {
                PointLink pointLink = PointLink.createPointLink(link, savedBlueprint);
                toSaveLink.add(pointLink);
            }

            pointLinkRepository.saveAll(toSaveLink);
        }
    }
}
