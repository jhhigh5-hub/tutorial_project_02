package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter @Getter
public class Comment {
    int no;
    int postNo;
    String writerId;
    String content;
    LocalDateTime commentedAt;
}
