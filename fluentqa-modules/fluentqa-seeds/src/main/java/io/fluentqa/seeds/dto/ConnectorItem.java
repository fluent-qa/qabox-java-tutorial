package io.fluentqa.seeds.dto;

import lombok.Data;


@Data
public class ConnectorItem {
    private Integer id;

    private String name;

    private Integer type;

    private String iconUrl;

    private int inboxCount;
}
