package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.ComponentLine;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentLineRepository extends JpaRepository<ComponentLine, Long> {
    List<ComponentLine> findAllByBluePrint(AwsBluePrint bluePrint);
}
