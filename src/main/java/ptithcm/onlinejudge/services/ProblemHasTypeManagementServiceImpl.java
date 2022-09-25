package ptithcm.onlinejudge.services;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.ProblemHasType;
import ptithcm.onlinejudge.model.entity.ProblemHasTypeId;
import ptithcm.onlinejudge.model.entity.ProblemType;
import ptithcm.onlinejudge.model.request.MultipleProblemTypeRequest;
import ptithcm.onlinejudge.model.request.ProblemHasTypeRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ProblemHasTypeRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ProblemHasTypeManagementServiceImpl implements ProblemHasTypeManagementService {
    private ProblemHasTypeRepository problemHasTypeRepository;
    private ProblemTypeManagementService problemTypeManagementService;
    @Override
    public ResponseObject addProblemType(ProblemHasTypeRequest problemHasTypeRequest) {
        Problem problem = problemHasTypeRequest.getProblem();
        ProblemType problemType = problemHasTypeRequest.getProblemType();
        ProblemHasType problemHasType = new ProblemHasType();
        ProblemHasTypeId problemHasTypeId = new ProblemHasTypeId();
        problemHasTypeId.setProblemId(problem.getId());
        problemHasTypeId.setProblemTypeId(problemType.getId());
        problemHasType.setId(problemHasTypeId);
        problemHasType.setProblem(problem);
        problemHasType.setProblemType(problemType);
        problemHasType = problemHasTypeRepository.save(problemHasType);
        return new ResponseObject(HttpStatus.OK, "Success", problemHasType);
    }

    @Override
    public ResponseObject addMultipleProblemType(MultipleProblemTypeRequest multipleProblemTypeRequest) {
        String[] typeIds = multipleProblemTypeRequest.getProblemTypeIds();
        Problem problem = multipleProblemTypeRequest.getProblem();
        List<ProblemType> problemTypes = new ArrayList<>();
        for (String typeId: typeIds) {
            ResponseObject responseGetTypeById = problemTypeManagementService.getProblemTypeById(typeId);
            if (!responseGetTypeById.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Problem type is not exist", "");
            problemTypes.add((ProblemType) responseGetTypeById.getData());
        }

        for (ProblemType problemType : problemTypes) {
            ProblemHasTypeRequest problemHasTypeRequest = new ProblemHasTypeRequest(problem, problemType);
            ResponseObject responseAddProblemType = addProblemType(problemHasTypeRequest);
            if (!responseAddProblemType.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Add problem type has error", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }
}
