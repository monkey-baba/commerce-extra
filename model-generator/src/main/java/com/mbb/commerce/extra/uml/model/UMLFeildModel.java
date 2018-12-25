package com.mbb.commerce.extra.uml.model;

public class UMLFeildModel extends UMLTypeModel {

    private boolean isTransient;

    private boolean isDbColumn;

    public boolean isTransient() {
        return isTransient;
    }

    public void setTransient(boolean aTransient) {
        isTransient = aTransient;
    }

    public boolean isDbColumn() {
        return isDbColumn;
    }

    public void setDbColumn(boolean dbColumn) {
        isDbColumn = dbColumn;
    }
}
