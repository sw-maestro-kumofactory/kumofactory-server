package com.kumofactory.cloud.appDeploy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("logs")
@Getter
@Setter
@NoArgsConstructor
public class BuildLog {
    @Id
    private String _id; // instanceId
    private int status;
    private String repository;
    private String branch;
    private Object logs;
}