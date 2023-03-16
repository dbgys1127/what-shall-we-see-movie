package shallwe.movie.querydsl;

import shallwe.movie.member.entity.Member;

import java.util.List;

public interface QuerydslRepository {
    List<Member> findMemberBySearch(String email);
}
