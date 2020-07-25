package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
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

    // 여기도 org.hibernate.LazyInitializationException: failed to lazily initialize a collection of role:
    // fetch.EAGER를 안넣어주면 다 에러가 나네,,
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Chat> chats = new ArrayList<>();

    public void addChat(Chat chat){
        chats.add(chat);
        chat.setMember(this);
    }

    public void removeChat(Chat chat){
        chats.remove(chat);
    }


    //Transient
    @Transient
    WebSocketSession webSocketSession;
}
