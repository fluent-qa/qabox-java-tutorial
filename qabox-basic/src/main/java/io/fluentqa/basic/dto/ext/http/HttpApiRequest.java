package io.fluentqa.basic.dto.ext.http;

import io.fluentqa.basic.dto.RequestCommand;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HttpApiRequest extends RequestCommand {
    private String url;
    private List<Map<String,String>> headers;

    private String method;
    private String requestBody;
}
