package shallwe.movie.inquiry.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    String getInquiryAddForm() {
        return "inquiry/addInquiry";
    }

    @NeedMember
    @PostMapping("/inquiry")
    String postInquiry(Member member, @ModelAttribute @Valid InquiryDto.Post inquiryDto) {
        inquiryService.saveInquiry(member, inquiryDto);
        return "redirect:/inquiry";
    }

    @NeedEmail
    @GetMapping("/inquiry")
    String getMyInquiryList(String email,@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList(email,page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/myInquiry";
    }

    @GetMapping("/inquiry/detail")
    String getMyInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/myInquiryDetail";
    }

    @PostMapping("/inquiry/delete")
    String deleteInquiry(@RequestParam("inquiryId") Long inquiryId) {
        inquiryService.deleteInquiry(inquiryId);
        return "redirect:/inquiry";
    }
    //============================관리자 ==========================

    @GetMapping("/admin/inquiry")
    String getInquiryListForAdmin(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
                        Model model) {
        PagingResponseDto<InquiryDto.Response> pageData=inquiryService.getInquiryList("@",page-1,sort);
        model.addAttribute("pageData", pageData);
        return "inquiry/adminInquiry";
    }

    @PostMapping("/admin/inquiry/answer")
    String postAnswer(@RequestParam("inquiryId") Long inquiryId,
                      @RequestParam(value = "inquiryStatus",defaultValue = "off") String inquiryStatus,
                      @ModelAttribute @Valid AnswerDto.Post answerDto,
                      RedirectAttributes redirectAttributes) {
        inquiryService.saveAnswer(inquiryId, inquiryStatus, answerDto);
        redirectAttributes.addAttribute("inquiryId", inquiryId);
        return "redirect:/admin/inquiry/detail";
    }

    @PostMapping("/admin/inquiry/answer/delete")
    String deleteAnswer(@RequestParam("answerId") Long answerId,
                        @RequestParam("inquiryId") Long inquiryId,
                        RedirectAttributes redirectAttributes) {
        inquiryService.deleteAnswer(answerId);
        redirectAttributes.addAttribute("inquiryId", inquiryId);
        return "redirect:/admin/inquiry/detail";
    }

    @GetMapping("/admin/inquiry/detail")
    String getAdminInquiry(@RequestParam("inquiryId") Long inquiryId, Model model) {
        InquiryDto.Response inquiryRepDto = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiryRepDto);
        return "inquiry/adminInquiryDetail";
    }


}
