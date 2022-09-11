package ptithcm.onlinejudge.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public Student(String id, String password, String studentFirstName, String studentLastName, Byte active) {
        this.id = id;
        this.password = password;
        this.studentFirstName = studentFirstName;
        this.studentLastName = studentLastName;
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public Byte getActive() {
        return active;
    }

    public void setActive(Byte active) {
        this.active = active;
    }

}