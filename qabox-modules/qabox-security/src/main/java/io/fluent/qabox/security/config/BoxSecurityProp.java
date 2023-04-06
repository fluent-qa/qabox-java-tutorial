package io.fluent.qabox.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "box.security", ignoreUnknownFields = false)
public class BoxSecurityProp {

    // 是否记录操作日志
    private boolean recordOperateLog = true;
}
