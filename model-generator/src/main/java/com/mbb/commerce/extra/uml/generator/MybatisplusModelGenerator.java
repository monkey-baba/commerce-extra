package com.mbb.commerce.extra.uml.generator;

import com.mbb.commerce.extra.uml.model.UMLClassModel;
import com.mbb.commerce.extra.uml.model.UMLFeildModel;

import java.util.List;

public class MybatisplusModelGenerator extends UMLModelGenerator{

    public MybatisplusModelGenerator() {}

    public MybatisplusModelGenerator(List<UMLClassModel> classModels) {
        this.classModels = classModels;
    }

    protected String getPersistenceProviderImports(UMLClassModel classModel) {
        return "import com.baomidou.mybatisplus.annotations.*;\n\n";
    }

    protected void parseEntityAnnotation(UMLClassModel classModel, StringBuffer sb) {
        sb.append("@TableName(\"" + classModel.getAnnotation() + "\")");
    }

    protected void parseFeild(UMLFeildModel feildModel, StringBuffer sbFeilds) {
        if (feildModel.isTransient()) {
            sbFeilds.append("\t@TableField(exist = false)\n");
        } else {
            sbFeilds.append("\t@TableId(\"" + feildModel.getDocumentation() + "\")\n");
        }
        sbFeilds.append("\tprivate " + feildModel.getType() + " " + feildModel.getName() + ";\r\n\n");

        if (feildModel.isDbColumn()) {
            sbFeilds.append("\t@TableId(\"" + feildModel.getDocumentation() + "\")\n");
            sbFeilds.append("\tprivate Long "  + feildModel.getName() + "_id;\r\n\n");
        }
    }
}
