package shallwe.movie.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PagingResponseDto<T> {
    private List<T> data;

    private PageInfo pageInfo;

    public PagingResponseDto(List<T> data, Page page) {
        this.data = data;
        this.pageInfo = new PageInfo(page.getNumber()+1, pageInfo.getSize(), page.getTotalElements(), pageInfo.getTotalPages());
    }
}
