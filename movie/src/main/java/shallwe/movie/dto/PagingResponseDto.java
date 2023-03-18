package shallwe.movie.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PagingResponseDto<T> {
    private List<T> data;

    private int startPage;
    private int endPage;
    private int nowPage;
    private boolean prev, next;


    public PagingResponseDto(List<T> data, Page page) {
        this.data = data;
        this.nowPage = page.getNumber()+1;
        this.endPage = (int)(Math.ceil(nowPage/10.0))*10;
        this.startPage = this.endPage-9;

        int realEnd = page.getTotalPages();

        if (realEnd < this.endPage) {
            this.endPage = realEnd;
        }
        this.prev = this.startPage>1;
        this.next = this.endPage < realEnd;
    }
}
