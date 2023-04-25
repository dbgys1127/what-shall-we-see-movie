package shallwe.movie.inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
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
public class InquiryController {

    private final InquiryService inquiryService;

    //============================ 사용자 ==========================

    @GetMapping("/inquiry/add-inquiry-form")
    public String getInquiryAddForm() {
        return "inquiry/addInquiry";
    }

    @NeedMember
    @PostMapping("/inquiry")
    @ResponseBody
    public void postInquiry(Member member, @ModelAttribute @Valid InquiryDto.Post inquiryDto) {
        inquiryService.saveInquiry(member, inquiryDto);
    }

    @NeedEmail
    @GetMapping("/inquiry")
    public String getMyInquiryList(String email,@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList(email,page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/myInquiry";
    }

    @GetMapping("/inquiry/detail")
    public String getMyInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/myInquiryDetail";
    }

    @PostMapping("/inquiry/delete")
    @ResponseBody
    public void deleteInquiry(@RequestParam("inquiryId") Long inquiryId) {
        inquiryService.deleteInquiry(inquiryId);
    }
    //============================관리자 ==========================

    @GetMapping("/admin/inquiry")
    public String getInquiryListForAdmin(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList("@",page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/admin/adminInquiry";
    }

    @PostMapping("/admin/inquiry/answer")
    @ResponseBody
    public void postAnswer(@RequestParam("inquiryId") Long inquiryId,
                      @ModelAttribute @Valid AnswerDto.Post answerDto) {
        inquiryService.saveAnswer(inquiryId,answerDto);
    }

    @PostMapping("/admin/inquiry/answer/status")
    @ResponseBody
    public void postAnswerStatus(@RequestParam("inquiryId") Long inquiryId,
                                 @RequestParam(value = "inquiryStatus", defaultValue = "off") String inquiryStatus) {
        inquiryService.updateAnswerStatus(inquiryId, inquiryStatus);
    }

    @PostMapping("/admin/inquiry/answer/delete")
    @ResponseBody
    public void deleteAnswer(@RequestParam("answerId") Long answerId) {
        inquiryService.deleteAnswer(answerId);
    }

    @GetMapping("/admin/inquiry/detail")
    public String getAdminInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/admin/adminInquiryDetail";
    }
}
