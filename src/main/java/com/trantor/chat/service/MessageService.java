package com.trantor.chat.service;

import com.trantor.chat.model.Message;
import com.trantor.chat.model.Status;
import com.trantor.chat.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repository;

    @Autowired
    private RoomService chatRoomService;

    @Autowired
    private MongoOperations mongoOperations;

    public Message save(Message chatMessage) {
        chatMessage.setStatus(Status.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return repository.countBySenderIdAndRecipientIdAndStatus(
                senderId, recipientId, Status.RECEIVED);
    }

    public List<Message> findChatMessages(String senderId, String recipientId) {
        Optional<String> chatId = chatRoomService.getChatId(senderId, recipientId, false);

        List messages =
                chatId.map(cId -> repository.findByChatId(cId)).orElse(new ArrayList<>());

        if(messages.size() > 0) {
            updateStatuses(senderId, recipientId, Status.DELIVERED);
        }

        return messages;
    }

    public Message findById(String id) {
        return repository
                .findById(id)
                .map(chatMessage -> {
                    chatMessage.setStatus(Status.DELIVERED);
                    return repository.save(chatMessage);
                })
                .orElseThrow(() ->
                        new NullPointerException("can't find message (" + id + ")"));
    }

    public void updateStatuses(String senderId, String recipientId, Status status) {
        Query query = new Query(
                Criteria
                        .where("senderId").is(senderId)
                        .and("recipientId").is(recipientId));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, Message.class);
    }
}
