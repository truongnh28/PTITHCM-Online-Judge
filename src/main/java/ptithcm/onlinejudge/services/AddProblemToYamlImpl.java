package ptithcm.onlinejudge.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.model.yaml.ProblemYaml;
import ptithcm.onlinejudge.model.yaml.ProblemsYaml;
import ptithcm.onlinejudge.model.yaml.Subtask;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AddProblemToYamlImpl implements AddProblemToYaml{
    static final private String problemsPath = System.getProperty("user.dir") + "/problem_info/problems.yml";
    static final private String problemInfoPath = System.getProperty("user.dir") + "/problem_info";
    @Override
    public ResponseObject addProblemToYaml(ProblemYaml problemYaml) {
        ObjectMapper objectMapper = new YAMLMapper();
        ProblemsYaml problemsYamlObjectMap = null;
        try {
            problemsYamlObjectMap = objectMapper.readValue(new File(problemsPath),
                    new TypeReference<>() {});
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Open file problems.yml err", "");
        }

        List<ProblemYaml> problemList = problemsYamlObjectMap.getGroups().get(0).getProblems();
        problemList.add(problemYaml);
        try {
            objectMapper.writeValue(new File(problemsPath), problemsYamlObjectMap);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Write file problems.yml err", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    @Override
    public ResponseObject addProblemToDir(Info info, MultipartFile[] inputs, MultipartFile[] outputs) {
        File problem = new File(problemInfoPath + "/" + info.getProblemId());
        File problemInfo = new File(problemInfoPath + "/" + info.getProblemId() + "/subtasks");
        if (!problem.exists()){
            problem.mkdirs();
        }
        if (!problemInfo.exists()){
            problemInfo.mkdirs();
        }
        ResponseObject resp = addProblemInfo(info, problemInfoPath + "/" + info.getProblemId() + "/info.yml");
        //validate resp
        // in
//        Subtask subtask = new Subtask();
//        subtask.setName(info.getSubtasks().get(0));
        return null;
    }

    @Override
    public ResponseObject addProblemInfo(Info info, String problemPath) {
        ObjectMapper objectMapper = new YAMLMapper();
        try {
            objectMapper.writeValue(new File(problemPath), info);
        } catch (IOException e) {
            return new ResponseObject(HttpStatus.FOUND, "Write file info.yml err", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    public static void main(String []args) {

    }
}
