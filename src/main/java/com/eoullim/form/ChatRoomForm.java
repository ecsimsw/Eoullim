package com.eoullim.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomForm {
    private String writerLoginId;
    private String roomTitle;
    private long roomHashId;
    private String roomDescription;
    private int limitPerson;
}
