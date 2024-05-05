package dev.swellington.literalura.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.swellington.literalura.exception.TransformDataException;

public class TransformData implements IDataTransform {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T extends Record> T transform(String json, Class<T> clazz) throws JsonProcessingException {
        return objectMapper.readValue(json, clazz);
    }
}
