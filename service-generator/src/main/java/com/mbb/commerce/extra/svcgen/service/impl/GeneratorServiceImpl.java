package com.mbb.commerce.extra.svcgen.service.impl;

import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.JinjavaConfig;
import com.mbb.commerce.extra.svcgen.file.DeleteDirectoryVisitor;
import com.mbb.commerce.extra.svcgen.service.GeneratorService;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class GeneratorServiceImpl implements GeneratorService {


    private static final Pattern MYBATIS = Pattern.compile(".+/src/.+Mapper\\.(xml)$");
    private static final List<Pattern> PATTERNS =
            Arrays.asList(Pattern.compile(".+\\.md$"), Pattern.compile(".+pom.xml$"),
                    Pattern.compile(".+/src/.+\\.(xml|java|yaml)$")
            );
    private static final String TEMPLATE = "demo-service";

    private Jinjava jinjava = new Jinjava(
            JinjavaConfig.newBuilder().withLstripBlocks(true).withTrimBlocks(true).build());


    @Override
    public String moduleGenerate(HashMap<String, Object> context, String path) throws IOException {

        //扫描TEMPLATE路径
        try {
            String templatePath = path + File.separator + TEMPLATE;
            if (Files.notExists(Paths.get(templatePath))) {
                throw new RuntimeException("模板文件夹不存在，请检查:" + templatePath);
            }
            Files.walkFileTree(Paths.get(templatePath), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    String filePath = file.toString();
                    final String originPath = filePath;
                    if (PATTERNS.stream().anyMatch(p -> p.matcher(originPath).matches())) {
                        if ("jpa".equals(context.get("orm"))) {
                            //JPA忽略mapper文件
                            if (MYBATIS.matcher(originPath).matches()) {
                                return FileVisitResult.CONTINUE;
                            }
                        }
                        System.out.println("Origin:" + originPath);
                        String name = context.get("name").toString();
                        if (filePath.contains("src")) {
                            //替换包名
                            filePath = filePath.replaceAll("com/mbb/demo",
                                    (context.get("group").toString()+"."+name).replaceAll("\\.", "/"));
                        }
                        //替换name
                        filePath = filePath
                                .replaceAll("(demo)", name);

                        name=name.substring(0,1).toUpperCase()+name.substring(1);
                        filePath = filePath
                                .replaceAll("(Demo)", name);
                        System.out.println("New:" + filePath);

                        //生成新的文件
                        String render = jinjava
                                .render(new String(Files.readAllBytes(file),
                                        StandardCharsets.UTF_8), context);

                        Path target = Paths.get(filePath);
                        createParentDir(target.getParent());
                        Files.write(target,
                                render.getBytes(StandardCharsets.UTF_8),
                                StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);


                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    return FileVisitResult.TERMINATE;
                }
            });
        } catch (Exception e) {
            Files.walkFileTree(Paths.get(path + context.get("name") + "-service"),
                    new DeleteDirectoryVisitor());
            e.printStackTrace();
            return "生成模板失败，错误:" + e.getMessage();
        }
        return "完成，请添加 <module>"+context.get("name")+"-service</module> 到commerce-service/pom.xml";
    }


    private void createParentDir(Path target) throws IOException {
        if (Files.notExists(target)) {
            createParentDir(target.getParent());
            Files.createDirectory(target);
        }

    }

}
