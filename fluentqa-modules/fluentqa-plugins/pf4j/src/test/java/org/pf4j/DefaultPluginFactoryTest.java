/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.pf4j;

import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;
import org.pf4j.test.JavaFileObjectClassLoader;
import org.pf4j.test.JavaFileObjectUtils;
import org.pf4j.test.JavaSources;
import org.pf4j.test.TestPlugin;

import javax.tools.JavaFileObject;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mario Franco
 */
public class DefaultPluginFactoryTest {

    public static final JavaFileObject FailTestPlugin = JavaFileObjects.forSourceLines("FailTestPlugin",
        "package test;",
        "import org.pf4j.Plugin;",
        "",
        "public class FailTestPlugin {",
        "}");

    public static final JavaFileObject AnotherFailTestPlugin = JavaFileObjects.forSourceLines("AnotherFailTestPlugin",
        "package test;",
        "import org.pf4j.Plugin;",
        "",
        "public class AnotherFailTestPlugin extends Plugin {",
        "     public AnotherFailTestPlugin() { super(null); }",
        "}");

    public static final JavaFileObject AnotherTestPlugin = JavaFileObjects.forSourceLines("AnotherTestPlugin",
        "package test;",
        "import org.pf4j.Plugin;",
        "",
        "public class AnotherTestPlugin extends Plugin {",
        "     public AnotherTestPlugin() { super(); }",
        "}");

    @Test
    public void testCreate() {
        PluginDescriptor pluginDescriptor = mock(PluginDescriptor.class);
        when(pluginDescriptor.getPluginClass()).thenReturn(TestPlugin.class.getName());

        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getDescriptor()).thenReturn(pluginDescriptor);
        when(pluginWrapper.getPluginClassLoader()).thenReturn(getClass().getClassLoader());

        PluginFactory pluginFactory = new DefaultPluginFactory();

        Plugin result = pluginFactory.create(pluginWrapper);
        assertNotNull(result);
        assertThat(result, instanceOf(TestPlugin.class));
    }

    @Test
    public void pluginConstructorNoParameters() {
        PluginDescriptor pluginDescriptor = mock(PluginDescriptor.class);
        JavaFileObject object = JavaSources.compile(AnotherTestPlugin);
        String pluginClassName = JavaFileObjectUtils.getClassName(object);
        when(pluginDescriptor.getPluginClass()).thenReturn(pluginClassName);

        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getDescriptor()).thenReturn(pluginDescriptor);
        JavaFileObjectClassLoader classLoader = new JavaFileObjectClassLoader();
        classLoader.load(AnotherTestPlugin);
        when(pluginWrapper.getPluginClassLoader()).thenReturn(classLoader);

        PluginFactory pluginFactory = new DefaultPluginFactory();

        Plugin result = pluginFactory.create(pluginWrapper);
        assertNotNull(result);
        assertEquals(pluginClassName, result.getClass().getName());
    }

    @Test
    public void testCreateFail() {
        PluginDescriptor pluginDescriptor = mock(PluginDescriptor.class);
        JavaFileObject object = JavaSources.compile(FailTestPlugin);
        String pluginClassName = JavaFileObjectUtils.getClassName(object);
        when(pluginDescriptor.getPluginClass()).thenReturn(pluginClassName);

        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getDescriptor()).thenReturn(pluginDescriptor);
        JavaFileObjectClassLoader classLoader = new JavaFileObjectClassLoader();
        classLoader.load(FailTestPlugin);
        when(pluginWrapper.getPluginClassLoader()).thenReturn(classLoader);

        PluginFactory pluginFactory = new DefaultPluginFactory();

        Plugin plugin = pluginFactory.create(pluginWrapper);
        assertNull(plugin);
    }

    @Test
    public void testCreateFailNotFound() {
        PluginDescriptor pluginDescriptor = mock(PluginDescriptor.class);
        when(pluginDescriptor.getPluginClass()).thenReturn("org.pf4j.plugin.NotFoundTestPlugin");

        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getDescriptor()).thenReturn(pluginDescriptor);
        when(pluginWrapper.getPluginClassLoader()).thenReturn(getClass().getClassLoader());

        PluginFactory pluginFactory = new DefaultPluginFactory();

        Plugin plugin = pluginFactory.create(pluginWrapper);
        assertNull(plugin);
    }

    @Test
    public void testCreateFailConstructor() {
        PluginDescriptor pluginDescriptor = mock(PluginDescriptor.class);
        JavaFileObject object = JavaSources.compile(AnotherFailTestPlugin);
        String pluginClassName = JavaFileObjectUtils.getClassName(object);
        when(pluginDescriptor.getPluginClass()).thenReturn(pluginClassName);

        PluginWrapper pluginWrapper = mock(PluginWrapper.class);
        when(pluginWrapper.getDescriptor()).thenReturn(pluginDescriptor);
        JavaFileObjectClassLoader classLoader = new JavaFileObjectClassLoader();
        classLoader.load(AnotherFailTestPlugin);
        when(pluginWrapper.getPluginClassLoader()).thenReturn(classLoader);

        PluginFactory pluginFactory = new DefaultPluginFactory();

        Plugin plugin = pluginFactory.create(pluginWrapper);
        assertNull(plugin);
    }

}
