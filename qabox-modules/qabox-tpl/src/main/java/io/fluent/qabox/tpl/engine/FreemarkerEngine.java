package io.fluent.qabox.tpl.engine;

import freemarker.template.Configuration;
import io.fluent.qabox.frontend.operation.Tpl;
import lombok.SneakyThrows;


import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;


public class FreemarkerEngine extends EngineTemplate<Configuration> {

    @Override
    public Tpl.Engine engine() {
        return Tpl.Engine.FreeMarker;
    }

    @Override
    public Configuration init() {
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_29);
        freemarkerConfig.setDefaultEncoding(StandardCharsets.UTF_8.name());
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/");
        return freemarkerConfig;
    }

    @SneakyThrows
    @Override
    public void render(Configuration configuration, String filePath, Map<String, Object> bindingMap, Writer out) {
        configuration.getTemplate(filePath).process(bindingMap, out);
    }

}
