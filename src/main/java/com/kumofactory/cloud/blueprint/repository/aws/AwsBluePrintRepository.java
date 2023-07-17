package com.kumofactory.cloud.blueprint.repository.aws;

import com.kumofactory.cloud.blueprint.domain.aws.AwsBluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AwsBluePrintRepository extends JpaRepository<AwsBluePrint, Long> {
		AwsBluePrint findAwsBluePrintById(long id);
}
