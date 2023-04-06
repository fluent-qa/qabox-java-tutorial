package io.fluent.qabox.view;

import io.fluent.qabox.fun.PermissionObject;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
public class BoxBuildModel {

    private BoxModel eruptModel;

    private Map<String, BoxBuildModel> tabErupts;

    private Map<String, BoxModel> combineErupts;

    private Map<String, BoxModel> operationErupts;

    private PermissionObject power;
}
