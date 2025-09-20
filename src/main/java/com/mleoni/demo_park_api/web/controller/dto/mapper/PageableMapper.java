package com.mleoni.demo_park_api.web.controller.dto.mapper;

import com.mleoni.demo_park_api.web.controller.dto.PageableDTO;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

@NoArgsConstructor
public class PageableMapper {

    public static PageableDTO toDto(Page page) {
        return new ModelMapper().map(page, PageableDTO.class);
    }
}
