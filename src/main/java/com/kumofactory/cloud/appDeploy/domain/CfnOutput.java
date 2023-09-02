package com.kumofactory.cloud.appDeploy.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("cfnoutputs")
@Getter
@NoArgsConstructor
public class CfnOutput {
	@Id
	private String id;
	private String key;
	private Object result;

}
