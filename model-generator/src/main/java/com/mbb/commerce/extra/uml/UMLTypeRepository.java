package com.mbb.commerce.extra.uml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mbb.commerce.extra.uml.model.UMLTypeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class UMLTypeRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UMLTypeRepository.class);

    public static void initUMLRepository(String strJson) {
        JSONObject root = JSON.parseObject(strJson);
        loadUMLTypeObj(root);
    }

    public static JSONObject loadUMLTypeObj(JSONObject jo) {
        addTypeToRepository(jo);
        String _type = jo.getString("_type");

        if ("UMLClass".equalsIgnoreCase(_type)) {
            return jo;
        } else {
            if ("Project".equalsIgnoreCase(_type) || "UMLModel".equalsIgnoreCase(_type) || "UMLPackage".equalsIgnoreCase(_type)) {
                JSONArray jsonArray = jo.getJSONArray("ownedElements");
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject subJo = jsonArray.getJSONObject(i);
                    loadUMLTypeObj(subJo);
                }
            }
        }
        return null;
    }

    public static void addTypeToRepository(JSONObject jo) {
        String id = jo.getString("_id");
        String type = jo.getString("_type");
        String name = jo.getString("name");
        String parent = null;

        if (!("UMLPackage".equalsIgnoreCase(type) || "UMLClass".equalsIgnoreCase(type))) {
            return;
        }

        JSONObject jParent = jo.getJSONObject("_parent");
        if (null != jParent) {
            parent = jParent.getString("ref");
        }

        UMLTypeModel typeModel = new UMLTypeModel(type, id, parent, name);
        if (null != UMLTypeRepository.getTypeById(typeModel.getParent())) {
            typeModel.setPackageName(UMLTypeRepository.getTypeById(typeModel.getParent()).getPackageName() +  ("UMLPackage".equalsIgnoreCase(typeModel.getType()) ? "." + typeModel.getName() : ""));
        } else {
            typeModel.setPackageName(typeModel.getName());
        }

        UMLTypeRepository.addType(id, typeModel);
        LOGGER.info("Initialized Type : " + typeModel.toString());
    }



    public static Map<String, UMLTypeModel> repository = new HashMap<String, UMLTypeModel>();

    public static UMLTypeModel getTypeById(String id) {
        return repository.get(id);
    }

    public static void addType(String id, UMLTypeModel typeModel) {
        repository.put(id, typeModel);
    }

}
