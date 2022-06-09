package com.trantor.chat.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Data
public class ChatNotification {
    private String id;
    private String senderId;
    private String senderName;
}
