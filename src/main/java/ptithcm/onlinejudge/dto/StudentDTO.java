package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private String studentId;
    private String studentPassword;
    private String studentFirstName;
    private String studentLastName;
    private String studentClass;
    private String createAt;
    private String updateAt;
    private String lastLogin;
    private Byte active;
}
