package ptithcm.onlinejudge.services;

import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.model.ResponseObject;
import ptithcm.onlinejudge.model.request.ContestRequest;

@Service
public interface ContestManagementService {
    ResponseObject addContest(ContestRequest contestRequest);
    ResponseObject editContest(ContestRequest contestRequest);

}
