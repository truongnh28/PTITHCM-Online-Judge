package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.PasswordChangeDTO;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.Role;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.RoleRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TeacherManagementServiceImpl implements TeacherManagementService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public ResponseObject getAllTeachersExceptAdmin(int page) {
        if (page <= 0)
            page = 1;
        Page<Teacher> teachers = teacherRepository.getAllTeacherExceptAdmin(PageRequest.of(page - 1, 10));
        int totalPage = teachers.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", teachers.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchTeachersByKeyWordExceptAdmin(String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Teacher> teachers = teacherRepository.searchTeachersByKeywordExceptAdmin("%" + keyword + "%",PageRequest.of(page - 1, 10));
        int totalPage = teachers.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", teachers.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject addTeacher(TeacherDTO teacherDTO) {
        Optional<Role> foundRole = roleRepository.findById((byte) 2);
        if (foundRole.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lỗi server", null);
        Teacher teacher = teacherMapper.dtoToEntity(teacherDTO);
        teacher.setId(teacherDTO.getTeacherId().trim().toUpperCase());
        teacher.setTeacherFirstName(teacherDTO.getTeacherFirstName().trim());
        teacher.setTeacherLastName(teacherDTO.getTeacherLastName().trim());
        teacher.setPassword(SHA256Helper.hash("123456"));
        teacher.setActive((byte) 1);
        teacher.setCreateAt(Instant.now());
        teacher.setUpdateAt(Instant.now());
        teacher.setLastLogin(Instant.now());
        teacher.setRole(foundRole.get());
        teacher = teacherRepository.save(teacher);
        return new ResponseObject(HttpStatus.OK, "Success", teacher);
    }

    @Override
    public ResponseObject editTeacher(TeacherDTO teacherDTO) {
        Teacher newTeacher = teacherRepository.findById(teacherDTO.getTeacherId()).get();
        newTeacher.setTeacherFirstName(teacherDTO.getTeacherFirstName().trim());
        newTeacher.setTeacherLastName(teacherDTO.getTeacherLastName().trim());
        newTeacher.setUpdateAt(Instant.now());
        newTeacher = teacherRepository.save(newTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", newTeacher);
    }

    @Override
    public ResponseObject updateTeacher(String id, TeacherDTO teacherDTO) {
        Teacher newTeacher = teacherRepository.findById(id).get();
        newTeacher.setTeacherFirstName(teacherDTO.getTeacherFirstName().trim());
        newTeacher.setTeacherLastName(teacherDTO.getTeacherLastName().trim());
        newTeacher.setUpdateAt(Instant.now());
        newTeacher = teacherRepository.save(newTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", newTeacher);
    }

    @Override
    public ResponseObject changePassword(String id, PasswordChangeDTO passwordChange) {
        Teacher teacher = teacherRepository.findById(id).get();
        teacher.setPassword(SHA256Helper.hash(passwordChange.getNewPassword()));
        teacher.setUpdateAt(Instant.now());
        return new ResponseObject(HttpStatus.OK, "Success", teacher);
    }

    // lock
    @Override
    public ResponseObject lockTeacher(String teacherId) {
        Teacher lockedTeacher = teacherRepository.findById(teacherId).get();
        lockedTeacher.setActive((byte) 0);
        lockedTeacher.setUpdateAt(Instant.now());
        lockedTeacher = teacherRepository.save(lockedTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", lockedTeacher);
    }

    // unlock
    @Override
    public ResponseObject unlockTeacher(String teacherId) {
        Teacher unlockedTeacher = teacherRepository.findById(teacherId).get();
        unlockedTeacher.setActive((byte) 1);
        unlockedTeacher.setUpdateAt(Instant.now());
        unlockedTeacher = teacherRepository.save(unlockedTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", unlockedTeacher);
    }

    // reset password
    @Override
    public ResponseObject resetPasswordTeacher(String teacherId) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        if (foundTeacher.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy teacher", null);
        Teacher resetPasswordTeacher = foundTeacher.get();
        resetPasswordTeacher.setPassword(SHA256Helper.hash("123456"));
        resetPasswordTeacher.setUpdateAt(Instant.now());
        resetPasswordTeacher = teacherRepository.save(resetPasswordTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", resetPasswordTeacher);
    }

    @Override
    public ResponseObject getTeacherById(String teacherId) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        if (foundTeacher.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy teacher", null);
        return new ResponseObject(HttpStatus.OK, "Success", foundTeacher.get());
    }

    @Override
    public ResponseObject getTeachersOwnClass(String classId) {
        List<Teacher> teachers = teacherRepository.getTeachersOwnClass(classId);
        return new ResponseObject(HttpStatus.OK, "Success", teachers);
    }

    @Override
    public ResponseObject searchTeachersOwnClassById(String classId, String keyword) {
        List<Teacher> teachers = teacherRepository.searchTeachersOwnClassById(classId, keyword);
        return new ResponseObject(HttpStatus.OK, "Success", teachers);
    }

    @Override
    public ResponseObject getTeachersNotOwnClass(String classId, int page) {
        if (page <= 0)
            page = 1;
        Page<Teacher> teachers = teacherRepository.getTeachersNotOwnClass(classId, PageRequest.of(page - 1, 10));
        int totalPage = teachers.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", teachers.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }

    @Override
    public ResponseObject searchTeachersNotOwnClassByKeyword(String classId, String keyword, int page) {
        if (page <= 0)
            page = 1;
        Page<Teacher> teachers = teacherRepository.searchTeachersNotOwnClassByKeyword(classId, "%" + keyword + "%", PageRequest.of(page - 1, 10));
        int totalPage = teachers.getTotalPages();
        if (page > totalPage)
            page = totalPage;
        Map<String, Object> data = new HashMap<>();
        data.put("data", teachers.getContent());
        data.put("currentPage", page);
        data.put("totalPages", totalPage);
        return new ResponseObject(HttpStatus.OK, "Success", data);
    }
}
