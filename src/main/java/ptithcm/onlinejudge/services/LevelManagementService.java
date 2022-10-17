package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.model.response.ResponseObject;

public interface LevelManagementService {
    ResponseObject getAllLevels();
    ResponseObject getLevelById(byte id);
}
