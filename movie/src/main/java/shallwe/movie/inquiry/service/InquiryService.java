package shallwe.movie.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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

    @CacheEvict(value = "allInquiry",allEntries = true,cacheManager ="contentCacheManager")
    public Inquiry saveInquiry(Member member, InquiryDto.Post inquiryDto) {
        log.info("문의 등록 -> 등록 회원 : {}",member.getEmail());
        Inquiry inquiry = transferDtoToInquiry(inquiryDto);
        setMemberRelation(member,inquiry);
        return inquiryRepository.save(inquiry);
    }
    @Cacheable(value = "allInquiry",key = "#email.concat('-').concat(#page).concat('-').concat(#sort)",cacheManager = "contentCacheManager",unless = "#result == null")
    public PagingResponseDto<InquiryDto.Response> getInquiryList(String email, int page, String sort) {
        log.info("나의 문의 목록 조회 -> 조회 회원 : {}, 조회 페이지 : {}, 조회 정렬 기준 : {}",email,page,sort);
        Page<Inquiry> pageInfo = inquiryRepository.findInquiryByMemberWithPaging(email, PageRequest.of(page, 10, Sort.by(sort).descending()));
        List<Inquiry> inquiries = pageInfo.getContent();
        List<InquiryDto.Response> inquiryRepDtoList = getInquiryRepDtoList(inquiries);
        return new PagingResponseDto<>(inquiryRepDtoList,pageInfo);
    }

    public InquiryDto.Response getInquiry(Long inquiryId) {
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        return InquiryDto.Response.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryDescription(inquiry.getInquiryDescription())
                .inquiryStatus(inquiry.getInquiryStatus())
                .createdAt(inquiry.getCreatedAt())
                .createdBy(inquiry.getCreatedBy())
                .answers(InquiryDto.getAnswerResponseDtoList(inquiry.getAnswers()))
                .build();
    }

    public void deleteInquiry(Long inquiryId) {
        log.info("문의 삭제 시도 -> 삭제 대상 문의 : {}",inquiryId);
        inquiryRepository.delete(is_exist_inquiry(inquiryId));
        log.info("문의 삭제 완료 -> 삭제 완료 문의 : {}",inquiryId);
    }
    public void saveAnswer(Long inquiryId, String inquiryStatus, AnswerDto.Post answerDto) {
        log.info("답변 등록 시도 -> 답변 대상 문의 : {}",inquiryId);
        Inquiry inquiry = is_exist_inquiry(inquiryId);
        answerService.saveAnswer(inquiry, answerDto);
        if (inquiryStatus.equals("on")) {
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.처리);
            log.info("답변 상태 처리 등록 -> 대상 문의 : {}, 처리",inquiryId);
        } else {
            inquiry.setInquiryStatus(Inquiry.InquiryStatus.대기);
            log.info("답변 상태 대기 등록 -> 대상 문의 : {}, 대기",inquiryId);
        }
    }
    public void deleteAnswer(Long inquiryId) {
        log.info("답변 삭제 시도");
        answerService.deleteAnswer(inquiryId);
        log.info("답변 삭제 완료");
    }
    public Inquiry is_exist_inquiry(Long inquiryId) {
        log.info("문의 DB 조회 시도 -> 조회 대상 문의 : {}",inquiryId);
        Optional<Inquiry> optionalInquiry =inquiryRepository.findById(inquiryId);
        if (optionalInquiry.isEmpty()) {
            log.error("문의 DB 조회 실패 -> 조회 실패 문의 : {}",inquiryId);
        }
        Inquiry findInquiry = optionalInquiry.orElseThrow(() -> new RuntimeException());
        return findInquiry;
    }
    private static Inquiry transferDtoToInquiry(InquiryDto.Post inquiryDto) {
        return Inquiry.builder()
                .inquiryTitle(inquiryDto.getInquiryTitle())
                .inquiryDescription(inquiryDto.getInquiryDescription())
                .build();
    }
    private static List<InquiryDto.Response> getInquiryRepDtoList(List<Inquiry> inquiries) {
        List<InquiryDto.Response> inquiryRepDtoList = new ArrayList<>();
        for (Inquiry inquiry : inquiries) {
            InquiryDto.Response inquiryRepDto = InquiryDto.Response.builder()
                    .inquiryId(inquiry.getInquiryId())
                    .inquiryTitle(inquiry.getInquiryTitle())
                    .inquiryDescription(inquiry.getInquiryDescription())
                    .inquiryStatus(inquiry.getInquiryStatus())
                    .createdBy(inquiry.getCreatedBy())
                    .createdAt(inquiry.getCreatedAt())
                    .build();
            inquiryRepDtoList.add(inquiryRepDto);
        }
        return inquiryRepDtoList;
    }
    public void setMemberRelation(Member member, Inquiry inquiry) {
        inquiry.setMember(member);
        member.getInquiries().add(inquiry);
    }



}
