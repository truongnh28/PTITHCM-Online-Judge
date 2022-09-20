package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.request.MultipleProblemTypeRequest;
import ptithcm.onlinejudge.model.request.ProblemHasTypeRequest;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface ProblemHasTypeManagementService {
    ResponseObject addProblemType(ProblemHasTypeRequest problemHasTypeRequest);

    ResponseObject addMultipleProblemType(MultipleProblemTypeRequest multipleProblemTypeRequest);
}
