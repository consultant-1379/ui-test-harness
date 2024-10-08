package com.ericsson.cifwk.taf.ui;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

class ClassAdapter implements JsonSerializer<Class> {

    @Override
    public JsonElement serialize(Class clazz, Type type, JsonSerializationContext jsc) {
        return new JsonPrimitive(clazz.getName());
    }
}
