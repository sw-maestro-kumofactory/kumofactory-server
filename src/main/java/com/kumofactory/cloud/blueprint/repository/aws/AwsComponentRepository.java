package com.kumofactory.cloud.blueprint.repository.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import com.kumofactory.cloud.blueprint.domain.aws.AwsComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AwsComponentRepository extends JpaRepository<AwsComponent, Long> {
    List<AwsComponent> findAllByBluePrint(AwsBluePrint awsBluePrint);
}
