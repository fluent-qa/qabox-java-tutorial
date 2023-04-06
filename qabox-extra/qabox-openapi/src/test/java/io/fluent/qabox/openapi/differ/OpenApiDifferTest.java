package io.fluent.qabox.openapi.differ;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.openapitools.openapidiff.core.OpenApiCompare;
import org.openapitools.openapidiff.core.model.ChangedOpenApi;
import org.openapitools.openapidiff.core.output.HtmlRender;
import org.openapitools.openapidiff.core.output.JsonRender;
import org.openapitools.openapidiff.core.output.MarkdownRender;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;



public class OpenApiDifferTest {

    @Test
    public void testOpenAPIDifference() throws IOException {
        String originPetStore = "./petstore_v3.yml";
        String newPetStore = "./petstore_v2.json";

        ChangedOpenApi diff = OpenApiCompare.fromLocations(originPetStore,newPetStore);
        System.out.println(diff);

        //write to html
        String html = new HtmlRender("Changelog",
                "http://deepoove.com/swagger-diff/stylesheets/demo.css")
                .render(diff);
        FileUtils.writeStringToFile(new File("apiDifference.html"),html,
                Charset.defaultCharset());
        String markdownRender = new MarkdownRender().render(diff);
        FileUtils.writeStringToFile(new File("apiDifference.md"),markdownRender,
                Charset.defaultCharset());

        String jsonDiff = new JsonRender().render(diff);
        FileUtils.writeStringToFile(new File("./docs/apiDifference.json"),jsonDiff,
                Charset.defaultCharset());
    }
}