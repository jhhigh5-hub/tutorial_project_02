package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class Posts {
    int no;
    int bandNo;
    String writerId;
    String hashtag;
    String content;
    LocalDateTime wroteAt;
    int viewCnt;
    int likeCnt;
    int commentCnt;

    // 댓글 목록 필드 추가
    List<Comment> comments;
}
