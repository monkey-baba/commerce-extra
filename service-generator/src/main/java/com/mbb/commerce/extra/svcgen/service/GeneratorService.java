package com.mbb.commerce.extra.svcgen.service;

import java.io.IOException;
import java.util.HashMap;

public interface GeneratorService {


    String moduleGenerate(HashMap<String, Object> context, String path) throws IOException;
}
