package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.ComponentDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentDotRepository extends JpaRepository<ComponentDot, Long> {
}
