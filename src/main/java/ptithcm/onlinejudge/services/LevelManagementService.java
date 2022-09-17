package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.dto.LevelDTO;
import ptithcm.onlinejudge.model.entity.Level;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface LevelManagementService {
    ResponseObject getAllLevel();
    ResponseObject getLevelById(byte id);
}
