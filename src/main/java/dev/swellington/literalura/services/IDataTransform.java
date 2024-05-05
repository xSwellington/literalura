package dev.swellington.literalura.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import dev.swellington.literalura.exception.TransformDataException;

public interface IDataTransform {
    <T extends Record> T transform(String json, Class<T> clazz) throws TransformDataException, JsonProcessingException;
}
