package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Setter @Getter
public class BandJoinRequest {
    int idx;
    int bandNo;
    String memberId;
    String joinStatus;
    LocalDateTime joinAt;
}
