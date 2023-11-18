package com.kumofactory.cloud.blueprint.repository;

import com.kumofactory.cloud.blueprint.domain.InfraCost;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface InfraCostRepository extends MongoRepository<InfraCost, String> {
    @Query("{_id:'?0'}")
    Optional<InfraCost> findByUuid(String uuid);

}