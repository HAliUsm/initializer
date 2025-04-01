package com.code.initializer.model;

import io.americanexpress.synapse.service.imperative.model.BaseServiceResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInitalizerResponse implements BaseServiceResponse {
    byte[] zipBytes;
    public String message;
}
