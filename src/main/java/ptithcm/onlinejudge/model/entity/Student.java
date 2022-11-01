package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "students")
public class Student {
    @Id
    @Column(name = "student_id", nullable = false, length = 10)
    private String id;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "student_first_name", nullable = false, length = 100)
    private String studentFirstName;

    @Column(name = "student_last_name", nullable = false, length = 100)
    private String studentLastName;

    @Column(name = "student_class", nullable = false, length = 20)
    private String studentClass;

    @Column(name = "create_at", nullable = false)
    private Instant createAt;

    @Column(name = "update_at", nullable = false)
    private Instant updateAt;

    @Column(name = "last_login", nullable = false)
    private Instant lastLogin;

    @Column(name = "active")
    private Byte active;
}