package shallwe.movie.querydsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shallwe.movie.member.entity.Member;

import java.util.List;

public interface QuerydslRepository {
    List<Member> findMemberBySearch(String email,Pageable pageable);

    Page<Member> findAllMemberWithPaging(String email,Pageable pageable);
}
