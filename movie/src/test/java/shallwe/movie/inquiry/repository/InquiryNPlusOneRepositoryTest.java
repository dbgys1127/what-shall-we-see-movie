package shallwe.movie.inquiry.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("testDB")
@Slf4j
public class InquiryNPlusOneRepositoryTest {
    @Autowired
    InquiryRepository inquiryRepository;
}
