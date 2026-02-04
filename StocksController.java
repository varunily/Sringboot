package io.endeavour.stocks.controller;

import io.endeavour.stocks.request.PriceHistoryRequestVO;
import io.endeavour.stocks.service.MarketAnalyticsService;
import io.endeavour.stocks.Vo.StockFundamentalsWithNamesVO;
import io.endeavour.stocks.Vo.StockPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/stocks")
public class StocksController {

    //Define a LOGGER instance by passing your current class name
    private static final Logger LOGGER = LoggerFactory.getLogger(StocksController.class);

    @Autowired
    MarketAnalyticsService marketAnalyticsService;

    @GetMapping(value = "/getSingleStockPriceHistory/{tickerSymbol}")
    public List<StockPriceHistoryVO> getSingleStockPriceHistory(@PathVariable(value = "tickerSymbol") String tickerSymbol,
                                                                @RequestParam(value = "fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fromDate,
                                                                @RequestParam(value = "toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate toDate,
                                                                @RequestParam(value = "sortColumn", required = true) Optional<String> sortColumn,
                                                                @RequestParam(value = "sortOrder",required = true) Optional<String> sortOrder)
    {
        //Input parameters validation
        if(fromDate.isAfter(toDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate cannot be greater than toDate");
        }

        List<StockPriceHistoryVO> singlePriceHistoryList = marketAnalyticsService.getSinglePriceHistory
                (tickerSymbol, fromDate, toDate, sortColumn, sortOrder);

        //Checking the response from lower layers before sending out
        if(singlePriceHistoryList==null || singlePriceHistoryList.isEmpty()){
            LOGGER.error("No Price History data returned from the database for Ticker Symbol {}, fromdate {}, toDate {}",
                    tickerSymbol, fromDate, toDate);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find Price History for given Stock "+tickerSymbol+
                    ", fromDate "+fromDate+", toDate "+toDate);
        }
        LOGGER.info("Retrieved {} stock Price History records for ticker {} in the date range {} {}",
                singlePriceHistoryList.size(), tickerSymbol, fromDate, toDate);
        return singlePriceHistoryList;
    }

    /**
     * This method gets a List of Ticker Symbols, a date range as fromDate and toDate and returns stock price history
     * for all the tickers in the given data range
     *
     * @return
     */
    @PostMapping(value = "/getMultipleStockPriceHistory")
    public List<StockPriceHistoryVO> getMultiplePriceHistory(@RequestBody PriceHistoryRequestVO priceHistoryRequestVO){
//        LOGGER.debug("Entered method getMultiplePriceHistory in {}", getClass());
        LOGGER.info("Input List of tickers : {}, fromDate : {}, toDate: {}", priceHistoryRequestVO.getTickersList(),
                priceHistoryRequestVO.getFromDate(), priceHistoryRequestVO.getToDate());
        System.out.println("Input List of tickers : "+priceHistoryRequestVO.getTickersList()+", fromDate : "+priceHistoryRequestVO.getFromDate()+
                ", toDate : "+priceHistoryRequestVO.getToDate());

        if(priceHistoryRequestVO.getFromDate().isAfter(priceHistoryRequestVO.getToDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "fromDate cannot be greater than toDate");
        }
        return marketAnalyticsService.getMultiplePriceHistory(priceHistoryRequestVO.getTickersList(),
                priceHistoryRequestVO.getFromDate(),
                priceHistoryRequestVO.getToDate());
    }

    /**
     * Web Method that gets all StockFundamentals data with names
     * @return
     */
    @GetMapping(value = "/getAllStockFundamentalsWithNames")
    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsWithNames(){
        List<StockFundamentalsWithNamesVO> allStockFundamentalsWithNamesList = marketAnalyticsService.getAllStockFundamentalsWithNames();

        if(allStockFundamentalsWithNamesList.isEmpty()){
            LOGGER.error("No Stock Fundamentals data returned from the database");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Stock Fundamentals Found in the DB");
        }
        LOGGER.info("Retrieved {} Stock Fundamentals records from the database",
                allStockFundamentalsWithNamesList.size());
        return allStockFundamentalsWithNamesList;
    }

    /**
     * Exception handler method that captures 2 kinds of exceptions thrown anywhere in the code
     * In this example, IllegalStateException and NullPointerException are captured and a
     * Http Status code 400 for Bad Request is sent back
     * @param e
     * @return
     */
    @ExceptionHandler({IllegalStateException.class, NullPointerException.class})
    public ResponseEntity<?> exceptionHandlerMethod(Throwable e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
