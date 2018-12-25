package com.mbb.commerce.extra.uml.generator;

import com.mbb.commerce.extra.uml.UMLConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UMLModelGeneratorFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(UMLModelGeneratorFactory.class);

    private static UMLModelGeneratorFactory factory;

    public static synchronized UMLModelGeneratorFactory getInstance() {
        if (null == factory) {
            factory = new UMLModelGeneratorFactory();
        }
        return factory;
    }

    public UMLModelGenerator create(String generater) {
        if (UMLConfig.UML_MODEL_TYPE_JAP.equalsIgnoreCase(generater)) {
            return new JPAModelGenerator();
        } else if (UMLConfig.UML_MODEL_TYPE_MAPPER.equalsIgnoreCase(generater)) {
            return new MapperModelGenerator();
        } else if (UMLConfig.UML_MODEL_TYPE_MYBATISPLUS.equalsIgnoreCase(generater)) {
            return new MybatisplusModelGenerator();
        } else {
            LOGGER.error("unknown UMLModelGenerater, [" + generater + "]");
        }

        return null;
    }
}
