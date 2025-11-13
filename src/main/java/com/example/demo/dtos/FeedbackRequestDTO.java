package com.example.demo.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackRequestDTO {
    private Long receiverId;
    private String someone_text;
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




