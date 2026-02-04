package io.endeavour.stocks.Vo;

import java.math.BigDecimal;
import java.util.Objects;

public class StockFundamentalsWithNamesVO {

    private String tickerSymbol;
    private String tickerName;
    private BigDecimal marketCap;
    private double currentRatio;
    private String sectorName;
    private String subSectorName;

    public StockFundamentalsWithNamesVO(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    private StockFundamentalsWithNamesVO() {
    }

    public String getTickerSymbol() {
        return tickerSymbol;
    }

    public void setTickerSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public String getTickerName() {
        return tickerName;
    }

    public void setTickerName(String tickerName) {
        this.tickerName = tickerName;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(BigDecimal marketCap) {
        this.marketCap = marketCap;
    }

    public double getCurrentRatio() {
        return currentRatio;
    }

    public void setCurrentRatio(double currentRatio) {
        this.currentRatio = currentRatio;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public String getSubSectorName() {
        return subSectorName;
    }

    public void setSubSectorName(String subSectorName) {
        this.subSectorName = subSectorName;
    }

    @Override
    public String toString() {
        return "StockFundamentalsWithNamesVO{" +
                "tickerSymbol='" + tickerSymbol + '\'' +
                ", tickerName='" + tickerName + '\'' +
                ", marketCap=" + marketCap +
                ", currentRatio=" + currentRatio +
                ", sectorName='" + sectorName + '\'' +
                ", subSectorName='" + subSectorName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockFundamentalsWithNamesVO that = (StockFundamentalsWithNamesVO) o;
        return Objects.equals(getTickerSymbol(), that.getTickerSymbol());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTickerSymbol());
    }
}
