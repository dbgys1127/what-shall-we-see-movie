package shallwe.movie.querydsl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import shallwe.movie.member.entity.Member;

import java.util.List;

import static shallwe.movie.member.entity.QMember.member;

@RequiredArgsConstructor
public class QuerydslRepositoryImpl implements QuerydslRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Page<Member> findAllMemberWithPaging(String email,Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(member.email.contains(email)
                                .and(member.roles.size().eq(1)))
                .orderBy(queryDslSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(member)
                .where(member.email.contains(email)
                                .and(member.roles.size().eq(1)))
                .fetchCount();
        return new PageImpl<>(content,pageable,total);
    }

    @Override
    public Page<Member> findAllAdminWithPaging(String email, Pageable pageable) {
        List<Member> content = queryFactory
                .selectFrom(member)
                .where(member.email.contains(email)
                        .and(member.roles.size().eq(2)))
                .orderBy(queryDslSort(pageable))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.selectFrom(member)
                .where(member.email.contains(email)
                        .and(member.roles.size().eq(2)))
                .fetchCount();
        return new PageImpl<>(content,pageable,total);
    }

    private OrderSpecifier<?> queryDslSort(Pageable page) {
        if (!page.getSort().isEmpty()) {
            for (Sort.Order order : page.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "memberId":
                        return new OrderSpecifier(direction, member.memberId);
                    case "warningCard":
                        return new OrderSpecifier(direction, member.warningCard);
                    case "memberStatus":
                        return new OrderSpecifier(Order.ASC, member.memberStatus);
                }
            }
        }
        return null;
    }
}
