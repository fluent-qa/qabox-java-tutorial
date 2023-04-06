package io.fluentqa.basic.dto.ext.http;

import io.fluentqa.basic.dto.GenericResponse;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class HttpApiResponse extends GenericResponse {
    private String content;
}
