package com.example.demo.services;


import com.example.demo.dtos.FeedbackRequestDTO;
import com.example.demo.dtos.MyWallResponseDTO;

import java.util.List;

public interface FeedbackService {

    List<MyWallResponseDTO> getFeedbacksForLoggedInUser(String token);

    MyWallResponseDTO getFeedbackById(Long feedbackId, String token);

    boolean reactToFeedback(Long feedbackId, String token, String reaction);

    List<MyWallResponseDTO> getFeedbacksWrittenByUser(String token);

    MyWallResponseDTO writeFeedback(String token, FeedbackRequestDTO feedbackRequestDTO);


}

