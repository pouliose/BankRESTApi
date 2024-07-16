package com.company.fintech.enums;

public enum ResponseStatus {

    TRANSACTION_AMOUNT_IS_NOT_VALID("Transaction amount is not valid"),
    SOURCE_AND_TARGET_ACCOUNT_ARE_THE_SAME("Source and target account are the same"),
    BOTH_SOURCE_AND_TARGET_ACCOUNTS_DO_NOT_EXIST("Both source and target accounts do not exist"),
    SOURCE_ACCOUNT_DOES_NOT_EXIST("Source account does not exist"),
    TARGET_ACCOUNT_DOES_NOT_EXIST("Target account does not exist"),

    CURRENCY_OF_TRANSACTION_DIFFERS_TO_THAT_OF_ACCOUNTS("Currency of transaction differs to that of accounts"),

    SOURCE_AND_TARGET_ACCOUNTS_CURRENCY_ARE_DIFFERENT("Source and target accounts currency are different"),
    INSUFFICIENT_BALANCE_FOR_MONEY_TRANSFER("Insufficient balance for money transfer"),
    ;

    private final String value;

    ResponseStatus(String value) {
        this.value = value;
    }

    public String getDescription() {
        return value;
    }
}
