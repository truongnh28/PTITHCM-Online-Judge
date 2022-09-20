package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentOfGroupRequest {
    private String studentId;
    private String subjectClassGroupId;
}
