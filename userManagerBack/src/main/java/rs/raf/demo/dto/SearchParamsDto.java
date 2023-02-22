package rs.raf.demo.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SearchParamsDto {


    private String name;

    private String status;

    private Long dateFrom;

    private Long dateTo;
}
