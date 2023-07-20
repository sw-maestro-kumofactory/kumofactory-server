package com.kumofactory.cloud.auth.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ResultDto {
    private Boolean result;
    private String message;
}
