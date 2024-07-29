package com.tecflux.validator;

public class CnpjValidator {

    private CnpjValidator() {
        // Construtor privado para impedir instâncias
    }

    public static boolean isValidCnpj(String cnpj) {
        cnpj = cnpj.replaceAll("\\D", "");

        if (cnpj.length() != 14) {
            return false;
        }

        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int sum = 0;
        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 12; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight1[i];
        }

        int mod = sum % 11;
        int firstCheckDigit = mod < 2 ? 0 : 11 - mod;

        sum = 0;
        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        for (int i = 0; i < 13; i++) {
            sum += Character.getNumericValue(cnpj.charAt(i)) * weight2[i];
        }

        mod = sum % 11;
        int secondCheckDigit = mod < 2 ? 0 : 11 - mod;

        return firstCheckDigit == Character.getNumericValue(cnpj.charAt(12)) &&
                secondCheckDigit == Character.getNumericValue(cnpj.charAt(13));
    }
}
