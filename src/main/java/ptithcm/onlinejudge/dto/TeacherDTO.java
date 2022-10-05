package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherDTO {
    private String teacherId;
    private String teacherPassword;
    private String teacherFirstName;
    private String teacherLastName;
    private boolean active;
    private String createAt;
    private String updateAt;
    private String lastLogin;
    private RoleDTO role;
}
