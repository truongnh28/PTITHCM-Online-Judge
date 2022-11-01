package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContestDTO {
    private String contestId;
    private String contestName;
    private String contestStart;
    private String contestEnd;
    private String createAt;
    private String updateAt;
    private Byte hide;
    private TeacherDTO teacher;
}
