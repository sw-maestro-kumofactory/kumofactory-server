package com.kumofactory.cloud.blueprint.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kumofactory.cloud.blueprint.domain.BluePrintScope;
import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.ProvisionStatus;
import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import com.kumofactory.cloud.blueprint.dto.ComponentLineDto;
import com.kumofactory.cloud.blueprint.dto.aws.*;

import com.kumofactory.cloud.blueprint.repository.ComponentLineRepository;
import com.kumofactory.cloud.blueprint.repository.InfraCostRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsAreaRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsBluePrintRepository;
import com.kumofactory.cloud.blueprint.repository.aws.AwsComponentRepository;
import com.kumofactory.cloud.global.rabbitmq.MessageProducer;
import com.kumofactory.cloud.global.rabbitmq.domain.CdkMessagePattern;
import com.kumofactory.cloud.member.MemberRepository;
import com.kumofactory.cloud.member.domain.Member;
import com.kumofactory.cloud.util.aws.s3.AwsS3Helper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.parseBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
@Repository
public class AwsBlueprintServiceImpl implements AwsBlueprintService {

    private final MemberRepository memberRepository;
    private final AwsBluePrintRepository awsBluePrintRepository;
    private final AwsComponentRepository awsComponentRepository;
    private final ComponentLineRepository componentLineRepository;
    private final AwsAreaRepository awsAreaRepository;
    private final InfraCostRepository infraCostRepository;
    private final MessageProducer sender;
    private final AwsS3Helper awsS3Helper;
    private final Logger logger = LoggerFactory.getLogger(AwsBlueprintServiceImpl.class);


    @Override
    public AwsBluePrintDto getAwsBlueprint(String uuid, String userId) {
        AwsBluePrint awsBluePrintById = awsBluePrintRepository.findAwsBluePrintByUuid(uuid);
        if (awsBluePrintById.getMember().getOauthId().equals(userId)) {

            List<AwsArea> awsAreas = awsAreaRepository.findAllByBluePrint(awsBluePrintById);
            List<AwsComponent> awsComponents = awsComponentRepository.findAllByBluePrint(awsBluePrintById);
            List<ComponentLine> componentLines = componentLineRepository.findAllByBluePrint(awsBluePrintById);

            return AwsBluePrintDto.build(awsBluePrintById, awsAreas, awsComponents, componentLines);
        }

        throw new RuntimeException("Not Authorized");
    }

    @Override
    public List<AwsBluePrintListDto> getMyAwsBlueprints(String oauthId) {
        Member member = memberRepository.findMemberByOauthId(oauthId);
        if (member == null) {
            throw new RuntimeException("member is null");
        }

        List<AwsBluePrint> awsBluePrints = awsBluePrintRepository.findAwsBluePrintsByMember(member);
        List<AwsBluePrintListDto> awsBluePrintDtos = new ArrayList<>();
        for (AwsBluePrint awsBluePrint : awsBluePrints) {
            String presignedUrl = awsS3Helper.getPresignedUrl(awsBluePrint.getKeyName());
            AwsBluePrintListDto dto = AwsBluePrintListDto.fromAwsBluePrint(awsBluePrint, presignedUrl);
            awsBluePrintDtos.add(dto);
        }
        return awsBluePrintDtos;
    }

    @Override
    public void store(AwsBluePrintDto awsBluePrintDto, String provision, CdkMessagePattern pattern, String userId) throws IOException {
        this.delete(awsBluePrintDto.getUuid()); // 기존 BluePrint 삭제

        AwsBluePrint savedBlueprint = saveBlueprint(awsBluePrintDto, provision, userId); // BluePrint 저장
        saveComponentLines(savedBlueprint, awsBluePrintDto.getLinks()); // ComponentLine 저장
        saveAwsAreas(savedBlueprint, awsBluePrintDto.getAreas()); // Area 저장

        List<AwsComponent> components = new ArrayList<>();
        List<AwsCdkDto> awsCdkDtos = new ArrayList<>();

        AwsCdkDto id = AwsCdkDto.builder().id(awsBluePrintDto.getUuid()).build();
        awsCdkDtos.add(id);

        // Components 저장
        for (AwsComponentDto component : awsBluePrintDto.getComponents()) {
            AwsComponent awsComponent = AwsComponent.createAwsComponent(component, savedBlueprint);
            AwsCdkDto awsCdkDto = AwsCdkDto.createAwsCdkDto(component);
            components.add(awsComponent);
            awsCdkDtos.add(awsCdkDto);
        }
        awsComponentRepository.saveAll(components);

        if (parseBoolean(provision)) {
            logger.info("send message to cdk server: {}", pattern);
            sender.sendAwsCdkOption(pattern, awsCdkDtos);
        }else {
            logger.info("send message to cdk server: {}", CdkMessagePattern.COST);
            sender.sendAwsCdkOption(CdkMessagePattern.COST, awsCdkDtos);
        }
    }

