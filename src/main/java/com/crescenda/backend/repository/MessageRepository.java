package com.crescenda.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crescenda.backend.model.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatId(Long chatId);
}