package shallwe.movie.inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shallwe.movie.answer.dto.AnswerDto;
import shallwe.movie.aop.NeedEmail;
import shallwe.movie.aop.NeedMember;
import shallwe.movie.dto.PagingResponseDto;
import shallwe.movie.inquiry.dto.InquiryDto;
import shallwe.movie.inquiry.service.InquiryService;
import shallwe.movie.member.entity.Member;

import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@Transactional
public class InquiryController {

    private final InquiryService inquiryService;

    //============================ 사용자 요청 처리 API ==========================

    /**
     * 문의 등록
     * @param member : 작성자 객체
     * @param inquiryDto : 등록 문의 내용
     */
    @NeedMember
    @PostMapping("/inquiry")
    @ResponseBody
    public void postInquiry(Member member, @ModelAttribute @Valid InquiryDto.Post inquiryDto) {
        inquiryService.saveInquiry(member, inquiryDto);
    }

    /**
     * 문의 수정
     * @param inquiryId : 수정 대상 문의
     * @param inquiryDto : 수정 내용
     */
    @PostMapping("/inquiry/patch")
    @ResponseBody
    public void patchInquiry(@RequestParam("inquiryId") Long inquiryId, @ModelAttribute InquiryDto.Patch inquiryDto) {
        inquiryService.patchInquiry(inquiryId, inquiryDto);
    }

    /**
     * 나의 문의내역 조회
     * @param email : 조회를 시도하는 회원
     * @param page : 조회하고 싶은 페이지
     * @param sort : 정렬 기준으로 문의 생성일, 처리여부 기준으로 정렬될 수 있다.
     */
    @NeedEmail
    @GetMapping("/inquiry")
    public String getMyInquiryList(String email,@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList(email,page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/myInquiry";
    }

    /**
     * 문의 상세페이지 조회
     * 문의내역 조회는 답변이 보이지 않으나, 상세페이지에서 답변목록을 볼 수 있다.
     * @param inquiryId : 조회 대상 문의 기본키
     */
    @GetMapping("/inquiry/detail")
    public String getMyInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/myInquiryDetail";
    }

    /**
     * 문의 삭제
     * @param inquiryId : 삭제 대상 문의 기본키
     */
    @PostMapping("/inquiry/delete")
    @ResponseBody
    public void deleteInquiry(@RequestParam("inquiryId") Long inquiryId) {
        inquiryService.deleteInquiry(inquiryId);
    }


    //============================관리자 요청 처리 API ==========================

    /**
     * 관리자용 문의 목록 조회
     */
    @GetMapping("/admin/inquiry")
    public String getInquiryListForAdmin(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList("@",page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/admin/adminInquiry";
    }

    /**
     * 답변 등록
     * @param inquiryId : 답변이 등록될 문의
     */
    @PostMapping("/admin/inquiry/answer")
    @ResponseBody
    public void postAnswer(@RequestParam("inquiryId") Long inquiryId,
                      @ModelAttribute @Valid AnswerDto.Post answerDto) {
        inquiryService.saveAnswer(inquiryId,answerDto);
    }

    /**
     * 답변 수정
     * @param answerId : 수정 대상 답변
     */
    @PostMapping("/admin/inquiry/answer/patch")
    @ResponseBody
    public void patchAnswer(@RequestParam("answerId") Long answerId, @ModelAttribute AnswerDto.Patch answerDto){
        inquiryService.patchAnswer(answerId, answerDto);
    }

    /**
     * 문의에 대한 처리여부를 사용자에게 피드백 하기 위한 용도
     * @param inquiryId : 처리여부 변경 대상 문의
     * @param inquiryStatus : 처리 여부
     */
    @PostMapping("/admin/inquiry/answer/status")
    @ResponseBody
    public void postAnswerStatus(@RequestParam("inquiryId") Long inquiryId,
                                 @RequestParam(value = "inquiryStatus", defaultValue = "off") String inquiryStatus) {
        inquiryService.updateAnswerStatus(inquiryId, inquiryStatus);
    }

    /**
     * 답변 삭제
     */
    @PostMapping("/admin/inquiry/answer/delete")
    @ResponseBody
    public void deleteAnswer(@RequestParam("answerId") Long answerId) {
        inquiryService.deleteAnswer(answerId);
    }

    /**
     * 답변을 등록하기 위해서 문의 상세페이지에 가야하고, 회원이 보는 상세페이지와 다른 기능을 함
     */
    @GetMapping("/admin/inquiry/detail")
    public String getAdminInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/admin/adminInquiryDetail";
    }
}
