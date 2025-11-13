package com.example.demo.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseDTO {
    private Long id;
    private UserResponseDTO writer;
    private UserResponseDTO receiver;
    private String someone_text;
    private String feedback_text;
    private String feedback_reaction;
    private String sentiment_label;
    private Float sentiment_score;
}
