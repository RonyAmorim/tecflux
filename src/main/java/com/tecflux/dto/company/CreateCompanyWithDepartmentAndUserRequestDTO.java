package com.tecflux.dto.company;

import java.util.Set;

public record CreateCompanyWithDepartmentAndUserRequestDTO(
        String cnpj,
        String companyName,
        String address,
        String phone,
        String userName,
        String userEmail,
        String userPhone,
        String userPosition,
        String userPassword,
        Set<String> roles
) {
}
