package ptithcm.onlinejudge.services;

import org.springframework.web.multipart.MultipartFile;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.yaml.Info;
import ptithcm.onlinejudge.model.yaml.ProblemYaml;

import java.util.List;

public interface AddProblemToYaml {
    ResponseObject addProblemToYaml(ProblemYaml problemYaml);
    ResponseObject addProblemToDir(Info info, MultipartFile[] inputs, MultipartFile[] outputs);
    ResponseObject addProblemInfo(Info info, String problemPath);
    Info getProblemInfo(String problemPath);
    List<ProblemYaml> getProblemYaml();
}
