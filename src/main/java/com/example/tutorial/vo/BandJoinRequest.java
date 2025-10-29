package com.example.tutorial.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter @Getter
public class BandJoinRequest {
    int idx;
    int bandNo;
    String memberId;
    String joinStatus;
    LocalDate joinAt;
}
