package com.example.tutorial.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
public class LoginHistory {
    int idx;
    String memberId;
    LocalDate loginAt;
}
