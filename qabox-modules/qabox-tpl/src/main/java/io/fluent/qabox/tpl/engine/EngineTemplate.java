package io.fluent.qabox.tpl.engine;


import io.fluent.qabox.frontend.operation.Tpl;

import java.io.Writer;
import java.util.Map;


public abstract class EngineTemplate<Engine> {

    private Engine engine;

    public abstract Tpl.Engine engine();

    public abstract Engine init();

    public abstract void render(Engine engine, String filePath, Map<String, Object> bindingMap, Writer out);

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
