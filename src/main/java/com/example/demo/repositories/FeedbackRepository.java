package com.example.demo.repositories;

import com.example.demo.entities.Feedback;
import com.example.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByReceiver(User receiver);

    List<Feedback> findByWriter(User writer);

}
