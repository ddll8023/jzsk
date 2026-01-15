package com.szy;

import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;

public class DocGenerate {
    public static void main(String[] args){
        DocsConfig config = new DocsConfig();
        config.setProjectPath("C:\\Users\\ace\\Desktop\\szy\\src\\main\\java\\com"); // 项目根目录
        config.setProjectName("szy"); // 项目名称
        config.setApiVersion("接口文档V1.0");       // 声明该API的版本
        config.setDocsPath("C:\\Users\\ace\\Desktop\\"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }

}
