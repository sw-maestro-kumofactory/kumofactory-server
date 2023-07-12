package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.PointLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface PointLinkRepository extends JpaRepository<PointLink, Long> {

}
