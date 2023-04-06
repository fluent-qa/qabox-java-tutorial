package io.fluent.qabox.tpl.engine;

import io.fluent.qabox.frontend.operation.Tpl;
import lombok.SneakyThrows;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;


import java.io.Writer;
import java.util.Map;


public class ThymeleafEngine extends EngineTemplate<TemplateEngine> {

    @Override
    public Tpl.Engine engine() {
        return Tpl.Engine.Thymeleaf;
    }

    @Override
    public TemplateEngine init() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setCacheable(false);
        resolver.setPrefix("/");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCheckExistence(true);
        resolver.setUseDecoupledLogic(true);
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(resolver);
        return templateEngine;
    }

    @SneakyThrows
    @Override
    public void render(TemplateEngine templateEngine, String filePath, Map<String, Object> bindingMap, Writer out) {
        Context ctx = new Context();
        ctx.setVariables(bindingMap);
        out.write(templateEngine.process(filePath, ctx));
    }
}
