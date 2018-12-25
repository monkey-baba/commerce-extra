package com.mbb.commerce.extra.uml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mbb.commerce.extra.uml.model.UMLClassModel;
import com.mbb.commerce.extra.uml.model.UMLFeildModel;
import com.mbb.commerce.extra.uml.model.UMLTypeModel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UMLTranslater {

    private static final Logger LOGGER = LoggerFactory.getLogger(UMLTranslater.class);

    public static UMLTranslater instance = new UMLTranslater();

    public static UMLTranslater getInstance() {
        return instance;
    }


    public String getJsonTextFromUDF(String fileName) throws IOException {
        java.net.URL path = getClass().getClassLoader().getResource(fileName);
        String jContent = FileUtils.readFileToString(new File(path.getFile()), "UTF-8");
        return jContent;
    }

    public List<UMLClassModel> extractClassModel(String strJson) {
        JSONObject root = JSON.parseObject(strJson);

        List<UMLClassModel> classModels = new ArrayList<UMLClassModel>();
        getClassModelJo(root, classModels);

        return classModels;
    }

    public JSONObject getClassModelJo(JSONObject jo, List<UMLClassModel> classModels) {
//        loadUMLTypeObj(jo);
        String _type = jo.getString("_type");

        if ("UMLClass".equalsIgnoreCase(_type)) {
            return jo;
        } else {
            if ("Project".equalsIgnoreCase(_type) || "UMLModel".equalsIgnoreCase(_type) || "UMLPackage".equalsIgnoreCase(_type)) {
                JSONArray jsonArray = jo.getJSONArray("ownedElements");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    JSONObject retJo = getClassModelJo(subJo, classModels);
                    if (null != retJo) {

                        UMLClassModel classModel = new UMLClassModel((String)retJo.get("name"),(String)retJo.get("documentation"));
                        classModels.add(classModel);
                        classModel.setId((String) retJo.get("_id"));
                        classModel.setType((String) retJo.get("_type"));
                        classModel.setVisibility((String) retJo.get("visibility"));
                        classModel.setAnnotation((String)retJo.get("documentation"));
                        classModel.setPackageName(UMLTypeRepository.getTypeById(classModel.getId()).getPackageName());
                        classModel.setImports(new HashMap<String, String>());
                        classModel.setDocumentation(classModel.getName().substring(0, classModel.getName().indexOf("Model")));
                        if (null != retJo.getBoolean("isAbstract")) {
                            classModel.setAbstract(retJo.getBoolean("isAbstract"));
                        }


                        if (retJo.get("stereotype") instanceof JSONObject) {
                            JSONObject typeJo = (JSONObject) retJo.get("stereotype");
                            String refId = typeJo.getString("ref");
                            UMLTypeModel typeModel = UMLTypeRepository.getTypeById(refId);
                            classModel.setStereotype(typeModel.getName());
                        }

                        JSONArray attrArray = retJo.getJSONArray("attributes");
                        if (null != attrArray && attrArray.size() > 0) {
                            List<UMLFeildModel> feilds = new ArrayList<UMLFeildModel>();
                            classModel.setFeilds(feilds);

                            for (int j = 0; j < attrArray.size(); j++) {

                                String strType = "";
                                JSONObject attrJo = attrArray.getJSONObject(j);

                                UMLFeildModel feildModel = new UMLFeildModel();
                                feilds.add(feildModel);
                                feildModel.setId((String) attrJo.get("_id"));
                                feildModel.setName((String) attrJo.get("name"));
                                feildModel.setDocumentation((String) attrJo.get("documentation"));
                                feildModel.setVisibility((String) attrJo.get("visibility"));
                                feildModel.setMultiplicity((String) attrJo.get("multiplicity"));
                                feildModel.setAnnotation((String)attrJo.get("documentation"));


                                if (attrJo.get("type") instanceof String) {
                                    strType = (String) attrJo.get("type");
                                    feildModel.setType(strType);

                                } else if (attrJo.get("type") instanceof JSONObject) {
                                    feildModel.setTransient(true);
                                    JSONObject typeJo = (JSONObject) attrJo.get("type");
                                    String refId = typeJo.getString("ref");

                                    UMLTypeModel typeModel = UMLTypeRepository.getTypeById(refId);
                                    if ("1".equalsIgnoreCase(feildModel.getMultiplicity())) {
                                        feildModel.setDbColumn(true);
                                        feildModel.setType(typeModel.getName());
                                        classModel.getImports().put(typeModel.getPackageName() + "." + typeModel.getName(), "");
                                    } else if ("1..*".equalsIgnoreCase(feildModel.getMultiplicity())) {
                                        feildModel.setType("List<" + typeModel.getName() + ">");
                                        classModel.getImports().put(typeModel.getPackageName() + "." + typeModel.getName(), "");
                                    }
                                }
                            }
                        }

                        LOGGER.info("Loaded UML Class Model definition : " + classModel.toString());
                    }
                }
            }
        }
        return null;
    }
}
