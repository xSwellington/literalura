package dev.swellington.literalura.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PersonDTO(int birth_year, int death_year, String name) {
}
