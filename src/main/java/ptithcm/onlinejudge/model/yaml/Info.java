package ptithcm.onlinejudge.model.yaml;

import java.util.HashMap;
import java.util.List;
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
        "problem_name",
        "difficulty",
        "time_limit",
        "memory_limit",
        "scoring_method",
        "checker",
        "max_score",
        "fill_missing_output",
        "subtasks"
})
@Generated("jsonschema2pojo")
public class Info {

    @JsonProperty("problem_id")
    private String problemId;
    @JsonProperty("problem_name")
    private String problemName;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("time_limit")
    private Integer timeLimit;
    @JsonProperty("memory_limit")
    private Integer memoryLimit;
    @JsonProperty("scoring_method")
    private String scoringMethod;
    @JsonProperty("checker")
    private String checker;
    @JsonProperty("max_score")
    private Integer maxScore;
    @JsonProperty("fill_missing_output")
    private Boolean fillMissingOutput;
    @JsonProperty("subtasks")
    private List<Subtask> subtasks = null;
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

    @JsonProperty("problem_name")
    public String getProblemName() {
        return problemName;
    }

    @JsonProperty("problem_name")
    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    @JsonProperty("difficulty")
    public String getDifficulty() {
        return difficulty;
    }

    @JsonProperty("difficulty")
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @JsonProperty("time_limit")
    public Integer getTimeLimit() {
        return timeLimit;
    }

    @JsonProperty("time_limit")
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
    }

    @JsonProperty("memory_limit")
    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    @JsonProperty("memory_limit")
    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    @JsonProperty("scoring_method")
    public String getScoringMethod() {
        return scoringMethod;
    }

    @JsonProperty("scoring_method")
    public void setScoringMethod(String scoringMethod) {
        this.scoringMethod = scoringMethod;
    }

    @JsonProperty("checker")
    public String getChecker() {
        return checker;
    }

    @JsonProperty("checker")
    public void setChecker(String checker) {
        this.checker = checker;
    }

    @JsonProperty("max_score")
    public Integer getMaxScore() {
        return maxScore;
    }

    @JsonProperty("max_score")
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    @JsonProperty("fill_missing_output")
    public Boolean getFillMissingOutput() {
        return fillMissingOutput;
    }

    @JsonProperty("fill_missing_output")
    public void setFillMissingOutput(Boolean fillMissingOutput) {
        this.fillMissingOutput = fillMissingOutput;
    }

    @JsonProperty("subtasks")
    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @JsonProperty("subtasks")
    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
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