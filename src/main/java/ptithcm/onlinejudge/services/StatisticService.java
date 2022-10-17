package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface StatisticService {
    ResponseObject statisticContest(String contestId);
}
