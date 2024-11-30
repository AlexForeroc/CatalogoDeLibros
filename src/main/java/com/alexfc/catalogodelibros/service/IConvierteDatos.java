package com.alexfc.catalogodelibros.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> classInfo);
}
