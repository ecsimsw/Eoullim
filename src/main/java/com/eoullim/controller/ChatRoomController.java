package com.eoullim.controller;

import com.eoullim.domain.ChatRoom;
import com.eoullim.domain.ChatRoomForm;
import com.eoullim.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/rooms")
    public String rooms(Model model){
        model.addAttribute("rooms",chatRoomService.getAllChatRooms());
        model.addAttribute("numberOfRooms",chatRoomService.getAllChatRooms().size());
        return "rooms";
    }

    //for jsp Test
    @GetMapping("/chat/roomsJSP")
    public String roomsTEST(Model model){
        model.addAttribute("rooms",chatRoomService.getAllChatRooms());
        model.addAttribute("numberOfRooms",chatRoomService.getAllChatRooms().size());
        return "rooms.jsp";
    }

    @GetMapping("/chat/room/{id}")
    public String room(@PathVariable String id, Model model){
        ChatRoom room = chatRoomService.getChatRoomById(Long.parseLong(id));
        model.addAttribute("room",room);
        return "room";
    }

    @GetMapping("/chat/rooms/new")
    public String make(Model model){
        model.addAttribute("form",new ChatRoomForm());
        return "newRoom";
    }

    @PostMapping("/chat/rooms/new")
    public String makeRoom(ChatRoomForm form){
        chatRoomService.createChatRoom(form.getName());
        return "redirect:/chat/rooms";
    }
}
