package com.example.demo.dtos;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MyWallResponseDTO {

    private Long id;
    private UserResponseDTO receiver;
    private String someone_text;
    private String feedback_text;
    private String feedback_reaction;
    private String sentiment_label;
}

