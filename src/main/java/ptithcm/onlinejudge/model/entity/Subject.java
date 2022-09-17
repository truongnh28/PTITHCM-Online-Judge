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
@Table(name = "subjects")
public class Subject {
    @Id
    @Column(name = "subject_id", nullable = false, length = 10)
    private String id;

    @Column(name = "subject_name", length = 100)
    private String subjectName;
}