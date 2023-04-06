package io.fluent.qabox.config.prop;

import io.fluent.qabox.config.HikariCpConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.NestedConfigurationProperty;


@Getter
@Setter
public class DataSourceProp {

    private String name;

    private String driverClassName;

    private String url;

    private String username;

    private String password;

    @NestedConfigurationProperty
    private HikariCpConfig hikari = new HikariCpConfig();

}
