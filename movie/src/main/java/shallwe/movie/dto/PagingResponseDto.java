package shallwe.movie.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponseDto<T> implements Serializable {
    private List<T> data;

    private int startPage;
    private int endPage;
    private int nowPage;
    private boolean prev, next;
    private String sort;
    private String target;
    private String genre;

    public PagingResponseDto(List<T> data, Page page) {
        getPageInfo(data, page);
    }

    public PagingResponseDto(List<T> data, Page page,String target, String genre) {
        getPageInfo(data, page);
        this.target = target;
        this.genre = genre;
    }

    private void getPageInfo(List<T> data, Page page) {
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
        String originStr = page.getSort().toString();
        this.sort = originStr.substring(0,originStr.indexOf(":"));
    }
}
