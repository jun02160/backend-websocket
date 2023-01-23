package com.backend.simya.domain.chat.repository;

import com.backend.simya.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ChatRepository extends JpaRepository<ChatRoom, Long> {

    Slice<ChatRoom> findAllByCreatedAtBeforeAndHouse_HouseIdOrderByCreatedAtDesc(String curserCreatedAt, Long houseId, Pageable pageable);

    List<ChatRoom> findAllByCreatedAtAfterOrderByCreatedAtDesc(String cursorCreatedAt);
}
