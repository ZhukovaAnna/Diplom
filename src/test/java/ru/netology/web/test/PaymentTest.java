package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.TableHelper;
import ru.netology.web.page.PaymentPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
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
        void shouldReceivePaymentWhenValidCard() throws SQLException {
            val cartnumber = DataHelper.getValidCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = approvedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
        }

        @Test
        void shouldNotReceivePaymentWhenInvalidCard() throws SQLException {
            val cartnumber = DataHelper.getInvalidCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.failMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = declinedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
        }

        @Test
        void shouldNotReceivePaymentWhenOtherBankCard() throws SQLException {
            val cartnumber = DataHelper.getOtherBankCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.failMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = declinedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
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
            val cartnumber = DataHelper.getEmptyCard();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCardLess16() {
            val cartnumber = DataHelper.getCardLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCardSymbols() {
            val cartnumber = DataHelper.getCardSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly16SymbolsWhenCardMore16Symbols() throws SQLException {
            val cartnumber = DataHelper.getCardMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = TableHelper.getPaymentStatus();
            String actual = approvedStatus;
            assertEquals(expected, actual);
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
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthLess() {
            val month = DataHelper.getMonthLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongTermMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthMore() {
            val month = DataHelper.getMonthMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongTermMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonth1Number() {
            val month = DataHelper.getMonthLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenMonthSymbols() {
            val month = DataHelper.getMonthSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly2SymbolsWhenMonthMore2Symbols() throws SQLException {
            val month = DataHelper.getMonthMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = approvedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
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
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearLess() {
            val year = DataHelper.getYearLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearInPast() {
            val year = DataHelper.getEarlyYear();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.cardExpiredMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenYearSymbols() {
            val year = DataHelper.getYearSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly2SymbolsWhenYearMore2Symbols() throws SQLException {
            val year = DataHelper.getYearMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = approvedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
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
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.shouldFillMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameOneWord() {
            val name = DataHelper.getNameLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.shouldFillMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameThreeWords() {
            val name = DataHelper.getNameMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.shouldFillMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameSymbols() {
            val name = DataHelper.getNameSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.shouldFillMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenNameSmallLetters() {
            val name = DataHelper.getNameSmallLetters();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.shouldFillMessage();
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
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCVV2Numbers() {
            val cvv = DataHelper.getCVVLess();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldNotReceivePaymentWhenCVVSymbols() {
            val cvv = DataHelper.getCVVSymbols();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.wrongFormatCardMessage();
        }

        @Test
        void shouldReceiveOnly3SymbolsWhenCVVMore3Symbols() throws SQLException {
            val cvv = DataHelper.getCVVMore();
            open("http://localhost:8080");
            val paymentPage = new PaymentPage();
            paymentPage.payByCard();
            paymentPage.fillForm(cartnumber, month, year, name, cvv);
            paymentPage.successMessage();
            TableHelper.comparePaymentAndTransactionID();
            String expected = approvedStatus;
            String actual = TableHelper.getPaymentStatus();
            assertEquals(expected, actual);
        }
    }
}
