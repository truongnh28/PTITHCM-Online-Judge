package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface ProblemTypeManagementService {
    ResponseObject getAllProblemTypes();
    ResponseObject getProblemTypeById(String problemTypeId);
}
