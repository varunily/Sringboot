package io.endeavour.stocks.DAO;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.response.CityPriceSummaryVO;
import io.endeavour.stocks.rowmapper.PriceHistoryRowMapper;
import io.endeavour.stocks.Vo.StockPriceHistoryVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PriceHistoryDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceHistoryDAO.class);

    //    @Autowired
    JdbcTemplate jdbcTemplate;  //This is a class from Spring JDBC framework that we use to get connection and fire queries into the database

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public PriceHistoryDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<StockPriceHistoryVO> getSingleStockPriceHistory(String tickerSymbol, LocalDate fromDate, LocalDate toDate) {
        String query = """
                	 select
                	 	sph.ticker_symbol,
                	 	sph.trading_date,
                	 	sph.open_price,
                	 	sph.close_price,
                	 	sph.high_price,
                	 	sph.low_price
                	 from
                	 	endeavour.stocks_price_history sph
                	 where
                	 	sph.ticker_symbol = ?
                	 	and sph.trading_date between ? and ?
                """;
        try {
            //Passing input parameters into an Object array and feed that array into the query method of jdbcTemplate
            Object[] inputParams = new Object[]{tickerSymbol, fromDate, toDate};
            List<StockPriceHistoryVO> priceHistoryList = jdbcTemplate.query(query, inputParams, new PriceHistoryRowMapper());
            return priceHistoryList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new StockException(e.getMessage(), e);
        }

    }


    public List<StockPriceHistoryVO> getMultiplePriceHistory(List<String> tickersList, LocalDate fromDate, LocalDate toDate) {
        List<StockPriceHistoryVO> priceHistoryList = null;
        String query = """
                	 select
                	 	sph.ticker_symbol,
                	 	sph.trading_date,
                	 	sph.open_price,
                	 	sph.close_price,
                	 	sph.high_price,
                	 	sph.low_price
                	 from
                	 	endeavour.stocks_price_history sph
                	 where
                	 	sph.ticker_symbol IN (:stocksList)
                	 	and sph.trading_date between :fromDate and :toDate
                """;
        //This is used to feed values as input parameters to the query
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("stocksList", tickersList);
        mapSqlParameterSource.addValue("toDate", toDate);
        mapSqlParameterSource.addValue("fromDate", fromDate);
        try {
            //RowMapper is a lambda function
            priceHistoryList = namedParameterJdbcTemplate.query(query, mapSqlParameterSource,
                    (rs, rowNum) -> {
                        StockPriceHistoryVO stockPriceHistoryVO = new StockPriceHistoryVO(
                                rs.getString("ticker_symbol"),
                                rs.getDate("trading_date").toLocalDate()
                        );
                        stockPriceHistoryVO.setOpenPrice(rs.getBigDecimal("open_price"));
                        stockPriceHistoryVO.setClosePrice(rs.getBigDecimal("close_price"));
                        stockPriceHistoryVO.setHighPrice(rs.getBigDecimal("high_price"));
                        stockPriceHistoryVO.setLowPrice(rs.getBigDecimal("low_price"));
                        return stockPriceHistoryVO;
                    });

        } catch (Exception e) {
            LOGGER.error("Encountered an exception while retrieving price History for Tickers {}, fromDate {}, toDate {} , the exception root cause being {}",
                    tickersList, fromDate, toDate, e.getStackTrace());
            throw new StockException("Error retrieving price history for ticker", e);
        }
        return priceHistoryList;
    }

    public List<StockPriceHistoryVO> getAllStocks(String state, LocalDate tradingDate) {
        List<StockPriceHistoryVO> priceHistoryList = new ArrayList<>();
        String query = """
                select
                    sph.ticker_symbol,
                    sph.trading_date,
                    cl.state,
                    sph.volume
                from
                    endeavour.stocks_price_history sph
                    join endeavour.company_locations cl on cl.ticker_symbol  = sph.ticker_symbol
                where
                    sph.trading_date = (:tradingDate)
                    and cl.state = (:state)
                """;

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        //The following statements can be written in and order as we are passing the values as input parameters with a name of the parameters
        mapSqlParameterSource.addValue("state", state);
        mapSqlParameterSource.addValue("tradingDate", tradingDate);

        priceHistoryList = namedParameterJdbcTemplate.query(query, mapSqlParameterSource, (rs, rN) -> {
            StockPriceHistoryVO stockPriceHistoryVO = new StockPriceHistoryVO(
                    rs.getString("ticker_symbol"),
                    rs.getDate("trading_date").toLocalDate());
            stockPriceHistoryVO.setState(rs.getString("state"));
            stockPriceHistoryVO.setVolume(rs.getBigDecimal("volume"));
            return stockPriceHistoryVO;
        });
        return priceHistoryList;
    }

    public List<StockPriceHistoryVO> getPriceHistoryState(String city, LocalDate tradingDate) {
        List<StockPriceHistoryVO> priceHistoryList = new ArrayList<>();
        String query = """
                select
                    sph.ticker_symbol,
                    sph.trading_date,
                    cl.city,
                    sph.open_price,
                    sph.close_price
                from
                    endeavour.stocks_price_history sph
                    join endeavour.company_locations cl on cl.ticker_symbol  = sph.ticker_symbol
                where
                    sph.trading_date = (:tradingDate)
                    and cl.city =(:city)
                """;
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("tradingDate", tradingDate);
        parameterSource.addValue("city", city);

        try {
            priceHistoryList = namedParameterJdbcTemplate.query(query, parameterSource, (rs, rn) -> {
                StockPriceHistoryVO stockPriceHistoryVO = new StockPriceHistoryVO(rs.getString("ticker_symbol"),
                        rs.getDate("trading_date").toLocalDate());
                stockPriceHistoryVO.setCity(rs.getString("city"));
                stockPriceHistoryVO.setOpenPrice(rs.getBigDecimal("open_price"));
                stockPriceHistoryVO.setClosePrice(rs.getBigDecimal("close_price"));
                return stockPriceHistoryVO;
            });
            return priceHistoryList;
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

