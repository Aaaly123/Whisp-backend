package com.example.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDTO {

    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    @NotBlank(message = "Someone text cannot be empty")
    private String someone_text;

    @NotBlank(message = "Feedback text cannot be empty")
    private String feedback_text;


    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getSomeone_text() {
        return someone_text;
    }

    public void setSomeone_text(String someone_text) {
        this.someone_text = someone_text;
    }

    public String getFeedback_text() {
        return feedback_text;
    }

    public void setFeedback_text(String feedback_text) {
        this.feedback_text = feedback_text;
    }

}




