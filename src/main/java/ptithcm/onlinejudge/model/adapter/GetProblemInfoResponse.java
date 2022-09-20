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
        "bonus",
        "difficulty",
        "hints",
        "id",
        "max_score",
        "memory_limit",
        "name",
        "statement",
        "time_limit"
})
@Generated("jsonschema2pojo")
public class GetProblemInfoResponse {

    @JsonProperty("bonus")
    private String bonus;
    @JsonProperty("difficulty")
    private String difficulty;
    @JsonProperty("hints")
    private String hints;
    @JsonProperty("id")
    private String id;
    @JsonProperty("max_score")
    private Integer maxScore;
    @JsonProperty("memory_limit")
    private Integer memoryLimit;
    @JsonProperty("name")
    private String name;
    @JsonProperty("statement")
    private String statement;
    @JsonProperty("time_limit")
    private Integer timeLimit;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bonus")
    public String getBonus() {
        return bonus;
    }

    @JsonProperty("bonus")
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    @JsonProperty("difficulty")
    public String getDifficulty() {
        return difficulty;
    }

    @JsonProperty("difficulty")
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    @JsonProperty("hints")
    public String getHints() {
        return hints;
    }

    @JsonProperty("hints")
    public void setHints(String hints) {
        this.hints = hints;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("max_score")
    public Integer getMaxScore() {
        return maxScore;
    }

    @JsonProperty("max_score")
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    @JsonProperty("memory_limit")
    public Integer getMemoryLimit() {
        return memoryLimit;
    }

    @JsonProperty("memory_limit")
    public void setMemoryLimit(Integer memoryLimit) {
        this.memoryLimit = memoryLimit;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("statement")
    public String getStatement() {
        return statement;
    }

    @JsonProperty("statement")
    public void setStatement(String statement) {
        this.statement = statement;
    }

    @JsonProperty("time_limit")
    public Integer getTimeLimit() {
        return timeLimit;
    }

    @JsonProperty("time_limit")
    public void setTimeLimit(Integer timeLimit) {
        this.timeLimit = timeLimit;
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