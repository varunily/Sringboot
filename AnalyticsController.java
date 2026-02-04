package io.endeavour.stocks.controller;

import io.endeavour.stocks.response.CityPriceSummaryVO;
import io.endeavour.stocks.service.MarketAnalyticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final MarketAnalyticsService service;

    public AnalyticsController(MarketAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/city-price-summary")
    public CityPriceSummaryVO getCityPriceSummary(
            @RequestParam (value = "city") String city,
            @RequestParam(value = "tradingDate") String inputDate) {

        LocalDate tradingDate = LocalDate.parse(inputDate.trim());

        return service.getAvgPrices(city, tradingDate);
    }
}
