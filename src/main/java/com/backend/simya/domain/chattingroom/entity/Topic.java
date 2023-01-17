package com.backend.simya.domain.chattingroom.entity;

import com.backend.simya.domain.user.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "topic")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Topic extends BaseTimeEntity {

    @Id
    @Column(name = "topic_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long topicId;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @OneToOne
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @Column(name = "is_today_topic")
    private boolean isTodayTopic;

    @Column(name = "activated")
    private boolean activated;
}