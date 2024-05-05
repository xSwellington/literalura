package dev.swellington.literalura.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class TransformDataException extends Throwable {
    public TransformDataException(){
        super("Nã foi possível processar as informações.");
    }
}
