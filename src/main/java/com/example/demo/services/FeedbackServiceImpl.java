package com.example.demo.services;

import com.example.demo.dtos.FeedbackRequestDTO;
import com.example.demo.dtos.MyWallResponseDTO;
import com.example.demo.dtos.UserResponseDTO;
import com.example.demo.entities.Feedback;
import com.example.demo.entities.User;
import com.example.demo.repositories.FeedbackRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.FeedbackService;
import com.example.demo.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private SentimentAnalysisService sentimentAnalysisService;


    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public List<MyWallResponseDTO> getFeedbacksForLoggedInUser(String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);
        User loggedInUser = userRepository.findByEmail(email).orElseThrow();

        // Fetch feedbacks for receiver
        List<Feedback> feedbacks = feedbackRepository.findByReceiver(loggedInUser);

        // Convert to DTOs
        return feedbacks.stream().map(f -> {
            MyWallResponseDTO dto = new MyWallResponseDTO();
            dto.setId(f.getId());
            dto.setReceiver(new UserResponseDTO(
                    f.getReceiver().getId(),
                    f.getReceiver().getName(),
                    f.getReceiver().getEmail(),
                    f.getReceiver().getBio()
            ));
            dto.setSomeone_text(f.getSomeone_text());
            dto.setFeedback_text(f.getFeedback_text());
            dto.setFeedback_reaction(f.getFeedback_reaction());
            dto.setSentiment_label(f.getSentiment_label());
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public boolean reactToFeedback(Long feedbackId, String token, String reaction) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);
        User loggedInUser = userRepository.findByEmail(email).orElseThrow();

        // Find the feedback
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Check if logged-in user is the receiver
        if (!feedback.getReceiver().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not allowed to react to this feedback");
        }

        // Update reaction
        feedback.setFeedback_reaction(reaction);
        feedbackRepository.save(feedback);
        return true;
    }

    @Override
    public MyWallResponseDTO getFeedbackById(Long feedbackId, String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);
        User loggedInUser = userRepository.findByEmail(email).orElseThrow();

        // Fetch feedback by ID
        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Feedback not found"));

        // Ensure the logged-in user is the receiver
        if (!feedback.getReceiver().getId().equals(loggedInUser.getId())) {
            throw new RuntimeException("You are not authorized to view this feedback");
        }

        // Convert entity to DTO
        MyWallResponseDTO dto = new MyWallResponseDTO();
        dto.setId(feedback.getId());
        dto.setReceiver(new UserResponseDTO(
                feedback.getReceiver().getId(),
                feedback.getReceiver().getName(),
                feedback.getReceiver().getEmail(),
                feedback.getReceiver().getBio()
        ));
        dto.setSomeone_text(feedback.getSomeone_text());
        dto.setFeedback_text(feedback.getFeedback_text());
        dto.setFeedback_reaction(feedback.getFeedback_reaction());
        dto.setSentiment_label(feedback.getSentiment_label());

        return dto;
    }

    @Override
    public List<MyWallResponseDTO> getFeedbacksWrittenByUser(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract email from JWT
        String email = jwtUtil.extractEmail(token);
        User loggedInUser = userRepository.findByEmail(email).orElseThrow();

        // Fetch all feedbacks where this user is the writer
        List<Feedback> writtenFeedbacks = feedbackRepository.findByWriter(loggedInUser);

        // Map to DTOs
        return writtenFeedbacks.stream().map(f -> {
            MyWallResponseDTO dto = new MyWallResponseDTO();
            dto.setId(f.getId());
            dto.setReceiver(new UserResponseDTO(
                    f.getReceiver().getId(),
                    f.getReceiver().getName(),
                    f.getReceiver().getEmail(),
                    f.getReceiver().getBio()
            ));
            dto.setSomeone_text(f.getSomeone_text());
            dto.setFeedback_text(f.getFeedback_text());
            dto.setFeedback_reaction(f.getFeedback_reaction()); // reaction from receiver
            dto.setSentiment_label(f.getSentiment_label());
            return dto;
        }).collect(Collectors.toList());
    }


//    @Override
//    public MyWallResponseDTO writeFeedback(String token, FeedbackRequestDTO feedbackRequestDTO) {
//        if (token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        // Extract email of logged-in user (writer)
//        String email = jwtUtil.extractEmail(token);
//        User writer = userRepository.findByEmail(email).orElseThrow();
//
//        // Find receiver by ID
//        User receiver = userRepository.findById(feedbackRequestDTO.getReceiverId())
//                .orElseThrow(() -> new RuntimeException("Receiver not found"));
//
//        // Create feedback entity
//        Feedback feedback = new Feedback();
//        feedback.setWriter(writer);
//        feedback.setReceiver(receiver);
//        feedback.setSomeone_text(feedbackRequestDTO.getSomeone_text());
//        feedback.setFeedback_text(feedbackRequestDTO.getFeedback_text());
//        feedback.setFeedback_reaction(null); // receiver will add reaction later
//
//        // Save to DB
//        Feedback savedFeedback = feedbackRepository.save(feedback);
//
//        // Convert to response DTO
//        MyWallResponseDTO dto = new MyWallResponseDTO();
//        dto.setId(savedFeedback.getId());
//        dto.setReceiver(new UserResponseDTO(
//                receiver.getId(),
//                receiver.getName(),
//                receiver.getEmail(),
//                receiver.getBio()
//        ));
//        dto.setSomeone_text(savedFeedback.getSomeone_text());
//        dto.setFeedback_text(savedFeedback.getFeedback_text());
//        dto.setFeedback_reaction(savedFeedback.getFeedback_reaction());
//        dto.setSentiment_label(savedFeedback.getSentiment_label());
//
//        return dto;
//    }


    @Override
    public MyWallResponseDTO writeFeedback(String token, FeedbackRequestDTO feedbackRequestDTO) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Extract email of logged-in user (writer)
        String email = jwtUtil.extractEmail(token);
        User writer = userRepository.findByEmail(email).orElseThrow();

        // Find receiver by ID
        User receiver = userRepository.findById(feedbackRequestDTO.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        // Create feedback entity
        Feedback feedback = new Feedback();
        feedback.setWriter(writer);
        feedback.setReceiver(receiver);
        feedback.setSomeone_text(feedbackRequestDTO.getSomeone_text());
        feedback.setFeedback_text(feedbackRequestDTO.getFeedback_text());
        feedback.setFeedback_reaction(null);

        // Analyze sentiment before saving
        SentimentAnalysisService.SentimentResult sentiment =
                sentimentAnalysisService.analyzeSentiment(feedbackRequestDTO.getFeedback_text());

        feedback.setSentiment_label(sentiment.getLabel());
        feedback.setSentiment_score((float) sentiment.getScore());

        // Save to DB
        Feedback savedFeedback = feedbackRepository.save(feedback);

        // Convert to DTO
        MyWallResponseDTO dto = new MyWallResponseDTO();
        dto.setId(savedFeedback.getId());
        dto.setReceiver(new UserResponseDTO(
                receiver.getId(),
                receiver.getName(),
                receiver.getEmail(),
                receiver.getBio()
        ));
        dto.setSomeone_text(savedFeedback.getSomeone_text());
        dto.setFeedback_text(savedFeedback.getFeedback_text());
        dto.setFeedback_reaction(savedFeedback.getFeedback_reaction());
        dto.setSentiment_label(savedFeedback.getSentiment_label());

        return dto;
    }



}
