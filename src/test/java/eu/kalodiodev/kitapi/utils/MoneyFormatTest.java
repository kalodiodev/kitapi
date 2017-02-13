package eu.kalodiodev.kitapi.utils;

import org.junit.Assert;

import java.util.Locale;

/**
 * Money Format util Test
 *
 * @author Raptodimos Thanos
 */
public class MoneyFormatTest {

    @org.junit.Test
    public void moneyFormat() {
        double money = 5000.234d;

        //en-US
        Locale.setDefault(new Locale("en", "US"));
        String formattedMoney = MoneyFormat.format(money);
        Assert.assertEquals("US: Should be formatted with two decimals", "5,000.23", formattedMoney);

        //el-GR
        Locale.setDefault(new Locale("el","GR"));
        formattedMoney = MoneyFormat.format(money);
        Assert.assertEquals("GR: Should be formatted with two decimals", "5.000,23", formattedMoney);
    }
}
