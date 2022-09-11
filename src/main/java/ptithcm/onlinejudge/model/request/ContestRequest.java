package ptithcm.onlinejudge.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ptithcm.onlinejudge.model.entity.Teacher;

import java.sql.Time;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContestRequest {
    private String contestId;
    private String contestName;
    private String contestStartTime;
    private String contestEndTime;
    private String teacherId;
}

