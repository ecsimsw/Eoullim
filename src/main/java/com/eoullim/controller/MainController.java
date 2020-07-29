package com.eoullim.controller;

import com.eoullim.domain.ChatMessage;
import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.Member;
import com.eoullim.form.ChatRoomForm;
import com.eoullim.form.JoinForm;
import com.eoullim.form.LoginForm;
import com.eoullim.service.ChatRoomService;
import com.eoullim.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class MainController {

    private final MemberService memberService;
    private final ChatRoomService chatRoomService;

    @RequestMapping("/info")
    public String chatTest(Model model){
        return "info";
    }

    @GetMapping("/main")
    public String mainPage(Model model){
        return "test/mainPage";
    }



}
