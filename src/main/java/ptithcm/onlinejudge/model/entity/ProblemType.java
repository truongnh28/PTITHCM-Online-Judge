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
@Table(name = "problem_type")
public class ProblemType {
    @Id
    @Column(name = "problem_type_id", nullable = false, length = 100)
    private String id;

    @Column(name = "problem_type_name", nullable = false, length = 100)
    private String problemTypeName;
}