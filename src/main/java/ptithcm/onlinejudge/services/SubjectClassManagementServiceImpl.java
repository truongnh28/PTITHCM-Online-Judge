package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.SubjectClassDTO;
import ptithcm.onlinejudge.mapper.SubjectClassMapper;
import ptithcm.onlinejudge.model.entity.Subject;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.model.entity.SubjectClass;
import ptithcm.onlinejudge.model.request.SubjectClassRequest;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.SubjectRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SubjectClassManagementServiceImpl implements SubjectClassManagementService {
    @Autowired
    private SubjectClassMapper subjectClassMapper;
    @Autowired
    SubjectClassRepository subjectClassRepository;
    @Autowired
    SubjectRepository subjectRepository;

    @Override
    public ResponseObject add(String subjectId, SubjectClassDTO subjectClass) {
        String classId = subjectClass.getSubjectClassId().trim().toUpperCase();
        String className = subjectClass.getSubjectClassName().trim();
        Subject subject = subjectRepository.findById(subjectId).get();
        SubjectClass newClass = subjectClassMapper.dtoToEntity(subjectClass);
        newClass.setId(classId);
        newClass.setSubjectClassName(className);
        newClass.setSubject(subject);
        newClass.setHide((byte) 0);
        newClass.setCreateAt(Instant.now());
        newClass.setUpdateAt(Instant.now());
        newClass = subjectClassRepository.save(newClass);
        return new ResponseObject(HttpStatus.OK, "Success", newClass);
    }

    @Override
    public ResponseObject edit(SubjectClassDTO subjectClass) {
        String newName = subjectClass.getSubjectClassName().trim();
        SubjectClass editedClass = subjectClassRepository.findById(subjectClass.getSubjectClassId()).get();
        editedClass.setSubjectClassName(newName);
        editedClass.setUpdateAt(Instant.now());
        editedClass = subjectClassRepository.save(editedClass);
        return new ResponseObject(HttpStatus.OK, "Success", editedClass);
    }

    @Override
    public ResponseObject getAllClassesOfSubject(String subjectId, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClass> classes = subjectClassRepository.getSubjectClassOfSubject(subjectId, PageRequest.of(page - 1, 10));
        int totalPage = classes.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", classes.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchClassesOfSubjectByKeyword(String subjectId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClass> classes = subjectClassRepository.searchSubjectClassOfSubjectByKeyword(subjectId, keyword, PageRequest.of(page - 1, 10));
        int totalPage = classes.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", classes.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject lockClass(String classId) {
        SubjectClass lockedClass = subjectClassRepository.findById(classId).get();
        lockedClass.setUpdateAt(Instant.now());
        lockedClass.setHide((byte) 1);
        lockedClass = subjectClassRepository.save(lockedClass);
        return new ResponseObject(HttpStatus.OK, "Success", lockedClass);
    }

    @Override
    public ResponseObject unlockClass(String classId) {
        SubjectClass unlockedClass = subjectClassRepository.findById(classId).get();
        unlockedClass.setUpdateAt(Instant.now());
        unlockedClass.setHide((byte) 0);
        unlockedClass = subjectClassRepository.save(unlockedClass);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedClass);
    }

    @Override
    public ResponseObject getClassById(String classId) {
        Optional<SubjectClass> foundSubjectClass = subjectClassRepository.findById(classId);
        if (foundSubjectClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lớp không tồn tại", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundSubjectClass.get());
    }

    @Override
    public ResponseObject getClassesTeacherOwnActive(String teacherId, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClass> classes = subjectClassRepository.getClassesTeacherOwnActive(teacherId, PageRequest.of(page - 1, 10));
        int totalPage = classes.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", classes.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchClassesTeacherOwnActive(String teacherId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<SubjectClass> classes = subjectClassRepository.searchClassesTeacherOwnByKeywordActive(teacherId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = classes.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", classes.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    private boolean subjectClassRequestIsValidAddNew(SubjectClassRequest subjectClassRequest) {
        boolean subjectClassNameIsValid = subjectClassRequest.getSubjectClassName().length() > 0;
        boolean subjectIdIsValid = subjectRepository.existsById(subjectClassRequest.getSubjectId());
        return !subjectIdIsValid && subjectClassNameIsValid;
    }
}
