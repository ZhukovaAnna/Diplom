package ru.netology.web.data;

import lombok.Value;

public class DataHelper {
    private DataHelper(){
    }

    @Value
    public static class CartNumber {
        private String CartNumber;
    }

    public static CartNumber getValidCard() {
        return new CartNumber("4444444444444441");
    }

    public static CartNumber getInvalidCard() {
        return new CartNumber("4444444444444442");
    }

    public static CartNumber getOtherBankCard() {
        return new CartNumber("5678444444444442");
    }

    public static CartNumber getEmptyCard() {
        return new CartNumber("");
    }

    public static CartNumber getCardLess() {
        return new CartNumber("444444444444444");
    }

    public static CartNumber getCardMore() {
        return new CartNumber("44444444444444413");
    }

    public static CartNumber getCardSymbols() {
        return new CartNumber("qwertyuiop@#$%^&");
    }

    @Value
    public static class Month {
        private String Month;
    }

    public static Month getValidMonth() {
        return new Month("09");
    }

    public static Month getEmptyMonth() {
        return new Month("");
    }

    public static Month getMonthNull() {
        return new Month("00");
    }

    public static Month getMonthLess() {
        return new Month("8");
    }

    public static Month getMonthMore() {
        return new Month("088");
    }

    public static Month getInvalidMonthMore() {
        return new Month("13");
    }

    public static Month getMonthSymbols() {
        return new Month("q&");
    }

    @Value
    public static class Year {
        private String Year;
    }

    public static Year getValidYear() {
        return new Year("22");
    }

    public static Year getEmptyYear() {
        return new Year("");
    }

    public static Year getYearLess() {
        return new Year("2");
    }

    public static Year getYearMore() {
        return new Year("2022");
    }

    public static Year getEarlyYear() {
        return new Year("10");
    }

    public static Year getYearSymbols() {
        return new Year("w%");
    }

    @Value
    public static class Name {
        private String Name;
    }

    public static Name getValidName() {
        return new Name("Ivan Ivanov");
    }

    public static Name getEmptyName() {
        return new Name("");
    }

    public static Name getNameLess() {
        return new Name("Ivan");
    }

    public static Name getNameMore() {
        return new Name("Ivan Ivanovich Ivanov");
    }

    public static Name getNameSmallLetters() {
        return new Name("ivan ivanov");
    }

    public static Name getNameSymbols() {
        return new Name("Ива& !@d$k^");
    }

    @Value
    public static class Cvv {
        private String Cvv;
    }

    public static Cvv getValidCVV() {
        return new Cvv("999");
    }

    public static Cvv getEmptyCVV() {
        return new Cvv("");
    }

    public static Cvv getCVVLess() {
        return new Cvv("99");
    }

    public static Cvv getCVVMore() {
        return new Cvv("9999");
    }

    public static Cvv getCVVSymbols() {
        return new Cvv("j@_");
    }
}
