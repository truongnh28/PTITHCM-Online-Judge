package ptithcm.onlinejudge.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ptithcm.onlinejudge.dto.TestCaseDTO;
import ptithcm.onlinejudge.model.entity.TestCase;

@Component
public class TestCaseMapperImpl implements TestCaseMapper {
    @Autowired
    private ProblemMapper problemMapper;
    @Override
    public TestCase dtoToEntity(TestCaseDTO dto) {
        if (dto == null) return null;
        TestCase entity = new TestCase();
        entity.setId(dto.getTestCaseId());
        entity.setTestCaseIn(dto.getTestCaseInput());
        entity.setTestCaseOut(dto.getTestCaseOutput());
        entity.setProblem(problemMapper.dtoToEntity(dto.getProblem()));
        return entity;
    }

    @Override
    public TestCaseDTO entityToDTO(TestCase entity) {
        if (entity == null) return null;
        TestCaseDTO dto = new TestCaseDTO();
        dto.setTestCaseId(entity.getId());
        dto.setTestCaseInput(entity.getTestCaseIn());
        dto.setTestCaseOutput(entity.getTestCaseOut());
        dto.setProblem(problemMapper.entityToDTO(entity.getProblem()));
        return dto;
    }
}
