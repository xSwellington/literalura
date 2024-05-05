package dev.swellington.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SearchDTO(int count, String next, String previous, List<BookDTO> results) {
}
