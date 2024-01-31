package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class PageResponse {

    private List<HeroDTO> result;

    private Long totalPages;

    private Long totalElements;

    private Long currentPage;

}
