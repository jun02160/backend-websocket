package com.backend.simya.domain.chat.dto.response;

import com.backend.simya.domain.chat.dto.request.ChatMessageSaveDto;
import com.backend.simya.domain.chat.entity.Chat;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatPagingResponseDto {

    private Long houseId;
    private String sender;
    private String message;
    private String createdAt;
    private String nickname;  // TODO sender 와 nickname 둘 다 필요할까? sender(User)-nickname(Profile) 의 관계면 OK

    public static ChatPagingResponseDto of(Chat chat) {
        return ChatPagingResponseDto.builder()
                .sender(chat.getUsers())
                .houseId(chat.getHouse().getHouseId())
                .createdAt(chat.getCreatedAt())
                .message(chat.getMessage())
                .build();
    }

    public static ChatPagingResponseDto byChatMessageDto(ChatMessageSaveDto chatMessageSaveDto) {
        return ChatPagingResponseDto.builder()
                .sender(chatMessageSaveDto.getSender())
                .createdAt(chatMessageSaveDto.getCreatedAt())
                .houseId(Long.parseLong(chatMessageSaveDto.getRoomId()))   // 이야기 집과 채팅방의 ID는 동일
                .message(chatMessageSaveDto.getMessage())
                .build();
    }
}
