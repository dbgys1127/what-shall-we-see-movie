package shallwe.movie;

import com.amazonaws.services.s3.AmazonS3Client;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import shallwe.movie.member.repository.MemberRepository;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.querydsl.QuerydslRepositoryImpl;
import shallwe.movie.s3.S3UploadService;
import shallwe.movie.security.service.CustomAuthorityUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@TestConfiguration
public class TestConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    MemberRepository memberRepository;

    @Bean
    public JPAQueryFactory jpaQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public QuerydslRepositoryImpl querydslOrderRepository(){
        return new QuerydslRepositoryImpl(jpaQueryFactory());
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository, PasswordEncoderFactories.createDelegatingPasswordEncoder(),new CustomAuthorityUtils(),new S3UploadService(new AmazonS3Client()));
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory("localhost", 6379);
    }
}
