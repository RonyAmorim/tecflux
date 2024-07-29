package com.tecflux.dto.company;

import com.tecflux.entity.Company;

public record CompanyResponseDTO(
        Long id,
        String name,
        String cnpj,
        String address,
        String phone) {

    public static CompanyResponseDTO fromEntity(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getName(),
                maskCnpj(company.getRawCnpj()),
                maskAddress(company.getRawAddress()),
                maskPhone(company.getRawPhone())
        );
    }

    private static String maskCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() < 14) {
            return cnpj;
        }
        return cnpj.substring(0, 2) + ".***.***/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
    }

    private static String maskAddress(String address) {
        if (address == null || address.length() < 10) {
            return address;
        }
        int lengthToShow = address.length() / 3;
        return address.substring(0, lengthToShow) + "***";
    }

    private static String maskPhone(String phone) {
        if (phone == null || phone.length() < 8) {
            return phone;
        }
        int length = phone.length();
        return "***-****-" + phone.substring(length - 4);
    }
}
