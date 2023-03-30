package shallwe.movie.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.querydsl.QuerydslRepository;

public interface InquiryRepository extends JpaRepository<Inquiry,Long>, QuerydslRepository {
}
