package com.trantor.chat.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class Room {
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;
}
