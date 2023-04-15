package io.fluentqa.codegen.service;

import cn.hutool.extra.template.TemplateUtil;

import java.util.Map;

public class TemplateService {

  public String renderTemplate(String templateContent, Map<String,Object> data){
     //TemplateUtil.render
    return TemplateUtil.createEngine().getTemplate(templateContent).render(data);
  }
}
