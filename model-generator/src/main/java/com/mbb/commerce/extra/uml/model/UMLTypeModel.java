package com.mbb.commerce.extra.uml.model;

public class UMLTypeModel {
    private String type;
    private String id;
    private String parent;
    private String name;
    private String documentation;
    private String visibility;
    private String multiplicity;
    private String annotation;
    private String packageName;
    private String stereotype;
    private boolean isAbstract;


    public UMLTypeModel() {
    }

    public UMLTypeModel(String name, String documentation) {
        this.name = name;
        this.documentation = documentation;
    }

    public UMLTypeModel(String type, String id, String parent, String name) {
        this.type = type;
        this.id = id;
        this.parent = parent;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getStereotype() {
        return stereotype;
    }

    public void setStereotype(String stereotype) {
        this.stereotype = stereotype;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public String toString() {
        return "UMLTypeModel{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", parent='" + parent + '\'' +
                ", name='" + name + '\'' +
                ", documentation='" + documentation + '\'' +
                ", visibility='" + visibility + '\'' +
                ", multiplicity='" + multiplicity + '\'' +
                ", annotation='" + annotation + '\'' +
                ", packageName='" + packageName + '\'' +
                ", stereotype='" + stereotype + '\'' +
                ", isAbstract=" + isAbstract +
                '}';
    }
}
