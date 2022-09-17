package ptithcm.onlinejudge.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teachers")
public class Teacher {
    @Id
    @Column(name = "teacher_id", nullable = false, length = 10)
    private String id;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "teacher_first_name", nullable = false, length = 100)
    private String teacherFirstName;

    @Column(name = "teacher_last_name", nullable = false, length = 100)
    private String teacherLastName;

    @Column(name = "active")
    private Byte active;
}