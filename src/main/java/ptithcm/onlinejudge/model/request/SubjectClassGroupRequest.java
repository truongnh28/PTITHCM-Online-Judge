package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectClassGroupRequest {
    private String subjectClassGroupId;
    private String subjectClassGroupName;
    private String subjectClassId;
}
