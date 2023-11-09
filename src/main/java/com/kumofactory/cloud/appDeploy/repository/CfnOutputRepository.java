package com.kumofactory.cloud.appDeploy.repository;

import com.kumofactory.cloud.appDeploy.domain.CfnOutput;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CfnOutputRepository extends MongoRepository<CfnOutput, String> {
    @Query("{key:'?0'}")
    CfnOutput findByKey(String key);
}
