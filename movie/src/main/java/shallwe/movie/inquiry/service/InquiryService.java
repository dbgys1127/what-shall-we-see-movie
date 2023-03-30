package shallwe.movie.inquiry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public Inquiry saveInquiry(String email, InquiryDto.Post inquiryDto) {
        Inquiry inquiry = transferDtoToInquiry(inquiryDto);
        Member member = memberService.is_exist_member(email);
        setMemberRelation(member,inquiry);
        return inquiryRepository.save(inquiry);
    }
    public PagingResponseDto<InquiryDto.Response> getInquiryList(String email, int page, String sort) {
        Page<Inquiry> pageInfo = inquiryRepository.findInquiryByMemberWithPaging(email, PageRequest.of(page, 10, Sort.by(sort).descending()));
        List<Inquiry> inquiries = pageInfo.getContent();
        List<InquiryDto.Response> inquiryRepDtoList = getInquiryRepDtoList(inquiries);
        return new PagingResponseDto<>(inquiryRepDtoList,pageInfo);
    }

    public InquiryDto.Response getInquiry(Long inquiryId) {
        Inquiry inquiry = is_exist_comment(inquiryId);
        return InquiryDto.Response.builder()
                .inquiryId(inquiry.getInquiryId())
                .inquiryTitle(inquiry.getInquiryTitle())
                .inquiryDescription(inquiry.getInquiryDescription())
                .inquiryStatus(inquiry.getInquiryStatus())
                .createdAt(inquiry.getCreatedAt())
                .createdBy(inquiry.getCreatedBy())
                .build();
    }

    public void deleteInquiry(Long inquiryId) {
        inquiryRepository.delete(is_exist_comment(inquiryId));
    }
    public Inquiry is_exist_comment(Long inquiryId) {
        Optional<Inquiry> optionalInquiry =inquiryRepository.findById(inquiryId);
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
