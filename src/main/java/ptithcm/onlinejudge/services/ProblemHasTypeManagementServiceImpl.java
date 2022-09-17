package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Arrays;
import java.util.List;

@Service
public class ProblemHasTypeManagementServiceImpl implements ProblemHasTypeManagementService {
    @Autowired
    private ProblemHasTypeRepository problemHasTypeRepository;
    @Autowired
    private ProblemManagementService problemManagementService;
    @Autowired
    private ProblemTypeManagementService problemTypeManagementService;
    @Override
    public ResponseObject addProblemType(ProblemHasTypeRequest problemHasTypeRequest) {
        String problemId = problemHasTypeRequest.getProblemId();
        String problemTypeId = problemHasTypeRequest.getProblemTypeId();
        if (!checkRequestIsValid(problemHasTypeRequest))
            return new ResponseObject(HttpStatus.FOUND, "Request data not valid", "");
        ProblemHasType problemHasType = new ProblemHasType();
        ProblemHasTypeId problemHasTypeId = new ProblemHasTypeId();
        problemHasTypeId.setProblemId(problemId);
        problemHasTypeId.setProblemTypeId(problemTypeId);

        ResponseObject responseGetProblemById = problemManagementService.getProblemById(problemId);
        ResponseObject responseGetProblemTypeById = problemTypeManagementService.getProblemTypeById(problemTypeId);

        problemHasType.setProblem((Problem) responseGetProblemById.getData());
        problemHasType.setProblemType((ProblemType) responseGetProblemTypeById.getData());
        problemHasType.setId(problemHasTypeId);

        problemHasType = problemHasTypeRepository.save(problemHasType);
        return new ResponseObject(HttpStatus.OK, "Success", problemHasType);
    }

    @Override
    public ResponseObject addMultipleProblemType(MultipleProblemTypeRequest multipleProblemTypeRequest) {
        String[] typeIds = multipleProblemTypeRequest.getProblemTypeIds();
        String problemId = multipleProblemTypeRequest.getProblemId();
        List<ProblemType> problemTypes = new ArrayList<>();
        for (String typeId: typeIds) {
            ResponseObject responseGetTypeById = problemTypeManagementService.getProblemTypeById(typeId);
            if (!responseGetTypeById.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Problem type is not exist", "");
            problemTypes.add((ProblemType) responseGetTypeById.getData());
        }

        for (ProblemType problemType : problemTypes) {
            ProblemHasTypeRequest problemHasTypeRequest = new ProblemHasTypeRequest(problemId, problemType.getId());
            ResponseObject responseAddProblemType = addProblemType(problemHasTypeRequest);
            if (!responseAddProblemType.getStatus().equals(HttpStatus.OK))
                return new ResponseObject(HttpStatus.FOUND, "Add problem type has error", "");
        }
        return new ResponseObject(HttpStatus.OK, "Success", "");
    }

    private boolean checkRequestIsValid(ProblemHasTypeRequest problemHasTypeRequest) {
        String problemId = problemHasTypeRequest.getProblemId();
        String problemTypeId = problemHasTypeRequest.getProblemTypeId();

        ResponseObject responseGetProblemById = problemManagementService.getProblemById(problemId);
        ResponseObject responseGetProblemTypeById = problemTypeManagementService.getProblemTypeById(problemTypeId);
        return responseGetProblemById.getStatus().equals(HttpStatus.OK) && responseGetProblemTypeById.getStatus().equals(HttpStatus.OK);
    }
}
