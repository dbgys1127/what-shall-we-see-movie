package shallwe.movie.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> data;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PagingResponseDto(List<T> data, Page page) {
        this.data = data;
        this.page = page.getNumber()+1;
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
