package com.mbb.commerce.extra.uml.generator;

import com.mbb.commerce.extra.uml.model.UMLClassModel;
import com.mbb.commerce.extra.uml.model.UMLFeildModel;

import java.util.List;

public class JPAModelGenerator extends UMLModelGenerator{

    public JPAModelGenerator() {}

    public JPAModelGenerator(List<UMLClassModel> classModels) {
        this.classModels = classModels;
    }

    protected String getPersistenceProviderImports(UMLClassModel classModel) {
        return "import javax.persistence.*;\n\n";
    }

    protected void parseEntityAnnotation(UMLClassModel classModel, StringBuffer sb) {
        sb.append("@Table(name = \"" + classModel.getAnnotation() + "\")");
    }

    protected void parseFeild(UMLFeildModel feildModel, StringBuffer sbFeilds) {
        if (feildModel.isTransient()) {
            sbFeilds.append("\t@Transient\n");
        } else {
            sbFeilds.append("\t@Column(name = \"" + feildModel.getDocumentation() + "\")\n");
        }
        sbFeilds.append("\tprivate " + feildModel.getType() + " " + feildModel.getName() + ";\r\n\n");

        if (feildModel.isDbColumn()) {
            sbFeilds.append("\t@Column(name = \"" + feildModel.getDocumentation() + "\")\n");
            sbFeilds.append("\tprivate Long "  + feildModel.getName() + "_id;\r\n\n");
        }
    }
}
