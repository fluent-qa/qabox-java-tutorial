package io.fluentqa.seeds.dto;

import lombok.Data;

/**
 */
@Data
public class PageOperateResult {
    private Long id;

    private Integer librarySaveStatus;

    private Boolean starred;

    private Boolean readLater;
}
