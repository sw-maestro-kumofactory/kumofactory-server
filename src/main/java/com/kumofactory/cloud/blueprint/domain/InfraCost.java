package com.kumofactory.cloud.blueprint.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("infracosts")
@Getter
@Setter
@NoArgsConstructor
public class InfraCost {
    @Id
    private String _id; // instanceId
    private Object result;
}