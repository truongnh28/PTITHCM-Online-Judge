package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.TestCaseDTO;
import ptithcm.onlinejudge.model.entity.Problem;
import ptithcm.onlinejudge.model.entity.TestCase;
import ptithcm.onlinejudge.repository.ProblemRepository;

import java.util.Optional;

@Component
public class TestCaseMapperImpl implements TestCaseMapper {
    @Autowired
    private ProblemRepository problemRepository;

    @Override
    public TestCase dtoToEntity(TestCaseDTO dto) {
        if (dto == null) return null;
        TestCase entity = new TestCase();
        entity.setId(dto.getTestCaseId());
        entity.setTestCaseIn(dto.getTestCaseInput());
        entity.setTestCaseOut(dto.getTestCaseOutput());
        Optional<Problem> foundProblem = problemRepository.findById(dto.getProblemId());
        if (foundProblem.isEmpty())
            return null;
        entity.setProblem(foundProblem.get());
        return entity;
    }

    @Override
    public TestCaseDTO entityToDTO(TestCase entity) {
        if (entity == null) return null;
        TestCaseDTO dto = new TestCaseDTO();
        dto.setTestCaseId(entity.getId());
        dto.setTestCaseInput(entity.getTestCaseIn());
        dto.setTestCaseOutput(entity.getTestCaseOut());
        dto.setProblemId(entity.getProblem().getId());
        return dto;
    }
}
