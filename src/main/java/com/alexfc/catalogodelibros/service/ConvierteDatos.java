package com.alexfc.catalogodelibros.service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {

    private ObjectMapper mapper = new ObjectMapper();

    public ConvierteDatos() {
    }

    public <T> T obtenerDatos(String json, Class<T> classInfo) {
        try {
            return this.mapper.readValue(json, classInfo);
        } catch (JsonProcessingException var4) {
            JsonProcessingException e = var4;
            throw new RuntimeException(e);
        }
    }

}
