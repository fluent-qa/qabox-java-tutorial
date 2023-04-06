package io.fluent.qabox.config.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;

@Getter
@Setter
public class DatabaseProp {

    private DataSourceProp datasource;

    private JpaProperties jpa;

    private String[] scanPackages;

}
