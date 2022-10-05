package ptithcm.onlinejudge.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ptithcm.onlinejudge.dto.TeacherDTO;
import ptithcm.onlinejudge.helper.SHA256Helper;
import ptithcm.onlinejudge.mapper.TeacherMapper;
import ptithcm.onlinejudge.model.entity.Role;
import ptithcm.onlinejudge.model.entity.Teacher;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.RoleRepository;
import ptithcm.onlinejudge.repository.TeacherRepository;

import java.time.Instant;
import java.util.List;
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
    public ResponseObject getAllTeachersExceptAdmin() {
        List<Teacher> teachers = teacherRepository.getAllTeacherExceptAdmin();
        return new ResponseObject(HttpStatus.OK, "Success", teachers);
    }

    @Override
    public ResponseObject searchTeachersByKeyWordExceptAdmin(String keyword) {
        List<Teacher> teachers = teacherRepository.searchTeachersByKeywordExceptAdmin(keyword);
        return new ResponseObject(HttpStatus.OK, "Success", teachers);
    }

    @Override
    public ResponseObject addTeacher(TeacherDTO teacherDTO) {
        String id = teacherDTO.getTeacherId();
        String fName = teacherDTO.getTeacherFirstName();
        String lName = teacherDTO.getTeacherLastName();
        Optional<Teacher> foundTeacher = teacherRepository.findById(id);
        if (foundTeacher.isPresent())
            return new ResponseObject(HttpStatus.FOUND, "Tồn tại teacher", null);
        if (fName == null || fName.isEmpty() || lName == null || lName.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Họ và tên không được để trống", null);
        Optional<Role> foundRole = roleRepository.findById((byte) 2);
        if (foundRole.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Lỗi server", null);
        Teacher teacher = teacherMapper.dtoToEntity(teacherDTO);
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
    public ResponseObject editTeacher(String teacherId, TeacherDTO teacherDTO) {
        String newFName = teacherDTO.getTeacherFirstName();
        String newLName = teacherDTO.getTeacherLastName();
        if (newFName == null || newFName.isEmpty() || newLName == null || newLName.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Họ và tên không được để trống", null);
        Optional<Teacher> foundOldTeacher = teacherRepository.findById(teacherId);
        if (foundOldTeacher.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy teacher cũ", null);
        Teacher newTeacher = foundOldTeacher.get();
        newTeacher.setTeacherFirstName(newFName);
        newTeacher.setTeacherLastName(newLName);
        newTeacher.setUpdateAt(Instant.now());
        newTeacher = teacherRepository.save(newTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", newTeacher);
    }

    // lock
    @Override
    public ResponseObject lockTeacher(String teacherId) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        if (foundTeacher.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy teacher", null);
        Teacher lockedTeacher = foundTeacher.get();
        lockedTeacher.setActive((byte) 0);
        lockedTeacher.setUpdateAt(Instant.now());
        lockedTeacher = teacherRepository.save(lockedTeacher);
        return new ResponseObject(HttpStatus.OK, "Success", lockedTeacher);
    }

    // unlock
    @Override
    public ResponseObject unlockTeacher(String teacherId) {
        Optional<Teacher> foundTeacher = teacherRepository.findById(teacherId);
        if (foundTeacher.isEmpty())
            return new ResponseObject(HttpStatus.FOUND, "Không tìm thấy teacher", null);
        Teacher unlockedTeacher = foundTeacher.get();
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
}
