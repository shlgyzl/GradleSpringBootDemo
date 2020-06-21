package com.application;

import com.application.swagger.Swagger2Configuration;
import io.github.swagger2markup.GroupBy;
import io.github.swagger2markup.Language;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.BufferedWriter;
import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {Main.class, Swagger2Configuration.class})
@AutoConfigureRestDocs(outputDir = "build/asciidoc/snippets")
@AutoConfigureMockMvc
public class SwaggerTest {
    private String ip = "http://localhost:8080/v2/api-docs";
    private String staticDocsOutputDir = System.getProperty("user.dir") + File.separator + "build" + File.separator + "swagger";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createSpringFoxSwaggerJson() throws Exception {
        System.getProperties().put("io.springfox.staticdocs.outputDir", staticDocsOutputDir);
        String outputDir = System.getProperty("io.springfox.staticdocs.outputDir");
        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/v2/api-docs")
                .accept(MediaType.APPLICATION_JSON).characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        String swaggerJson = response.getContentAsString();
        Files.createDirectories(Paths.get(outputDir));
        Path path = Paths.get(outputDir, "swagger.json");
        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            writer.write(swaggerJson);
        }
    }


//    /**
//     * 生成AsciiDocs格式文档
//     *
//     * @throws Exception
//     */
//    @org.junit.Test
//    public void generateAsciiDocs() throws Exception {
//        //    输出Ascii格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(ip))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./build/asciidoc/generated"));
//    }
//
//    /**
//     * 生成Markdown格式文档
//     *
//     * @throws Exception
//     */
//    @org.junit.Test
//    public void generateMarkdownDocs() throws Exception {
//        //    输出Markdown格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(ip))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./build/markdown/generated"));
//    }
//
//    /**
//     * 生成Confluence格式文档
//     *
//     * @throws Exception
//     */
//    @org.junit.Test
//    public void generateConfluenceDocs() throws Exception {
//        //    输出Confluence使用的格式
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.CONFLUENCE_MARKUP)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(ip))
//                .withConfig(config)
//                .build()
//                .toFolder(Paths.get("./build/confluence/generated"));
//    }
//
//    /**
//     * 生成AsciiDocs格式文档,并汇总成一个文件
//     *
//     * @throws Exception
//     */
//    @org.junit.Test
//    public void generateAsciiDocsToFile() throws Exception {
//        //    输出Ascii到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(ip))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./build/asciidoc/generated/all"));
//    }
//
//
//    /**
//     * 生成Markdown格式文档,并汇总成一个文件
//     *
//     * @throws Exception
//     */
//    @org.junit.Test
//    public void generateMarkdownDocsToFile() throws Exception {
//        //    输出Markdown到单文件
//        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
//                .withMarkupLanguage(MarkupLanguage.MARKDOWN)
//                .withOutputLanguage(Language.ZH)
//                .withPathsGroupedBy(GroupBy.TAGS)
//                .withGeneratedExamples()
//                .withoutInlineSchema()
//                .build();
//
//        Swagger2MarkupConverter.from(new URL(ip))
//                .withConfig(config)
//                .build()
//                .toFile(Paths.get("./build/markdown/generated/all"));
//    }
}