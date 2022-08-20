package com.onlinejudge.springthymeleaf.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private String username;
    private int sum;
    private List<Integer> score = new ArrayList<>();
}
