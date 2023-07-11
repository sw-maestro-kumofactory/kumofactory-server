package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.BluePrint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface BluePrintRepository extends JpaRepository<BluePrint, Long> {

}
