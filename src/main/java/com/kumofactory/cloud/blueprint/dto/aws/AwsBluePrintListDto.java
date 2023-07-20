package com.kumofactory.cloud.blueprint.dto.aws;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AwsBluePrintListDto {
		private Long id;
		private String name;
		private Date createdAt;
}
