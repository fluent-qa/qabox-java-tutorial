package io.fluentqa.codegen.service;

import io.fluentqa.codegen.service.models.*;
import org.springframework.stereotype.Service;

@Service
public class GeneratorServiceImpl implements GeneratorService{
  @Override
  public String generateEntity(EntityGenerateConfig config, TemplateConfig tplConfig) {
    //Template, according to config to get column information
    //Render the TplLate

    return null;
  }

  @Override
  public String generateRepo(RepoGenerateConfig config, TemplateConfig tplConfig) {
    return null;
  }

  @Override
  public String generateService(ServiceGenerateConfig config, TemplateConfig tplConfig) {
    return null;
  }

  @Override
  public String generateController(ControllerGenerateConfig config, TemplateConfig tplConfig) {
    return null;
  }
}
