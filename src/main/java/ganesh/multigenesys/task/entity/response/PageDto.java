package ganesh.multigenesys.task.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PageDto {
    private Object dataSet;
    private int totalPages;
    private Long totalElements;
    private int number;

    }
