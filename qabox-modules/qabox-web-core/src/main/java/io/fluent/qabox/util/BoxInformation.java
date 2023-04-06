package io.fluent.qabox.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BoxInformation {

    private static Properties props;

    static {
        String path = "/box-core.properties";
        Properties props = new Properties();
        try (InputStream stream = BoxInformation.class.getResourceAsStream(path)) {
            props.load(stream);
            BoxInformation.props = props;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEruptVersion() {
        return props.getProperty("version");
    }


}
