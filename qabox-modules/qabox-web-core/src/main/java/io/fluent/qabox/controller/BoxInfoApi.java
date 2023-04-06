package io.fluent.qabox.controller;

import io.fluent.qabox.config.constant.BoxRestPath;
import io.fluent.qabox.config.prop.AppProp;
import io.fluent.qabox.util.BoxInformation;
import io.fluent.qabox.util.misc.MD5Util;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;



@RestController
@RequestMapping(BoxRestPath.ERUPT_API)
@RequiredArgsConstructor
public class BoxInfoApi {

    private final AppProp appProp;

    @GetMapping("/version")
    public String version() {
        return BoxInformation.getEruptVersion();
    }

    @GetMapping("/erupt-app")
    public AppProp eruptApp() {
        appProp.setHash(this.hashCode());
        appProp.setVersion(BoxInformation.getEruptVersion());
        return appProp;
    }

    @GetMapping(value = "/erupt-machine-code")
    public String eruptMachineCode() {
        //CPU序列号 + 操作系统序列号 + 硬件UUID
        HardwareAbstractionLayer hal = new SystemInfo().getHardware();
        return MD5Util.digest(hal.getProcessor().getProcessorIdentifier().getProcessorID()
                + hal.getComputerSystem().getSerialNumber()
                + hal.getComputerSystem().getHardwareUUID());
    }

}
