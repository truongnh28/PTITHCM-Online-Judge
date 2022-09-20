package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.ProblemType;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ProblemTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProblemTypeManagementServiceImpl implements ProblemTypeManagementService {
    @Autowired
    private ProblemTypeRepository problemTypeRepository;
    @Override
    public ResponseObject getAllProblemTypes() {
        List<ProblemType> problemTypes = problemTypeRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", problemTypes);
    }

    @Override
    public ResponseObject getProblemTypeById(String problemTypeId) {
        Optional<ProblemType> foundProblemType = problemTypeRepository.findById(problemTypeId);
        if (foundProblemType.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Problem type id not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundProblemType.get());
    }
}
