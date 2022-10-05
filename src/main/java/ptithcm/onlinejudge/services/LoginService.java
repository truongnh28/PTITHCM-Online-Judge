package ptithcm.onlinejudge.services;

import ptithcm.onlinejudge.dto.LoginDTO;
import ptithcm.onlinejudge.model.response.ResponseObject;

public interface LoginService {
    ResponseObject login(LoginDTO loginDTO);
}
