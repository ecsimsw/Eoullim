package com.eoullim.controller;

import com.eoullim.domain.ChatRoom;
import com.eoullim.form.ChatRoomForm;
import com.eoullim.message.EcreateChatRoomMessage;
import com.eoullim.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;

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

    @PostMapping("/board/{category}/add")
    @ResponseBody
    public EcreateChatRoomMessage makeRoom(ChatRoomForm form, @PathVariable String category){
        EcreateChatRoomMessage resultMessage = chatRoomService.createChatRoom(form, category);
        return resultMessage;
    }

    @GetMapping("/chat/room/{roomHash}")
    public String enterChatRoom(@PathVariable String roomHash, Model model){
        ChatRoom room = chatRoomService.getChatRoomByHashId(Long.parseLong(roomHash));
        model.addAttribute("room",room);
        return "test/chat/room";
    }

}
