package io.fluentqa.codegen.service;

import io.fluentqa.codegen.service.models.*;

public interface GeneratorService {

  String generateEntity(EntityGenerateConfig config, TemplateConfig tplConfig);

  String generateRepo(RepoGenerateConfig config,TemplateConfig tplConfig);
  String generateService(ServiceGenerateConfig config,TemplateConfig tplConfig);

  String generateController(ControllerGenerateConfig config,TemplateConfig tplConfig);
}
