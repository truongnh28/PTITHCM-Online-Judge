package ptithcm.onlinejudge.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ptithcm.onlinejudge.model.response.ResponseObject;
import ptithcm.onlinejudge.repository.ProblemRepository;

@RestController
@RequestMapping("/teacher/problem")
public class TeacherProblemBackendController {
    @Autowired
    private ProblemRepository problemRepository;

    @GetMapping("/{problemId}")
    public ResponseObject getOne(@PathVariable("problemId") String problemId) {
        if (!problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Mã bài tập không tồn tại! Vui lòng kiểm tra lại", null);
        return new ResponseObject(HttpStatus.OK, "Success", problemRepository.findById(problemId));
    }

    @PostMapping("/{problemId}")
    public ResponseObject checkUnique(@PathVariable("problemId") String problemId) {
        if (problemRepository.existsById(problemId))
            return new ResponseObject(HttpStatus.FOUND, "Mã bài tập đã tồn tại! Vui lòng chọn một mã bài tập khác", null);
        return new ResponseObject(HttpStatus.OK, "Success", null);
    }
}
