package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    private String problemId;
    private Long countACs;
    private Long countSubs;
    private Long countStudentACs;
    private Long countStudentSubs;
}
