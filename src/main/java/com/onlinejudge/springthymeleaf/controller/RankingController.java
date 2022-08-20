package com.onlinejudge.springthymeleaf.controller;

import com.onlinejudge.springthymeleaf.data.Data;
import com.onlinejudge.springthymeleaf.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RankingController {
    @GetMapping("/ranking")
    public String getRank(Model model) {
        model.addAttribute("problems", Data.problemList);
        List<RankingDTO> rankingList = new ArrayList<>();
        for (UserLogin user: Data.userList) {
            RankingDTO rankingDTO = new RankingDTO();
            String username = user.getUsername();
            rankingDTO.setUsername(username);
            List<Integer> score = new ArrayList<>();
            Map<String, Integer> map = new HashMap<>();
            for (ProblemInfoDTO problemInfo: Data.problemList) {
                map.put(problemInfo.getId(), 0);
            }
            for (SubmissionDTO submissionDTO: Data.submissionList) {
                if (submissionDTO.getUsername().equals(username) && submissionDTO.getStatus().equals("Accepted")) {
                    map.put(submissionDTO.getProblemId(), map.get(submissionDTO.getProblemId()) + 100);
                }
            }
            TreeMap<String, Integer> sortedMap = new TreeMap<>(map);
            int sum = 0;
            for (Map.Entry<String, Integer> entry: sortedMap.entrySet()) {
                sum += entry.getValue();
                score.add(entry.getValue());
            }
            rankingDTO.setSum(sum);
            rankingDTO.setScore(score);
            rankingList.add(rankingDTO);
        }
        List<RankingDTO> sortedRankingList = rankingList.stream().sorted(Comparator.comparing(RankingDTO::getSum).reversed()).collect(Collectors.toList());
        model.addAttribute("rankingList", sortedRankingList);
        return "ranking";
    }
}
