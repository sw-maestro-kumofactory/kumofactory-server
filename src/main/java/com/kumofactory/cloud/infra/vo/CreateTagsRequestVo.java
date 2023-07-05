package com.kumofactory.cloud.infra.vo;

import lombok.Getter;
import software.amazon.awssdk.services.ec2.model.CreateTagsRequest;
import software.amazon.awssdk.services.ec2.model.Tag;

@Getter
public class CreateTagsRequestVo {

  private String resourceId;
  private String tagName;
  private String tagValue;

  public CreateTagsRequestVo(String resourceId, String tagValue) {
    this.resourceId = resourceId;
    this.tagName = "Name";
    this.tagValue = tagValue;
  }

  public CreateTagsRequestVo(String resourceId, String tagName, String tagValue) {
    this.resourceId = resourceId;
    this.tagName = tagName;
    this.tagValue = tagValue;
  }

  public CreateTagsRequest toCreateTagsRequest() {
    Tag tag = Tag.builder()
                 .key(this.tagName)
                 .value(this.tagValue)
                 .build();

    return CreateTagsRequest.builder()
                            .resources(resourceId)
                            .tags(Tag.builder()
                                     .key(tagName)
                                     .value(tagValue)
                                     .build())
                            .build();
  }
}
