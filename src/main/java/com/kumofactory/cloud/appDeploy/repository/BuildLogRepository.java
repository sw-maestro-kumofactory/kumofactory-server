package com.kumofactory.cloud.appDeploy.repository;

import com.kumofactory.cloud.appDeploy.domain.BuildLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface BuildLogRepository extends MongoRepository<BuildLog, String> {
    @Query("{_id:'?0'}")
    Optional<BuildLog> findByInstanceId(String instanceId);
}