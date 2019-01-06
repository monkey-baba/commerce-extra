package com.mbb.commerce.extra.svcgen;

import com.google.common.collect.Maps;
import com.mbb.commerce.extra.svcgen.service.GeneratorService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.util.StringUtils;

@SpringBootApplication
@ShellComponent
public class Generator {

    private static final Map<String, String> CONFIG = new HashMap<String, String>() {{
        put("1", "nacos");
        put("2", "apollo");
        put("3", "config");
    }};
    private static final Set<String> CONFIG_TYPE = CONFIG.keySet();
    private static final Map<String, String> DATABASE = new HashMap<String, String>() {{
        put("1", "mysql");
        put("2", "h2");
        put("3", "postgresql");
        put("4", "sqlserver");
    }};
    private static final Set<String> DATABASE_TYPE = DATABASE.keySet();
    private static final Map<String, Boolean> HYSTRIX = new HashMap<String, Boolean>() {{
        put("1", true);
        put("2", false);
    }};
    private static final Set<String> HYSTRIX_TYPE = HYSTRIX.keySet();
    private static final Map<String, String> ORM = new HashMap<String, String>() {{
        put("1", "mapper");
        put("2", "mybatis-plus");
        put("3", "jpa");
    }};
    private static final Set<String> ORM_TYPE = DATABASE.keySet();
    private static final Map<String, String> PLATFORM = new HashMap<String, String>() {{
        put("1", "cloud");
        put("2", "dubbo");
    }};
    private static final Set<String> PLATFORM_TYPE = PLATFORM.keySet();
    private static final Map<String, String> REGISTRY = new HashMap<String, String>() {{
        put("1", "nacos");
        put("2", "consul");
        put("3", "eureka");
        put("4", "zookeeper");
    }};
    private static final Set<String> REGISTRY_TYPE = REGISTRY.keySet();
    private static final Map<String, Boolean> RIBBON = new HashMap<String, Boolean>() {{
        put("1", true);
        put("2", false);
    }};
    private static final Set<String> RIBBON_TYPE = RIBBON.keySet();
    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private Terminal terminal;


    private BufferedWriter writer;

    public static void main(String[] args) {
        SpringApplication.run(Generator.class, args);
    }


    @ShellMethod("Generate Custom Service")
    public String svcGen() throws IOException {
        InputStream input = terminal.input();
        OutputStream output = terminal.output();

        ApplicationHome home = new ApplicationHome(this.getClass());

        writer = new BufferedWriter(new OutputStreamWriter(output));

        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        printMessage("请输入服务名(eg:test)");
        // 服务名
        String name;
        while (StringUtils.isEmpty(name = reader.readLine())) {
            printMessage("请输入服务名(eg:test)");
        }
        name = name.replaceAll("-service", "");

        printMessage("请输入组织名(eg:com.mbb)");
        // 组织名
        String group;
        while (StringUtils.isEmpty(group = reader.readLine())) {
            printMessage("请输入组织名(eg:com.mbb.test)");
        }

        printMessage("请选择微服务平台 (1.Spring Cloud 2.Dubbo)");
        //平台
        String platform;
        while (StringUtils.isEmpty(platform = reader.readLine()) || !PLATFORM_TYPE
                .contains(platform)) {
            printMessage("请选择微服务平台 (1.Spring Cloud 2.Dubbo)");
        }

        printMessage("请选择数据库 (1.MySQL 2.H2 3.POSTGRESQL 4.SQLSERVER)");
        //数据库
        String database;
        while (StringUtils.isEmpty(database = reader.readLine()) || !DATABASE_TYPE
                .contains(database)) {
            printMessage("请选择数据库 (1.MySQL 2.H2 3.POSTGRESQL 4.SQLSERVER)");
        }

        printMessage("请选择持久层框架 (1.tk.mapper 2.Mybatis-Plus 3.JPA )");
        //持久层框架
        String orm;
        while (StringUtils.isEmpty(orm = reader.readLine()) || !ORM_TYPE
                .contains(orm)) {
            printMessage("请选择持久层框架 (1.tk.mapper 2.Mybatis-Plus 3.JPA )");
        }

        //Spring Cloud选项
        String registry = null;
        String ribbon = null;
        String hystrix = null;
        String config = null;
        if ("1".equals(platform)) {

            printMessage("请选择注册中心 (1.Nacos 2.Consul 3.Eureka 4.Zookeeper)");
            //注册中心选择
            while (StringUtils.isEmpty(registry = reader.readLine()) || !REGISTRY_TYPE
                    .contains(registry)) {
                printMessage("请选择注册中心 (1.Nacos 2.Consul 3.Eureka 4.Zookeeper)");
            }

            printMessage("是否开启Ribbon (1.是 2.否 )");
            //Ribbon开启
            while (StringUtils.isEmpty(ribbon = reader.readLine()) || !RIBBON_TYPE
                    .contains(ribbon)) {
                printMessage("是否开启Ribbon (1.是 2.否 )");
            }

            printMessage("是否开启Hystrix (1.是 2.否 )");
            //Hystrix开启
            while (StringUtils.isEmpty(hystrix = reader.readLine()) || !HYSTRIX_TYPE
                    .contains(hystrix)) {
                printMessage("是否开启Hystrix (1.是 2.否 )");
            }

            printMessage("选择配置中心 (1.Nacos 2.Apollo 3.Cloud Config )");
            while (StringUtils.isEmpty(config = reader.readLine()) || !CONFIG_TYPE
                    .contains(config)) {
                printMessage("选择配置中心 (1.Nacos 2.Apollo 3.Cloud Config )");
            }

        } else {
//            printMessage("选择配置中心 (1.是 2.否 )");
        }

        HashMap<String, Object> context = Maps.newHashMap();
        context.put("name", name);
        context.put("group", group);
        context.put("database", DATABASE.get(database));
        context.put("orm", ORM.get(orm));
        context.put("platform", PLATFORM.get(platform));
        context.put("ribbon", RIBBON.get(ribbon));
        context.put("hystrix", HYSTRIX.get(hystrix));
        context.put("config", CONFIG.get(config));
        context.put("registry", REGISTRY.get(registry));
        return generatorService.moduleGenerate(context, home.getDir().getAbsolutePath());
    }

    private void printMessage(String content) throws IOException {
        this.writer.write(content);
        this.writer.write("\n");
        this.writer.flush();
    }


}
