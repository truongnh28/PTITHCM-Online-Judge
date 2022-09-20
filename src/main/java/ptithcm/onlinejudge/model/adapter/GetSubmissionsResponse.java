package ptithcm.onlinejudge.model.adapter;

import java.util.HashMap;
        import java.util.Map;
        import javax.annotation.Generated;
        import com.fasterxml.jackson.annotation.JsonAnyGetter;
        import com.fasterxml.jackson.annotation.JsonAnySetter;
        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "problem_id",
        "username",
        "score",
        "job_id",
        "verdict",
        "timestamp"
})
@Generated("jsonschema2pojo")
public class GetSubmissionsResponse {

    @JsonProperty("problem_id")
    private String problemId;
    @JsonProperty("username")
    private String username;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("job_id")
    private String jobId;
    @JsonProperty("verdict")
    private String verdict;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("problem_id")
    public String getProblemId() {
        return problemId;
    }

    @JsonProperty("problem_id")
    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("score")
    public Integer getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(Integer score) {
        this.score = score;
    }

    @JsonProperty("job_id")
    public String getJobId() {
        return jobId;
    }

    @JsonProperty("job_id")
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    @JsonProperty("verdict")
    public String getVerdict() {
        return verdict;
    }

    @JsonProperty("verdict")
    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}