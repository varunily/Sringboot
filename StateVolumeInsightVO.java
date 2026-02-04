package io.endeavour.stocks.response;

import java.math.BigDecimal;

public class StateVolumeInsightVO {
    private String tickerSymbol;
    private BigDecimal volume;

    public StateVolumeInsightVO(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }
}