    @Override
    public boolean delete(String uuid) {
        AwsBluePrint awsBluePrint = awsBluePrintRepository.findAwsBluePrintByUuid(uuid);
        if (awsBluePrint == null) {
            return false;
        }
        awsBluePrintRepository.delete(awsBluePrint);
        return true;
    }

    @Override
    public boolean updateBluePrintScope(BluePrintScope scope, String uuid, String userId) {
        AwsBluePrint awsBluePrintByUuid = awsBluePrintRepository.findAwsBluePrintByUuid(uuid);
        if (awsBluePrintByUuid == null || !awsBluePrintByUuid.getMember().getOauthId().equals(userId)) {
            return false;
        }
        awsBluePrintByUuid.setScope(scope);
        awsBluePrintRepository.save(awsBluePrintByUuid);
        return true;
    }

    @Override
    public ProvisionStatus getProvisionStatus(String uuid, String userId) {
        AwsBluePrint awsBluePrintByUuid = awsBluePrintRepository.findAwsBluePrintByUuid(uuid);
        if (awsBluePrintByUuid == null || !awsBluePrintByUuid.getMember().getOauthId().equals(userId)) {
            return null;
        }
        return awsBluePrintByUuid.getStatus();
    }

    // Blueprint 저장
    private AwsBluePrint saveBlueprint(AwsBluePrintDto awsBluePrintDto, String provision, String userId) throws IOException {
        logger.info("awsBluePrintDto {}", awsBluePrintDto.toString());

        Member member = memberRepository.findMemberByOauthId(userId);
        // Provision 여부 설정
        ProvisionStatus status;
        if (parseBoolean(provision)) {
            status = ProvisionStatus.PROVISIONING;
        } else {
            status = ProvisionStatus.PENDING;
        }

        // thumbnail 저장
        String keyname = awsS3Helper.saveThumbnail(awsBluePrintDto, member);

        // BluePrint 저장
        AwsBluePrint awsBluePrint = new AwsBluePrint();
        awsBluePrint.setUuid(awsBluePrintDto.getUuid());
        awsBluePrint.setName(awsBluePrintDto.getName());
        awsBluePrint.setDescription(awsBluePrintDto.getDescription());
        awsBluePrint.setDownloadCount(0);
        awsBluePrint.setStatus(status);
        awsBluePrint.setMember(member);
        awsBluePrint.setScope(awsBluePrintDto.getScope() == null ? BluePrintScope.PRIVATE : awsBluePrintDto.getScope());
        awsBluePrint.setKeyName(keyname);
        awsBluePrint.setIsTemplate(false);
        return awsBluePrintRepository.save(awsBluePrint);
    }

    // ComponentLine 저장
    private void saveComponentLines(AwsBluePrint blueprint, List<ComponentLineDto> lines) {
        List<ComponentLine> componentLines = new ArrayList<>();
        for (ComponentLineDto link : lines) {
            ComponentLine componentLink = ComponentLine.createComponentLink(link, blueprint);
            componentLines.add(componentLink);
        }
        componentLineRepository.saveAll(componentLines);
    }

    private void saveAwsAreas(AwsBluePrint blueprint, List<AwsAreaDto> areas) {
        List<AwsArea> awsArea = new ArrayList<>();
        for (AwsAreaDto link : areas) {
            AwsArea area = AwsArea.createAwsArea(link, blueprint);
            awsArea.add(area);
        }
        awsAreaRepository.saveAll(awsArea);
    }

    @Override
    public Object getInfraCost(String uuid, String userId) {
        return infraCostRepository.findByUuid(uuid);
    }


}
