package shallwe.movie.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.answer.service.AnswerService;
import shallwe.movie.comment.entity.Comment;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.inquiry.dto.InquiryDto;
import shallwe.movie.inquiry.entity.Inquiry;
import shallwe.movie.inquiry.repository.InquiryRepository;
import shallwe.movie.member.entity.Member;
import shallwe.movie.member.service.MemberService;
import shallwe.movie.wantmovie.entity.WantMovie;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InquiryService {
    private final InquiryRepository inquiryRepository;
    private final MemberService memberService;
    private final AnswerService answerService;

    /**
     * 문의를 등록하면 문의 목록 내용이 변경되어야 하므로, 문의 목록을 조회할때 저장한 캐시를 삭제한다.
     */
    @CacheEvict(value = "allInquiry",allEntries = true,cacheManager ="contentCacheManager")
    public Inquiry saveInquiry(Member member, InquiryDto.Post inquiryDto) {
        log.info("문의 등록 -> 등록 회원 : {}",member.getEmail());
        Inquiry inquiry = transferDtoToInquiry(inquiryDto);
        setMemberRelation(member,inquiry);
        return inquiryRepository.save(inquiry);
    }
    /**
     * 문의를 수정하면 문의 목록 내용이 변경되어야 하므로, 문의 목록을 조회할때 저장한 캐시를 삭제한다.
     */
    @CacheEvict(value = "allInquiry",allEntries = true,cacheManager ="contentCacheManager")
    public void patchInquiry(Long inquiryId, InquiryDto.Patch inquiryDto) {
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        inquiry.setInquiryTitle(inquiryDto.getInquiryTitle());
        inquiry.setInquiryDescription(inquiryDto.getInquiryDescription());
    }

    /**
     * 문의 조회를 하게 되면 일정 시간 redis에 응답값을 저장해 둔다.
     * @param email : 나의 목록 조회일때는 조회한 사용자의 이메일, 관리자가 조회할때는 모든 회원의 문의를 조회하기 위해 '@'가 전달됨
     * @param sort : 정렬 기준은 생성일, 문의 처리 여부로 정렬된다.
     * @return
     */
    @Cacheable(value = "allInquiry",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<InquiryDto.Response> getInquiryList(String email, int page, String sort) {
        log.info("나의 문의 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}, 조회 정렬 기준 : {}",email,page,sort);
        Page<Inquiry> pageInfo = inquiryRepository.findInquiryByMemberWithPaging(email, PageRequest.of(page, 10, Sort.by(sort).descending()));
        List<Inquiry> inquiries = pageInfo.getContent();
        List<InquiryDto.Response> inquiryRepDtoList = getInquiryRepDtoList(inquiries);
        return new PagingResponseDto<>(inquiryRepDtoList,pageInfo);
    }

    /**
     * 문의 상세 내용 조회
     */
    public InquiryDto.Response getInquiry(Long inquiryId) {
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        return InquiryDto.Response.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryDescription(inquiry.getInquiryDescription())
                .inquiryStatus(inquiry.getInquiryStatus())
                .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .createdBy(inquiry.getCreatedBy())
                .answers(InquiryDto.getAnswerResponseDtoList(inquiry.getAnswers()))
                .build();
    }

    /**
     * 문의 삭제
     * 문의를 삭제하면 문의 목록 내용이 변경되어야 하므로, 문의 목록을 조회할때 저장한 캐시를 삭제한다.
     */
    @Caching(evict = {
            @CacheEvict(value = "allInquiry",allEntries = true,cacheManager = "contentCacheManager")
    })
    public void deleteInquiry(Long inquiryId) {
        log.info("문의 삭제 시도 -> 삭제 대상 문의 : {}",inquiryId);
        inquiryRepository.delete(is_exist_inquiry(inquiryId));
        log.info("문의 삭제 완료 -> 삭제 완료 문의 : {}",inquiryId);
    }

    /**
     * 답변 등록
     */
    public void saveAnswer(Long inquiryId, AnswerDto.Post answerDto) {
        log.info("답변 등록 시도 -> 답변 대상 문의 : {}",inquiryId);
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        answerService.saveAnswer(inquiry, answerDto);
    }

    /**
     * 답변 수정
     */
    public void patchAnswer(Long answerId, AnswerDto.Patch answerDto) {
        answerService.patchAnswer(answerId, answerDto);
    }

    /**
     * 문의 상태 수정
     * @param inquiryStatus : on 이면, inquiryStatus를 처리로 변경, off 이면, inquiryStatus를 대기로 변경
     * 문의 상태가 변경되면 문의 목록 내용이 변경되어야 하므로, 문의 목록을 조회할때 저장한 캐시를 삭제한다.
     */
    @Caching(evict = {
            @CacheEvict(value = "allInquiry",allEntries = true,cacheManager = "contentCacheManager")
    })
    public void updateAnswerStatus(Long inquiryId, String inquiryStatus) {
        log.info("답변 상태 변경 시도 -> 변경 대상 문의 : {}",inquiryId);
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        if (inquiryStatus.equals("on")) {
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.처리);
            log.info("답변 상태 처리 등록 -> 대상 문의 : {}, 처리",inquiryId);
        } else {
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.대기);
            log.info("답변 상태 대기 등록 -> 대상 문의 : {}, 대기",inquiryId);
        }
    }

    /**
     * 답변 삭제
     */
    public void deleteAnswer(Long inquiryId) {
        log.info("답변 삭제 시도");
        answerService.deleteAnswer(inquiryId);
        log.info("답변 삭제 완료");
    }

    /**
     * 문의가 있는지 파악 및 있을 시 객체 반환용
     */
    public Inquiry is_exist_inquiry(Long inquiryId) {
        log.info("문의 DB 조회 시도 -> 조회 대상 문의 : {}",inquiryId);
        Optional<Inquiry> optionalInquiry =inquiryRepository.findById(inquiryId);
        if (optionalInquiry.isEmpty()) {
            log.error("문의 DB 조회 실패 -> 조회 실패 문의 : {}",inquiryId);
        }
        Inquiry findInquiry = optionalInquiry.orElseThrow(() -> new RuntimeException());
        return findInquiry;
    }

    /**
     * Dto -> inquiry 객체 변환
     */
    private static Inquiry transferDtoToInquiry(InquiryDto.Post inquiryDto) {
        return Inquiry.builder()
                .inquiryTitle(inquiryDto.getInquiryTitle())
                .inquiryDescription(inquiryDto.getInquiryDescription())
                .build();
    }

    /**
     * 문의를 리스트 형태로 반환하기 위한 메서드
     * @param inquiries : 회원이나, 관리자의 문의 목록 요청시 페이징 설정에 따라 Inquiry 객체가 리스트로 넘어옴
     */
    private static List<InquiryDto.Response> getInquiryRepDtoList(List<Inquiry> inquiries) {
        List<InquiryDto.Response> inquiryRepDtoList = new ArrayList<>();
        for (Inquiry inquiry : inquiries) {
            InquiryDto.Response inquiryRepDto = InquiryDto.Response.builder()
                    .inquiryId(inquiry.getInquiryId())
                    .inquiryTitle(inquiry.getInquiryTitle())
                    .inquiryDescription(inquiry.getInquiryDescription())
                    .inquiryStatus(inquiry.getInquiryStatus())
                    .createdBy(inquiry.getCreatedBy())
                    .createdAt(inquiry.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                    .build();
            inquiryRepDtoList.add(inquiryRepDto);
        }
        return inquiryRepDtoList;
    }

    /**
     * 멤버-문의 객체간 연관관계 편의 메서드
     * @param member
     * @param inquiry
     */
    public void setMemberRelation(Member member, Inquiry inquiry) {
        inquiry.setMember(member);
        member.getInquiries().add(inquiry);
    }
}
