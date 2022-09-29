package ptithcm.onlinejudge.mapper;

import ptithcm.onlinejudge.dto.SubmissionDTO;
import ptithcm.onlinejudge.dto.SubmissionDetailDTO;
import ptithcm.onlinejudge.model.entity.Submission;

public interface SubmissionMapper {
    SubmissionDetailDTO entityToDetailDTO(Submission entity);
    SubmissionDTO entityToDTO(Submission entity);
    Submission dtoToEntity(SubmissionDTO dto);
}
