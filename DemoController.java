package io.endeavour.stocks.controller;

import io.endeavour.stocks.response.StateVolumeInsightVO;
import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.Vo.StockPriceHistoryVO;
import io.endeavour.stocks.response.CityPriceSummaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(value = "/analytics")
public class DemoController {

//    Write a StockAnalytics controller which is accessible over the sub-domain /analytics.
//    Add a GET API to return the stock with the highest trading volume for a given state on a
//    specific trading date.
//• Accept state and tradingDate as HTTP GET query parameters
//• Fetch the required data using Spring JDBC by joining
//    company_locations and stock_price_history tables
//• Return the response as a StateVolumeInsight VO containing:
//    o tickerSymbol
//    o totalVolume

    @Autowired
    MarketAnalyticsService marketAnalyticsService;

    @GetMapping(value = "/getTradingVolume")
    public StateVolumeInsightVO getTradingVolume(@RequestParam(value = "inputstate",required = true) String state,
                                                 @RequestParam(value = "inputtradingDate",required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate tradingDate){

        Optional<StockPriceHistoryVO> highestTradingVolume = marketAnalyticsService.getHighestTradingVolume(state, tradingDate);

        if (highestTradingVolume.isPresent()){
            StockPriceHistoryVO volumeForState = highestTradingVolume.get();
            StateVolumeInsightVO stateVolumeInsightVO = new StateVolumeInsightVO(volumeForState.getState());
            stateVolumeInsightVO.setVolume(volumeForState.getVolume());
            return stateVolumeInsightVO;
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Unable to fetch data for the given state and date");

    }
    @GetMapping(value = "/avgOpenClosePrice")
    public CityPriceSummaryVO avgOpenClosePrice(@RequestParam(value = "city",required = true) String city,
                                                 @RequestParam(value = "tradingDate",required = true) LocalDate tradingDate){

        return (CityPriceSummaryVO) marketAnalyticsService.getAvgPrices(city,tradingDate);

    }
}
