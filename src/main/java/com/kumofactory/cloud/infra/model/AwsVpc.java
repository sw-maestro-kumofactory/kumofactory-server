package com.kumofactory.cloud.infra.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class AwsVpc {
    @Id
    @Column(name = "aws_vpc", nullable = false)
    private Long id;

    private String name;
    private String cidr;
    private String memberId;
}
