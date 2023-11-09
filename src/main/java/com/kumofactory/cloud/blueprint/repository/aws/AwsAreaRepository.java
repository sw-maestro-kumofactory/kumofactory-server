package com.kumofactory.cloud.blueprint.repository.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsArea;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwsAreaRepository extends JpaRepository<AwsArea, Long> {
    List<AwsArea> findAllByBluePrint(AwsBluePrint awsBluePrint);
}
