package com.mbb.commerce.extra.uml;

import com.mbb.commerce.extra.uml.generator.UMLModelGenerator;
import com.mbb.commerce.extra.uml.generator.UMLModelGeneratorFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class UMLConfig {

    private String baseModel;
    public static String GENSRC_PATH = "/commerce-extra/model-generator/src/main/gensrc";
    public static String UML_DEFINITION_FILE = "modelgen/udf.mdj";
    public static String ID_SUFFIX = "_id";
    public static String ID_TYPE = "Long";
    public static boolean F_UTIL = true; // 是否需要导入包java.util.*
    public static boolean F_SQL = false; // 是否需要导入包java.sql.*

    public static String UML_MODEL_TYPE_JAP = "jpa";
    public static String UML_MODEL_TYPE_MAPPER = "mapper";
    public static String UML_MODEL_TYPE_MYBATISPLUS = "mybatis-plus";


    public static String getCopyrightRange() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String range = year + "-" + (year + 1);
        return range;
    }

    public static String getGeneratedDate() {
        return new Date().toString();
    }


    /**
     * 出口
     * TODO
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {

        String jsonText = UMLTranslater.getInstance().getJsonTextFromUDF(UMLConfig.UML_DEFINITION_FILE);
        UMLTypeRepository.initUMLRepository(jsonText);

        UMLModelGenerator modelGenerator = UMLModelGeneratorFactory.getInstance().create(UMLConfig.UML_MODEL_TYPE_MAPPER);
//        UMLModelGenerator modelGenerator = UMLModelGeneratorFactory.getInstance().create(UMLConfig.UML_MODEL_TYPE_MYBATISPLUS);
        modelGenerator.setClassModels(UMLTranslater.getInstance().extractClassModel(jsonText));
        modelGenerator.execute();


    }

}
