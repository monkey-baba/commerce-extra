package com.mbb.commerce.extra.uml.model;


import java.util.HashMap;
import java.util.List;

public class UMLClassModel extends UMLTypeModel {

    private HashMap<String, String> imports;
    private List<UMLFeildModel> feilds;

    public UMLClassModel() {}

    public UMLClassModel(String name, String documentation) {
        super(name, documentation);
    }

    public HashMap<String, String> getImports() {
        return imports;
    }

    public void setImports(HashMap<String, String> imports) {
        this.imports = imports;
    }

    public List<UMLFeildModel> getFeilds() {
        return feilds;
    }

    public void setFeilds(List<UMLFeildModel> feilds) {
        this.feilds = feilds;
    }

    @Override
    public String toString() {
        return "UMLClassModel{" +
                "imports=" + imports +
                ", feilds=" + feilds +
                '}';
    }
}
