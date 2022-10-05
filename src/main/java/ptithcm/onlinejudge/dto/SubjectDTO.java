package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO {
    private String subjectId;
    private String subjectName;
    private String createAt;
    private String updateAt;
    private Byte hide;
}
