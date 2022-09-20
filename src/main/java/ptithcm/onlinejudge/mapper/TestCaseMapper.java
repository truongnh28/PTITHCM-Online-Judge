package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.TestCaseDTO;
import ptithcm.onlinejudge.model.entity.TestCase;

public interface TestCaseMapper {
    TestCase dtoToEntity(TestCaseDTO dto);
    TestCaseDTO entityToDTO(TestCase entity);
}
