package ptithcm.onlinejudge.model.adapter;

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
        "final_score",
        "is_bonus",
        "max_score",
        "score",
        "status",
        "subtasks"
})
@Generated("jsonschema2pojo")
public class GetStatusResponse {

    @JsonProperty("final_score")
    private Integer finalScore;
    @JsonProperty("is_bonus")
    private List<Integer> isBonus = null;
    @JsonProperty("max_score")
    private Integer maxScore;
    @JsonProperty("score")
    private List<Integer> score = null;
    @JsonProperty("status")
    private String status;
    @JsonProperty("subtasks")
    private List<List<List<String>>> subtasks = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("final_score")
    public Integer getFinalScore() {
        return finalScore;
    }

    @JsonProperty("final_score")
    public void setFinalScore(Integer finalScore) {
        this.finalScore = finalScore;
    }

    @JsonProperty("is_bonus")
    public List<Integer> getIsBonus() {
        return isBonus;
    }

    @JsonProperty("is_bonus")
    public void setIsBonus(List<Integer> isBonus) {
        this.isBonus = isBonus;
    }

    @JsonProperty("max_score")
    public Integer getMaxScore() {
        return maxScore;
    }

    @JsonProperty("max_score")
    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    @JsonProperty("score")
    public List<Integer> getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(List<Integer> score) {
        this.score = score;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("subtasks")
    public List<List<List<String>>> getSubtasks() {
        return subtasks;
    }

    @JsonProperty("subtasks")
    public void setSubtasks(List<List<List<String>>> subtasks) {
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