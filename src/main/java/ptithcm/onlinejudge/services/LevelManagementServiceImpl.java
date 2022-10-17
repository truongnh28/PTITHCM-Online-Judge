package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.LevelDTO;
import ptithcm.onlinejudge.model.entity.Level;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.LevelRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LevelManagementServiceImpl implements LevelManagementService {
    @Autowired
    private LevelRepository levelRepository;
    @Override
    public ResponseObject getAllLevels() {
        List<Level> levels = levelRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", levels);
    }

    @Override
    public ResponseObject getLevelById(byte id) {
        Optional<Level> foundLevel = levelRepository.findById(id);
        if (foundLevel.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Level id not exist", "");
        return new ResponseObject(HttpStatus.OK, "Success", foundLevel.get());
    }
}
