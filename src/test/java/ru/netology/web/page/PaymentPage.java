package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaymentPage {
    private ElementsCollection buttons = $$("[role=button]");
    private SelenideElement byuButton = buttons.first();
    private SelenideElement creditButton = $(byText("Купить в кредит"));
    private SelenideElement continueButton = $(byText("Продолжить"));

    private ElementsCollection fields = $$(".input__control");
    private SelenideElement numberField = $("[maxlength='19']");
    private SelenideElement monthField = $("[placeholder='08']");
    private SelenideElement yearField = $("[placeholder='22']");
    private SelenideElement ownerField = fields.get(3);
    private SelenideElement cvvField = $("[maxlength='3']");

    private SelenideElement success = $(byText("Операция одобрена Банком."));
    private SelenideElement fail = $(byText("Ошибка! Банк отказал в проведении операции."));
    private SelenideElement wrongFormatCard = $(byText("Неверный формат"));
    private SelenideElement wrongTerm = $(byText("Неверно указан срок действия карты"));
    private SelenideElement cardExpired = $(byText("Истёк срок действия карты"));
    private SelenideElement shouldFillField = $(byText("Поле обязательно для заполнения"));

    private long timeout = 15000;

    public void payByCard() {
        byuButton.click();
    }

    public void creditByCard() {
        creditButton.click();
    }

    public void fillForm(DataHelper.CartNumber CartNumber, DataHelper.Month Month, DataHelper.Year Year, DataHelper.Name Name, DataHelper.Cvv Cvv) {
        numberField.sendKeys(CartNumber.getCartNumber());
        monthField.sendKeys(Month.getMonth());
        yearField.sendKeys(Year.getYear());
        ownerField.sendKeys(Name.getName());
        cvvField.sendKeys(Cvv.getCvv());
        continueButton.click();
    }

    public void successMessage() {
        success.waitUntil(Condition.visible, timeout);
    }

    public void failMessage() {
        fail.waitUntil(Condition.visible, timeout);
    }

    public void wrongFormatCardMessage() {
        wrongFormatCard.waitUntil(Condition.visible, timeout);
    }

    public void wrongTermMessage() {
        wrongTerm.waitUntil(Condition.visible, timeout);
    }

    public void cardExpiredMessage() {
        cardExpired.waitUntil(Condition.visible, timeout);
    }

    public void shouldFillMessage() {
        shouldFillField.waitUntil(Condition.visible, timeout);
    }
}
