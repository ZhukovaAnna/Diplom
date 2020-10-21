package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.TableHelper;
import ru.netology.web.page.PaymentPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditTest {
    @BeforeAll
    public static void cleanTablesBefore() throws SQLException {
        TableHelper.cleanData();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    public static void cleanTablesAfter() throws SQLException {
        TableHelper.cleanData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    private String approvedStatus = "APPROVED";
    private String declinedStatus = "DECLINED";

    @Nested
    public class PaymentOperationsTests {
        private DataHelper.Month month = DataHelper.getValidMonth();
        private DataHelper.Year year = DataHelper.getValidYear();
        private DataHelper.Name name = DataHelper.getValidName();
        private DataHelper.Cvv cvv = DataHelper.getValidCVV();

        @Test
        void shouldReceivePaymentWhenValidApprovedCard() throws SQLException {
            val number = DataHelper.getValidCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.compareCreditAndTransactionID();
            String expected = approvedStatus;
            String actual = TableHelper.getCreditStatus();
            assertEquals(expected, actual);
        }

        @Test
        void shouldNotReceivePaymentWhenInvalidCard() throws SQLException {
            val number = DataHelper.getInvalidCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.failMessage();
            TableHelper.compareCreditAndTransactionID();
            String expected = declinedStatus;
            String actual = TableHelper.getCreditStatus();
            Assertions.assertEquals(expected, actual);
        }

        @Test
        void shouldNotReceivePaymentWhenCardOtherBank() throws SQLException {
            val number = DataHelper.getOtherBankCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.failMessage();
            TableHelper.compareCreditAndTransactionID();
            String expectedStatus = declinedStatus;
            String actualStatus = TableHelper.getCreditStatus();
            Assertions.assertEquals(expectedStatus, actualStatus);
        }
    }

    @Nested
    public class NumberFieldTests {
        private DataHelper.Month month = DataHelper.getValidMonth();
        private DataHelper.Year year = DataHelper.getValidYear();
        private DataHelper.Name name = DataHelper.getValidName();
        private DataHelper.Cvv cvv = DataHelper.getValidCVV();

        @Test
        void shouldNotReceivePaymentWhenValidCard() {
            val number = DataHelper.getEmptyCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCardLess16() {
            val number = DataHelper.getCardLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCardSymbols() {
            val number = DataHelper.getCardSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly16SymbolsWhenCardMoreSymbols() throws SQLException {
            val number = DataHelper.getCardMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(number, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.compareCreditAndTransactionID();
            String expectedStatus = approvedStatus;
            String actualStatus = TableHelper.getCreditStatus();
            Assertions.assertEquals(expectedStatus, actualStatus);
        }
    }

    @Nested
    public class MonthFieldTests {
        private DataHelper.CartNumber cartnumber = DataHelper.getValidCard();
        private DataHelper.Year year = DataHelper.getValidYear();
        private DataHelper.Name name = DataHelper.getValidName();
        private DataHelper.Cvv cvv = DataHelper.getValidCVV();

        @Test
        void shouldNotReceivePaymentWhenMonthEmpty() {
            val month = DataHelper.getEmptyMonth();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthLess() {
            val month = DataHelper.getMonthLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongTermMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthMore() {
            val month = DataHelper.getInvalidMonthMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongTermMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonth1Number() {
            val month = DataHelper.getMonthLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthSymbols() {
            val month = DataHelper.getMonthSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly2SymbolsWhenMonthMore2Symbols() throws SQLException {
            val month = DataHelper.getMonthMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.compareCreditAndTransactionID();
            String expectedStatus = approvedStatus;
            String actualStatus = TableHelper.getCreditStatus();
            Assertions.assertEquals(expectedStatus, actualStatus);
        }
    }

    @Nested
    public class YearFieldTests {
        DataHelper.CartNumber cartnumber = DataHelper.getValidCard();
        DataHelper.Month month = DataHelper.getValidMonth();
        DataHelper.Name name = DataHelper.getValidName();
        DataHelper.Cvv cvv = DataHelper.getValidCVV();

        @Test
        void shouldNotReceivePaymentWhenYearEmpty() {
            val year = DataHelper.getEmptyYear();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearLess() {
            val year = DataHelper.getYearLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearInPast() {
            val year = DataHelper.getEarlyYear();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.cardExpiredMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearSymbols() {
            val year = DataHelper.getYearSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly2SymbolsWhenYearMore2Symbols() throws SQLException {
            val year = DataHelper.getYearMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.compareCreditAndTransactionID();
            String expectedStatus = approvedStatus;
            String actualStatus = TableHelper.getCreditStatus();
            Assertions.assertEquals(expectedStatus, actualStatus);
        }
    }

    @Nested
    public class OwnerFieldTests {
        DataHelper.CartNumber cartnumber = DataHelper.getValidCard();
        DataHelper.Month month = DataHelper.getValidMonth();
        DataHelper.Year year = DataHelper.getValidYear();
        DataHelper.Cvv cvv = DataHelper.getValidCVV();

        @Test
        void shouldNotReceivePaymentWhenNameEmpty() {
            val name = DataHelper.getEmptyName();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameOneWord() {
            val name = DataHelper.getNameLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameThreeWords() {
            val name = DataHelper.getNameMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameSymbols() {
            val name = DataHelper.getNameSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameSmallLetters() {
            val name= DataHelper.getNameSmallLetters();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }
    }

    @Nested
    public class CVVFieldTests {
        DataHelper.CartNumber cartnumber = DataHelper.getValidCard();
        DataHelper.Month month = DataHelper.getValidMonth();
        DataHelper.Year year = DataHelper.getValidYear();
        DataHelper.Name name = DataHelper.getValidName();

        @Test
        void shouldNotReceivePaymentWhenCVVEmpty() {
            val cvv = DataHelper.getEmptyCVV();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCVV2Numbers() {
            val cvv = DataHelper.getCVVLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCVVSymbols() {
            val cvv = DataHelper.getCVVSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly3SymbolsWhenCVVMore3Symbols() throws SQLException {
            val cvv = DataHelper.getCVVMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.creditByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.compareCreditAndTransactionID();
            String expectedStatus = approvedStatus;
            String actualStatus = TableHelper.getCreditStatus();
            Assertions.assertEquals(expectedStatus, actualStatus);
        }
    }
}
