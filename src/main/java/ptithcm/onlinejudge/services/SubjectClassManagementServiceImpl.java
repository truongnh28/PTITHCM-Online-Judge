package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
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
    public ResponseObject addSubjectClass(String subjectId, SubjectClassDTO subjectClass) {
        String classId = subjectClass.getSubjectClassId();
        String className = subjectClass.getSubjectClassName();
        if (subjectClassRepository.existsById(classId))
            return new ResponseObject(HttpStatus.FOUND, "Lớp tồn tại", null);
        if (classId.isEmpty() || className == null || className.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Mã lớp và tên lớp không được để trống", null);
        Optional<Subject> foundSubject = subjectRepository.findById(subjectId);
        if (foundSubject.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Môn học không tồn tại", null);
        Subject subject = foundSubject.get();
        SubjectClass newClass = subjectClassMapper.dtoToEntity(subjectClass);
        newClass.setSubject(subject);
        newClass.setHide((byte) 0);
        newClass.setCreateAt(Instant.now());
        newClass.setUpdateAt(Instant.now());
        newClass = subjectClassRepository.save(newClass);
        return new ResponseObject(HttpStatus.OK, "Success", newClass);
    }

    @Override
    public ResponseObject editSubjectClass(String classId, SubjectClassDTO subjectClass) {
        String newName = subjectClass.getSubjectClassName();
        if (newName == null || newName.isEmpty())
            return new ResponseObject(HttpStatus.BAD_REQUEST, "Tên lớp không được để trống", null);
        Optional<SubjectClass> foundOldClass = subjectClassRepository.findById(classId);
        if (foundOldClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lớp học cũ không tồn tại", null);
        SubjectClass editedClass = foundOldClass.get();
        editedClass.setSubjectClassName(newName);
        editedClass.setUpdateAt(Instant.now());
        editedClass = subjectClassRepository.save(editedClass);
        return new ResponseObject(HttpStatus.OK, "Success", editedClass);
    }

    @Override
    public ResponseObject getAllClass() {
        List<SubjectClass> SubjectClasses = subjectClassRepository.findAll();
        return new ResponseObject(HttpStatus.OK, "Success", SubjectClasses);
    }

    @Override
    public ResponseObject getAllClassesBySubjectId(String subjectId) {
        List<SubjectClass> subjectClasses = subjectClassRepository.getSubjectClassOfSubject(subjectId);
        return new ResponseObject(HttpStatus.OK, "Success", subjectClasses);
    }

    @Override
    public ResponseObject searchClassesByIdOrName(String subjectId, String keyword) {
        List<SubjectClass> subjectClasses = subjectClassRepository.searchSubjectClassOfSubjectByIdOrName(subjectId, keyword);
        return new ResponseObject(HttpStatus.OK, "Succes", subjectClasses);
    }

    @Override
    public ResponseObject lockClass(String classId) {
        Optional<SubjectClass> foundSubjectClass = subjectClassRepository.findById(classId);
        if (foundSubjectClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lớp không tồn tại", null);
        SubjectClass lockedClass = foundSubjectClass.get();
        lockedClass.setUpdateAt(Instant.now());
        lockedClass.setHide((byte) 1);
        lockedClass = subjectClassRepository.save(lockedClass);
        return new ResponseObject(HttpStatus.OK, "Success", lockedClass);
    }

    @Override
    public ResponseObject unlockClass(String classId) {
        Optional<SubjectClass> foundSubjectClass = subjectClassRepository.findById(classId);
        if (foundSubjectClass.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lớp không tồn tại", null);
        SubjectClass unlockedClass = foundSubjectClass.get();
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
    public ResponseObject getClassesTeacherOwnActive(String teacherId) {
        List<SubjectClass> classes = subjectClassRepository.getClassesTeacherOwnActive(teacherId);
        return new ResponseObject(HttpStatus.OK, "Success", classes);
    }

    @Override
    public ResponseObject searchClassesTeacherOwnActive(String teacherId, String keyword) {
        List<SubjectClass> classes = subjectClassRepository.searchClassesTeacherOwnActive(teacherId, keyword);
        return new ResponseObject(HttpStatus.OK, "Success", classes);
    }

    private boolean subjectClassRequestIsValidAddNew(SubjectClassRequest subjectClassRequest) {
        boolean subjectClassNameIsValid = subjectClassRequest.getSubjectClassName().length() > 0;
        boolean subjectIdIsValid = subjectRepository.existsById(subjectClassRequest.getSubjectId());
        return !subjectIdIsValid && subjectClassNameIsValid;
    }
}
