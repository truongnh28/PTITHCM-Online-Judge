package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentOfGroupDTO {
    private StudentDTO student;
    private SubjectClassGroupDTO subjectClassGroup;
}
