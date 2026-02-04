package io.endeavour.stocks.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CityPriceSummaryVO {
    private String city;
    private LocalDate tradingDate;
    private BigDecimal openPrice;
    private BigDecimal closePrice;


    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public CityPriceSummaryVO(String city, LocalDate tradingDate) {
        this.city = city;
        this.tradingDate = tradingDate;
    }

    public String getCity() {
        return city;
    }


    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }
}