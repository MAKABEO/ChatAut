package com.trantor.chat.repository;

import com.trantor.chat.model.Message;
import com.trantor.chat.model.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    long countBySenderIdAndRecipientIdAndStatus(
            String senderId, String recipientId, Status status);

    List<Message> findByChatId(String chatId);
}
