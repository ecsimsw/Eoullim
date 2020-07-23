package com.eoullim.backUp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ChatRoomRepository_backUp {





    /*
    private Map<Long, ChatRoom> chatRoomMap;
    private List<ChatRoom> chatRoomList; // for print chatRooms.

    @PostConstruct
    private void init(){
        chatRoomMap = new LinkedHashMap<>();
        chatRoomList = new LinkedList<>();
    }

    public List<ChatRoom> findAllRoom(){
        return chatRoomList;
    }

    public ChatRoom findRoomById(Long id){
        return chatRoomMap.get(id);
    }

    public ChatRoom save(String name){
        ChatRoom newChatRoom = ChatRoom.create(name);
        chatRoomMap.put(newChatRoom.getRoomId(), newChatRoom);
        chatRoomList.add(newChatRoom);
        return newChatRoom;
    }

    public void delete(Long roomId){
        ChatRoom deleteChatRoom = this.findRoomById(roomId);
        chatRoomList.remove(deleteChatRoom);
        chatRoomMap.remove(roomId);
    }
    */
}
