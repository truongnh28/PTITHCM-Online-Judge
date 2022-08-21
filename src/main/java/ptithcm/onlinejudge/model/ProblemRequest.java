package ptithcm.onlinejudge.model;

public class ProblemRequest {
    private String problemId;
    private String problemName;
    private int score;
    private String contestId;

    public ProblemRequest(String problemId, String problemName, int score, String contestId) {
        this.problemId = problemId;
        this.problemName = problemName;
        this.score = score;
        this.contestId = contestId;
    }

    public String getProblemId() {
        return problemId;
    }

    public void setProblemId(String problemId) {
        this.problemId = problemId;
    }

    public String getProblemName() {
        return problemName;
    }

    public void setProblemName(String problemName) {
        this.problemName = problemName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContestId() {
        return contestId;
    }

    public void setContestId(String contestId) {
        this.contestId = contestId;
    }
}
