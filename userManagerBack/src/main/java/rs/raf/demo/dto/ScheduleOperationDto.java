package rs.raf.demo.dto;

import lombok.Data;

@Data
public class ScheduleOperationDto {
    private long id;

    private long date;

    private String operation;
}
