package com.trantor.chat.service;

import com.trantor.chat.model.Room;
import com.trantor.chat.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository chatRoomRepository;

    public Optional<String> getChatId(
            String senderId, String recipientId, boolean createIfNotExist) {

        return Optional.ofNullable(chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(Room::getChatId)
                .orElseGet(() -> {
                    if (!createIfNotExist) {
                        return "";
                    }
                    String chatId =
                            String.format("%s_%s", senderId, recipientId);

                    Room senderRecipient = Room
                            .builder()
                            .chatId(chatId)
                            .senderId(senderId)
                            .recipientId(recipientId)
                            .build();

                    Room recipientSender = Room
                            .builder()
                            .chatId(chatId)
                            .senderId(recipientId)
                            .recipientId(senderId)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return chatId;
                }));
    }
}
