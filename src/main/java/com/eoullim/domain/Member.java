package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="member")
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String loginId;
    private String loginPw;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat){
        chat.setMember(this);
        chats.add(chat);
    }

    public void removeChat(Chat chat){
        chats.remove(chat);
    }


    //Transient
    @Transient
    WebSocketSession webSocketSession;
}
