package com.kumofactory.cloud.global.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

@Getter
@Setter
public class PagingDto implements Serializable {
    private int page = 0;
    private int size = 10;
    private String sort = "id.DESC";

    public static Pageable createPageAble(PagingDto pagingDto) {
        String[] split = pagingDto.getSort().split("\\.");
        if (split[1].equals("DESC")) {
            return PageRequest.of(pagingDto.getPage(), pagingDto.getSize(), Sort.by(split[0]).descending());
        }

        return PageRequest.of(pagingDto.getPage(), pagingDto.getSize(), Sort.by(split[0]).ascending());
    }
}
