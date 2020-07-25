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

            return "redirect:/chat/rooms";
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

    @GetMapping("/chat/rooms")
    public String rooms(Model model){
        model.addAttribute("rooms",chatRoomService.getAllChatRooms());
        model.addAttribute("numberOfRooms",chatRoomService.getAllChatRooms().size());
        return "test/chat/rooms";
    }

    @GetMapping("/chat/rooms/new")
    public String make(Model model){
        model.addAttribute("form",new ChatRoomForm());

        return "test/chat/newRoom";
    }

    @PostMapping("/chat/rooms/new")
    public String makeRoom(ChatRoomForm form){
        chatRoomService.createChatRoom(form.getName());
        return "redirect:/chat/rooms";
    }

    @GetMapping("/chat/room/{roomHash}")
    public String enterChatRoom(@PathVariable String roomHash, Model model){
        ChatRoom room = chatRoomService.getChatRoomByHashId(Long.parseLong(roomHash));
        model.addAttribute("room",room);
        return "test/chat/room";
    }


}
