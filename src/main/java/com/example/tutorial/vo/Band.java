package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class Band {
    int no;
    String createMaster;
    String bandName;
    LocalDateTime createAt;
    String category;
    int memberCnt;
    String status;
}
