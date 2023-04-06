package io.fluent.qabox.util;

import io.fluent.qabox.config.constant.BoxConst;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;


@Slf4j
public class ProjectUtil {

    private static final String LOADED_EXT = ".loaded";

    /**
     * @param projectName 标识名
     * @param first       bool回调，表示函数是否为第一次调用
     */
    @SneakyThrows
    public void projectStartLoaded(String projectName, Consumer<Boolean> first) {
        String userDir = System.getProperty("user.dir");
        File dirFile = new File(userDir, BoxConst.BOX_DIR);
        String warnTxt = " The erupt initialization ID file could not be created";
        if (!dirFile.exists() && !dirFile.mkdirs()) {
            log.warn(dirFile + warnTxt);
        }
        File file = new File(dirFile.getPath(), projectName + LOADED_EXT);
        if (file.exists()) {
            first.accept(false);
        } else {
            first.accept(true);
            if (file.createNewFile()) {
                FileUtils.writeStringToFile(file, BoxInformation.getEruptVersion(), StandardCharsets.UTF_8.name());
            } else {
                log.warn(dirFile + warnTxt);
            }
        }
    }

}
