package com.example.demo.controllers;



import com.example.demo.dtos.FeedbackReactionRequestDTO;
import com.example.demo.dtos.FeedbackRequestDTO;
import com.example.demo.dtos.MyWallResponseDTO;
import com.example.demo.services.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/my-wall")
    public List<MyWallResponseDTO> getMyWallFeedbacks(@RequestHeader("Authorization") String token){
        return feedbackService.getFeedbacksForLoggedInUser(token);
    }

    @GetMapping("/my-wall/{feedbackId}")
    public MyWallResponseDTO getFeedbackById(
            @PathVariable Long feedbackId,
            @RequestHeader("Authorization") String token) {
        return feedbackService.getFeedbackById(feedbackId, token);
    }

    @PutMapping("/my-wall/{feedbackId}/react")
    public String reactToFeedback(
            @PathVariable Long feedbackId,
            @RequestHeader("Authorization") String token,
            @RequestBody FeedbackReactionRequestDTO requestDTO) {

        boolean updated = feedbackService.reactToFeedback(feedbackId, token, requestDTO.getFeedbackReaction());
        if (updated) {
            return "Reaction added successfully!";
        } else {
            return "Failed to react to feedback.";
        }
    }

    @GetMapping("/my-written")
    public List<MyWallResponseDTO> getMyWrittenFeedbacks(
            @RequestHeader("Authorization") String token) {
        return feedbackService.getFeedbacksWrittenByUser(token);
    }

    @PostMapping("/write")
    public MyWallResponseDTO writeFeedback(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid FeedbackRequestDTO feedbackRequestDTO) {
        return feedbackService.writeFeedback(token, feedbackRequestDTO);
    }
}

