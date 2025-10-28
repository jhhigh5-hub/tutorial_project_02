package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Setter @Getter
public class Comment {
    int no;
    int postNo;
    String writerId;
    String content;
    LocalDate commentedAt;
}
