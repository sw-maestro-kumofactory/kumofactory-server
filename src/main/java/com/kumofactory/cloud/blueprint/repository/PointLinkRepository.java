package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.PointLink;
import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface PointLinkRepository extends JpaRepository<PointLink, Long> {
		List<PointLink> findAllByBluePrint(AwsBluePrint bluePrint);
}
