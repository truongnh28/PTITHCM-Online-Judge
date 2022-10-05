package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectClassGroupDTO {
    private String groupId;
    private String groupName;
    private String createAt;
    private String updateAt;
    private Byte hide;
    private SubjectClassDTO subjectClass;
}
