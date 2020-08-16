package com.eoullim.controller;

import com.eoullim.domain.Member;
import com.eoullim.form.JoinForm;
import com.eoullim.form.LoginForm;
import com.eoullim.message.EjoinMessage;
import com.eoullim.message.EloginMessage;
import com.eoullim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

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
    @ResponseBody
    public EloginMessage loginCheck(@ModelAttribute LoginForm form, HttpSession session){
        String loginId = form.getId();
        String loginPw = form.getPw();

        EloginMessage result = memberService.loginCheck(loginId, loginPw);

        if(result==EloginMessage.success) { // login
            session.setAttribute("loginMember", loginId);
        }
        return result;
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
    @ResponseBody
    public EjoinMessage join(@ModelAttribute JoinForm form,  HttpSession session){
        EjoinMessage joinResult = memberService.join(form);

        if(joinResult==EjoinMessage.success) {
            session.setAttribute("loginMember", form.getLoginId());
        }
        return joinResult;
    }
}
