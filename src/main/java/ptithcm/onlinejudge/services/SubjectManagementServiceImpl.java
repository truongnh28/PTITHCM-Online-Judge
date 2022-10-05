package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.mapper.SubjectMapper;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
@Service
public class SubjectManagementServiceImpl implements SubjectManagementService {
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private SubjectRepository subjectRepository;
    @Override
    public ResponseObject addSubject(SubjectDTO subject) {
        String id = subject.getSubjectId();
        String name = subject.getSubjectName();
        if(subjectRepository.existsById(id))
            return new ResponseObject(HttpStatus.FOUND, "Môn học đã tồn tại", null);
        if (id.isEmpty() || name == null || name.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Mã môn học và tên môn học không được trống", null);
        Subject newSubject = subjectMapper.dtoToEntity(subject);
        newSubject.setCreateAt(Instant.now());
        newSubject.setUpdateAt(Instant.now());
        newSubject.setHide((byte) 0);
        newSubject = subjectRepository.save(newSubject);
        return new ResponseObject(HttpStatus.OK, "Success", newSubject);
    }

    @Override
    public ResponseObject editSubject(String id, SubjectDTO subject) {
        String newName = subject.getSubjectName();
        if (newName == null || newName.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Tên môn học không được để trống", null);
        Optional<Subject> foundOldSubject = subjectRepository.findById(id);
        if (foundOldSubject.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Môn học không tồn tại", null);
        Subject editedSubject = foundOldSubject.get();
        editedSubject.setSubjectName(newName);
        editedSubject.setUpdateAt(Instant.now());
        editedSubject = subjectRepository.save(editedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", editedSubject);
    }

    @Override
    public ResponseObject searchByIdOrName(String keyword) {
        List<Subject> subjects = subjectRepository.searchSubjectByIdOrName(keyword);
        return new ResponseObject(HttpStatus.OK, "Success", subjects);
    }

    @Override
    public ResponseObject getSubjectById(String subjectId) {
        Optional<Subject> foundSubject = subjectRepository.findById(subjectId);
        if (foundSubject.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại môn học", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundSubject.get());
    }

    @Override
    public ResponseObject lockSubject(String subjectId) {
        Optional<Subject> foundSubject = subjectRepository.findById(subjectId);
        if (foundSubject.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại môn học", null);
        Subject lockedSubject = foundSubject.get();
        lockedSubject.setUpdateAt(Instant.now());
        lockedSubject.setHide((byte) 1);
        lockedSubject = subjectRepository.save(lockedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", lockedSubject);
    }

    @Override
    public ResponseObject unlockSubject(String subjectId) {
        Optional<Subject> foundSubject = subjectRepository.findById(subjectId);
        if (foundSubject.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tồn tại môn học", null);
        Subject unlockedSubject = foundSubject.get();
        unlockedSubject.setUpdateAt(Instant.now());
        unlockedSubject.setHide((byte) 0);
        unlockedSubject = subjectRepository.save(unlockedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedSubject);
    }

    @Override
    public ResponseObject getAllSubject() {
        List<Subject> subjects = subjectRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", subjects);
    }
}
