package com.example.tutorial.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter @Getter
public class LoginHistory {
    int idx;
    String memberId;
    LocalDateTime loginAt;
}
