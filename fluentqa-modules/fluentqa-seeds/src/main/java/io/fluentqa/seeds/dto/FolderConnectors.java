package io.fluentqa.seeds.dto;

import lombok.Data;

import java.util.List;

@Data
public class FolderConnectors {
    private Integer id;

    private String name;

    private List<ConnectorItem> connectorItems;
}
