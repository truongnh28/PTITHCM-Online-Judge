package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectDTO;
import ptithcm.onlinejudge.mapper.SubjectMapper;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
public class SubjectManagementServiceImpl implements SubjectManagementService {
    @Autowired
    private SubjectMapper subjectMapper;
    @Autowired
    private SubjectRepository subjectRepository;
    @Override
    public ResponseObject addSubject(SubjectDTO subject) {
        Subject newSubject = subjectMapper.dtoToEntity(subject);
        newSubject.setId(subject.getSubjectId().trim().toUpperCase());
        newSubject.setSubjectName(subject.getSubjectName().trim());
        newSubject.setCreateAt(Instant.now());
        newSubject.setUpdateAt(Instant.now());
        newSubject.setHide((byte) 0);
        newSubject = subjectRepository.save(newSubject);
        return new ResponseObject(HttpStatus.OK, "Success", newSubject);
    }

    @Override
    public ResponseObject editSubject(SubjectDTO subject) {
        String id = subject.getSubjectId();
        String name = subject.getSubjectName().trim();
        Subject editedSubject = subjectRepository.findById(id).get();
        editedSubject.setSubjectName(name);
        editedSubject.setUpdateAt(Instant.now());
        editedSubject = subjectRepository.save(editedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", editedSubject);
    }

    @Override
    public ResponseObject searchByIdOrName(String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Subject> subjects = subjectRepository.searchSubjectByIdOrName(keyword, PageRequest.of(page - 1, 10));
        int totalPage = subjects.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjects.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
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
        Subject lockedSubject = subjectRepository.findById(subjectId).get();
        lockedSubject.setUpdateAt(Instant.now());
        lockedSubject.setHide((byte) 1);
        lockedSubject = subjectRepository.save(lockedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", lockedSubject);
    }

    @Override
    public ResponseObject unlockSubject(String subjectId) {
        Subject unlockedSubject = subjectRepository.findById(subjectId).get();
        unlockedSubject.setUpdateAt(Instant.now());
        unlockedSubject.setHide((byte) 0);
        unlockedSubject = subjectRepository.save(unlockedSubject);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedSubject);
    }

    @Override
    public ResponseObject getAllSubject(int page) {
        if (page <= 0)
            page = 1;
        Page<Subject> subjects = subjectRepository.findAll(PageRequest.of(page - 1, 10));
        int totalPage = subjects.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", subjects.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }
}
