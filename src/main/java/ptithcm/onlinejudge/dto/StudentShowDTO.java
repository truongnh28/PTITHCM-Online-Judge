package ptithcm.onlinejudge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentShowDTO extends StudentDTO {
    private boolean disabledButtonAdding;
    private boolean disabledButtonRemoving;
}
