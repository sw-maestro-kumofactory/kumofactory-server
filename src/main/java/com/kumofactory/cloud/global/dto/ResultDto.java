package com.kumofactory.cloud.global.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResultDto {
    boolean result;
    String message;
}
