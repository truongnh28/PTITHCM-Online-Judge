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
@Table(name = "level")
public class Level {
    @Id
    @Column(name = "level_id", nullable = false)
    private Byte id;

    @Column(name = "level_name", length = 20)
    private String levelName;
}
