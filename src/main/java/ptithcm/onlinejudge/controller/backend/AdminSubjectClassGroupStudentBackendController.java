package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.StudentRepository;
import ptithcm.onlinejudge.repository.SubjectClassGroupRepository;
import ptithcm.onlinejudge.repository.SubjectClassRepository;
import ptithcm.onlinejudge.repository.SubjectRepository;

@RestController
@RequestMapping("/admin/subject/{subjectId}/class/{classId}/group/{groupId}/student")
public class AdminSubjectClassGroupStudentBackendController {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectClassRepository subjectClassRepository;
    @Autowired
    private SubjectClassGroupRepository subjectClassGroupRepository;
    @Autowired
    private StudentRepository studentRepository;
    @GetMapping("/{studentId}")
    public ResponseObject getOne(@PathVariable("subjectId") String subjectId,
                                 @PathVariable("classId") String classId,
                                 @PathVariable("groupId") String groupId,
                                 @PathVariable("studentId") String studentId) {
        if (!checkValid(subjectId, classId, groupId))
            return new ResponseObject(HttpStatus.FOUND, "Mã môn hoặc mã lớp hoặc mã nhóm thực hành không tồn tại! Vui lòng kiểm tra lại", null);
        if (!studentRepository.existsById(studentId))
            return new ResponseObject(HttpStatus.FOUND, "Mã sinh viên không tồn tại! Vui lòng kiểm tra lại", null);
        return new ResponseObject(HttpStatus.OK, "Success", studentRepository.findById(studentId));
    }

    private boolean checkValid(String subjectId, String classId, String groupId) {
        return subjectRepository.existsById(subjectId) && subjectClassRepository.existsById(classId) && subjectClassGroupRepository.existsById(groupId);
    }
}
