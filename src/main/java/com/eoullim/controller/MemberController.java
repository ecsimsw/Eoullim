package com.eoullim.controller;

import com.eoullim.domain.Member;
import com.eoullim.form.JoinForm;
import com.eoullim.form.LoginForm;
import com.eoullim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("form",new LoginForm());
        return"test/loginPage";
    }

    @PostMapping("/login")
    public String loginCheck(@ModelAttribute LoginForm form, HttpServletRequest request){
        String id = form.getId();
        String pw = form.getPw();

        Member result = memberService.loginCheck(id, pw);

        if(result!=null) { // login
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", result);
            return "redirect:/main";
        }
        else {return "test/ERROR";}  // 없는 id 또는 pw 불일치
    }

    @GetMapping("/logout")
    public String logout(Model model, HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }

    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("form",new JoinForm());
        return"test/joinPage";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute JoinForm form, HttpServletRequest request){
        Member result = memberService.join(form);

        if(result!=null) { // join and login
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", result);
            return "redirect:/chat/rooms";
        }
        else {return "redirect:/ERROR";} // 이미 존재하는 id
    }
}
