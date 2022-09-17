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

    @Column(name = "active")
    private Byte active;
}