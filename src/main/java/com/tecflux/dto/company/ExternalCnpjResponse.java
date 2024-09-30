package com.tecflux.dto.company;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ExternalCnpjResponse(
        @JsonProperty("cnpj") String cnpj,
        @JsonProperty("nome_social") String nome,
        @JsonProperty("nome_fantasia") String fantasia,
        @JsonProperty("ddd_telefone_1") String telefone,
        @JsonProperty("email") String email,
        @JsonProperty("logradouro") String logradouro,
        @JsonProperty("numero") String numero,
        @JsonProperty("complemento") String complemento,
        @JsonProperty("bairro") String bairro,
        @JsonProperty("municipio") String municipio,
        @JsonProperty("uf") String uf,
        @JsonProperty("cep") String cep
) {}
