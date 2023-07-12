package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.ComponentPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentPointRepository extends JpaRepository<ComponentPoint, Long> {

}
