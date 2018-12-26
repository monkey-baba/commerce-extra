package com.mbb.commerce.extra.uml.generator;

import com.mbb.commerce.extra.uml.UMLConfig;
import com.mbb.commerce.extra.uml.model.UMLClassModel;
import com.mbb.commerce.extra.uml.model.UMLFeildModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public abstract class UMLModelGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(UMLModelGenerator.class);

    protected List<UMLClassModel> classModels;


    public UMLModelGenerator() {

    }

    public UMLModelGenerator(List<UMLClassModel> classModels) {
        this.classModels = classModels;
    }

    public List<UMLClassModel> getClassModels() {
        return classModels;
    }

    public void setClassModels(List<UMLClassModel> classModels) {
        this.classModels = classModels;
    }

    /**
     * 功能：将输入字符串的首字母改成大写
     *
     * @param str
     * @return
     */
    protected String initcap(String str) {

        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }

        return new String(ch);
    }

    protected void parseFileComment(UMLClassModel classModel, StringBuffer sb) {
        String comment = "/*\n" +
                " * ----------------------------------------------------------------\n" +
                " * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---\n" +
                " * --- Generated at " + UMLConfig.getGeneratedDate() + "                ---\n" +
                " * ----------------------------------------------------------------\n" +
                " *\n" +
                " * [MBB] Commerce Platform\n" +
                " *\n" +
                " * Copyright (c) " + UMLConfig.getCopyrightRange() + " MBB\n" +
                " * All rights reserved.\n" +
                " *\n" +
                " * This software is the confidential and proprietary information of MBB\n" +
                " * Commerce Platform (\"Confidential Information\"). You shall not disclose such\n" +
                " * Confidential Information and shall use it only in accordance with the\n" +
                " * terms of the license agreement you entered into with MBB Commerce Platform.\n" +
                " *\n" +
                " */\n";
        sb.append(comment);
    }

    protected void parsePackage(UMLClassModel classModel, StringBuffer sb) {
        String packageStr = "package " + classModel.getPackageName() + ";\n";
        sb.append(packageStr);
        sb.append("\r\n");
    }

    protected void parseImport(UMLClassModel classModel, StringBuffer sb) {
        //判断是否导入工具包
        if (UMLConfig.F_UTIL) {
            sb.append("import java.util.*;\r\n");
        }
        if (UMLConfig.F_SQL) {
            sb.append("import java.sql.*;\r\n");
        }

        sb.append(getPersistenceProviderImports(classModel));

        if (null != classModel.getImports() && classModel.getImports().size() > 0) {
            for (String strImport : classModel.getImports().keySet()) {
                sb.append("import " + strImport + ";\r\n");
            }
        }

        if (StringUtils.isNotBlank(classModel.getStereotype())) {
            sb.append("import " + classModel.getPackageName() +  "." + classModel.getStereotype() + ";\n");
        }

        sb.append("\n");
    }

    protected void parseClassComment(UMLClassModel classModel, StringBuffer sb) {
        String comment = "/**\n" +
                "* @Description Generated model class for type " + classModel.getDocumentation() + "\n" +
                "*/\n";
        sb.append(comment);
    }

    protected abstract String getPersistenceProviderImports(UMLClassModel classModel);

    protected abstract void parseEntityAnnotation(UMLClassModel classModel, StringBuffer sb);

    protected void parseClassHeader(UMLClassModel classModel, StringBuffer sb) {
        sb.append("\r\npublic class " + classModel.getName());
        if (StringUtils.isNotBlank(classModel.getStereotype())) {
            sb.append(" extends " + classModel.getStereotype());
        }
    }

    protected void parseClassBody(UMLClassModel classModel, StringBuffer sb) {
        sb.append(" {\r\n\n");
        parseFeildsAndMethods(classModel, sb);
        sb.append("}\r\n");
    }

    protected abstract void parseFeild(UMLFeildModel feildModel, StringBuffer sbFeilds);

    protected void parseMethod(UMLClassModel classModel, UMLFeildModel feildModel, StringBuffer sbMethods) {
        String comment = "\t/**\n" +
                "\t * <i>Generated method</i> - Getter of the <code>" + classModel.getDocumentation() + "." + feildModel.getName() + "</code>.\n" +
                "\t */\n";
        sbMethods.append(comment);

        sbMethods.append("\tpublic void set" + initcap(feildModel.getName()) + "(" + feildModel.getType() + " " + feildModel.getName() + ") {\r\n");
        sbMethods.append("\t\tthis." + feildModel.getName() + " = " + feildModel.getName() + ";\r\n");
        sbMethods.append("\t}\r\n\n");

        comment = "\t/**\n" +
                "\t * <i>Generated method</i> - Getter of the <code>" + classModel.getDocumentation() + "." + feildModel.getName() + "</code>.\n" +
                "\t * @return the " + feildModel.getName() + " - " + feildModel.getName() + ".\n" +
                "\t */\n";
        sbMethods.append(comment);

        sbMethods.append("\tpublic " + feildModel.getType() + " get" + initcap(feildModel.getName() + "() {\r\n"));
        sbMethods.append("\t\treturn " + feildModel.getName() + ";\r\n");
        sbMethods.append("\t}\r\n");

        if (feildModel.isDbColumn()) {
            String feildName = feildModel.getName() + UMLConfig.ID_SUFFIX;
            String feildType = UMLConfig.ID_TYPE;

            comment = "\t/**\n" +
                    "\t * <i>Generated method</i> - Getter of the <code>" + classModel.getDocumentation() + "." + feildName + "</code>.\n" +
                    "\t */\n";
            sbMethods.append(comment);

            sbMethods.append("\tpublic void set" + initcap(feildName) + "(" + feildType + " " + feildName + ") {\r\n");
            sbMethods.append("\t\tthis." + feildName + " = " + feildName + ";\r\n");
            sbMethods.append("\t}\r\n\n");

            comment = "\t/**\n" +
                    "\t * <i>Generated method</i> - Getter of the <code>" + classModel.getDocumentation() + "." + feildName + "</code>.\n" +
                    "\t * @return the " + feildName + " - " + feildName + ".\n" +
                    "\t */\n";
            sbMethods.append(comment);

            sbMethods.append("\tpublic " + feildType + " get" + initcap(feildName + "() {\r\n"));
            sbMethods.append("\t\treturn " + feildName + ";\r\n");
            sbMethods.append("\t}\r\n");
        }
    }

    protected void parseFeildsAndMethods(UMLClassModel classModel, StringBuffer sb) {
        if (CollectionUtils.isNotEmpty(classModel.getFeilds())) {
            StringBuffer sbFeilds = new StringBuffer();
            StringBuffer sbMethods = new StringBuffer();
            for (UMLFeildModel feildModel : classModel.getFeilds()) {

                parseFeild(feildModel, sbFeilds);
                parseMethod(classModel, feildModel, sbMethods);
            }
            sb.append(sbFeilds);
            sb.append(sbMethods);
        }
    }


    protected String parseClass(UMLClassModel classModel) {
        StringBuffer sb = new StringBuffer();

        parseFileComment(classModel, sb);
        parsePackage(classModel, sb);
        parseImport(classModel, sb);
        parseClassComment(classModel, sb);
        parseEntityAnnotation(classModel, sb);
        parseClassHeader(classModel, sb);
        parseClassBody(classModel, sb);

//        LOGGER.info(sb.toString());
        return sb.toString();
    }

    protected String packageName2Path(String packageName) {
        if (StringUtils.isNoneBlank(packageName)) {
            StringBuffer sb = new StringBuffer();
            String[] array = StringUtils.split(packageName, ".");
            if (null != array & array.length > 0) {
                for (String name : array) {
                    sb.append("/").append(name);
                }
            }
            return sb.toString();
        }
        return "";
    }

    public void generateFile(String content, UMLClassModel classModel) {
        try {
            String userDir = System.getProperty("user.dir");
//            userDir = userDir + "/commerce-extra/model-generator";

            String path = userDir + UMLConfig.GENSRC_PATH + packageName2Path(classModel.getPackageName());
            File dirPath = new File(path);
            if (!dirPath.exists()) {
                dirPath.mkdirs();
                LOGGER.info("Created dir : " + path);
            }

            String outputPath = path + "/" + initcap(classModel.getName()) + ".java";
            FileWriter fw = new FileWriter(outputPath);
            PrintWriter pw = new PrintWriter(fw);
            pw.println(content);
            pw.flush();
            pw.close();
            LOGGER.info("Created 1 file : " + outputPath);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }


    public void execute() {
        if (CollectionUtils.isNotEmpty(this.classModels)) {
            for (UMLClassModel classModel : classModels) {
                String content = parseClass(classModel);
                generateFile(content, classModel);
            }
        }
    }

}
