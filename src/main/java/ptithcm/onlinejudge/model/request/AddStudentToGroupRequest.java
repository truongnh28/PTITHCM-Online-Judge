package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentToGroupRequest {
    private String studentId;
    private String classId;
    private String classGroupId;
}
