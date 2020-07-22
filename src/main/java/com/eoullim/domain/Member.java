package com.eoullim.domain;

import lombok.Getter;
import lombok.Setter;

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

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Chat> chatRooms = new ArrayList<>();
}
