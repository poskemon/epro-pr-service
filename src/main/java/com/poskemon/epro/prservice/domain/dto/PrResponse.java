package com.poskemon.epro.prservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.poskemon.epro.prservice.domain.entity.Item;

import java.util.List;

import com.poskemon.epro.prservice.domain.entity.PrHeader;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrResponse<T> {
    @JsonProperty("message")
    private String message;

    @JsonProperty("prHeader")
    private PrHeaderDetailRes prHeader;

    @JsonProperty("prLines")
    private List<PrDetailRes> prLines;

    @JsonProperty("items")
    private List<Item> itmeList;
}
