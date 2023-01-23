package com.backend.simya.global.util;

import com.backend.simya.domain.chat.dto.request.ChatMessageSaveDto;
import com.backend.simya.domain.chat.entity.Chat;
import com.backend.simya.domain.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.backend.simya.domain.chat.repository.ChatRoomRepository.CHAT_SORTED_SET_;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatUtils {

    private final ChatRepository chatRepository;
    private final RedisTemplate<String, ChatMessageSaveDto> chatRedisTemplate;
    private ZSetOperations<String, ChatMessageSaveDto> zSetOperations;

    /**
     * Destination 으로부터 roomId 값 조회
     */
    public String getRoomIdFromDestination(String destination) {
        int lastIdx = destination.lastIndexOf('/');
        if (lastIdx != -1) {
            return destination.substring(lastIdx+1);
        } else {
            return "";
        }
    }

    /**
     * 7일 전까지의 Chat Data DB -> Redis 로 Insert
     */
    public void cachingDataToRedisFromDB() {
        zSetOperations = chatRedisTemplate.opsForZSet();

        // 서버 시작 전, Redis 에 데이터 적재시키기
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime cursorDate = current.minusDays(7);

        String cursor = cursorDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
        log.info("7일 전 날짜 : {}", cursor);

        // 7일 전까지의 데이터를 모두 가져와서 Redis 에 적재
        List<Chat> chatList = chatRepository.findAllByCreatedAtAfterOrderByCreatedAtDesc(cursor);

        for (Chat chat : chatList) {
            ChatMessageSaveDto chatMessageSaveDto = ChatMessageSaveDto.of(chat);
            zSetOperations.add(CHAT_SORTED_SET_ + chat.getHouse().getHouseId(), chatMessageSaveDto, changeLocalDateTimeToDouble(chat.getCreatedAt()));
        }
    }

    /**
     * 채팅 데이터 생성일자 Double 형으로 형 변환
     */
    public Double changeLocalDateTimeToDouble(String createdAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        LocalDateTime localDateTime = LocalDateTime.parse(createdAt, formatter);
        return ((Long) localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).doubleValue();
    }
}
