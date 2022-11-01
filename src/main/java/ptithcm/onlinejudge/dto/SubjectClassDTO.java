package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectClassDTO {
    private String subjectClassId;
    private String subjectClassName;
    private String createAt;
    private String updateAt;
    private Byte hide;
    private SubjectDTO subject;
}
