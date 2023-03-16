package shallwe.movie.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import shallwe.movie.member.entity.Member;

import java.util.List;

import static shallwe.movie.member.entity.QMember.member;

@RequiredArgsConstructor
public class QuerydslRepositoryImpl implements QuerydslRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findMemberBySearch(String email) {
        return queryFactory
                .selectFrom(member)
                .where(member.email.contains(email)
                        .and(member.roles.size().eq(1)))
                .fetch();
    }
}
