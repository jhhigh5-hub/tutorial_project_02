package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
public class Posts {
    int no;
    String writerId;
    String hashtag;
    String content;
    LocalDate wroteAt;
    int viewCnt;
    int likeCnt;
    int commentCnt;
}
