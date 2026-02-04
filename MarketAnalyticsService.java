package io.endeavour.stocks.service;

import io.endeavour.stocks.DAO.PriceHistoryDAO;
import io.endeavour.stocks.DAO.StockFundamentalsDAO;
import io.endeavour.stocks.response.CityPriceSummaryVO;
import io.endeavour.stocks.Vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.Vo.StockPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class MarketAnalyticsService<CityPricesSummaryVO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MarketAnalyticsService.class);

    //    @Autowired //Autowire by name
    PriceHistoryDAO priceHistoryDAO;

    StockFundamentalsDAO stockFundamentalsDAO;

    @Autowired //Constructor autowiring
    public MarketAnalyticsService(PriceHistoryDAO priceHistoryDAO,
                                  StockFundamentalsDAO stockFundamentalsDAO) {
        this.priceHistoryDAO = priceHistoryDAO;
        this.stockFundamentalsDAO = stockFundamentalsDAO;
       // this.analyticsDAO = analyticsDAO;
    }

    public List<StockPriceHistoryVO> getSinglePriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate, Optional<String> sortColumn, Optional<String> sortOrder){

        String sortColumnString = sortColumn.orElse("TRADINGDATE");
        String sortOrderString = sortOrder.orElse("ASC");

        Comparator sortComparator = switch(sortColumnString.toUpperCase()) {
            case ("TRADINGDATE") -> Comparator.comparing(StockPriceHistoryVO::getTradingDate);
            case ("OPENPRICE") -> Comparator.comparing(StockPriceHistoryVO::getOpenPrice);
            case ("CLOSEPRICE") -> Comparator.comparing(StockPriceHistoryVO::getClosePrice);
            case ("HIGHPRICE") -> Comparator.comparing(StockPriceHistoryVO::getHighPrice);
            case ("LOWPRICE") -> Comparator.comparing(StockPriceHistoryVO::getLowPrice);
            case ("TICKERSYMBOL") -> Comparator.comparing(StockPriceHistoryVO::getTickerSymbol);
            default -> throw new IllegalStateException("Unexpected Column value entered " + sortColumnString.toUpperCase());
        };

        //If sortOrder is desc, reverse the comparator
        if(sortOrderString.equalsIgnoreCase("DESC")){
            sortComparator = sortComparator.reversed();
        }

        List<StockPriceHistoryVO> priceHistoryList = priceHistoryDAO.getSingleStockPriceHistory(tickerSymbol, fromDate, toDate);

        priceHistoryList.sort(sortComparator);

        return priceHistoryList;
    }


    public List<StockPriceHistoryVO> getMultiplePriceHistory(List<String> tickersList, LocalDate fromDate, LocalDate toDate) {
        return priceHistoryDAO.getMultiplePriceHistory(tickersList, fromDate, toDate);
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsWithNames(){
        return stockFundamentalsDAO.getAllStockFundamentalsWithNames()
                .stream()
                .sorted(Comparator.comparing(StockFundamentalsWithNamesVO::getTickerName))
                .toList();
    }

    public Optional<StockPriceHistoryVO> getHighestTradingVolume(String state, LocalDate tradingDate){
        List<StockPriceHistoryVO> volumeList = priceHistoryDAO.getAllStocks(state,tradingDate);


        return volumeList.stream()
                .max(Comparator.comparing(StockPriceHistoryVO::getVolume));



    }




    public CityPriceSummaryVO getAvgPrices(String city, LocalDate tradingDate){
        List<StockPriceHistoryVO> priceHistoryList = priceHistoryDAO.getPriceHistoryState(city,tradingDate);

        Optional<BigDecimal> sumOpenPrice = priceHistoryList.stream()
                .map(StockPriceHistoryVO::getOpenPrice)
                .reduce(BigDecimal::add);
        Optional<BigDecimal> sumClosePrice = priceHistoryList.stream()
                .map(StockPriceHistoryVO::getClosePrice)
                .reduce(BigDecimal::add);
        BigDecimal avgClosePrice = sumClosePrice.get().divide(BigDecimal.valueOf(priceHistoryList.size()));
        BigDecimal avgOpenPrice = sumOpenPrice.get().divide(BigDecimal.valueOf(priceHistoryList.size()));
        CityPriceSummaryVO cityPriceSummaryVO = new CityPriceSummaryVO(city,tradingDate);
        cityPriceSummaryVO.setClosePrice(avgClosePrice);
        cityPriceSummaryVO.setOpenPrice(avgOpenPrice);

        return cityPriceSummaryVO;
    }
}

