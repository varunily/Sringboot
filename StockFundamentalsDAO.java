package io.endeavour.stocks.DAO;

import io.endeavour.stocks.StockException;
import io.endeavour.stocks.Vo.StockFundamentalsWithNamesVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StockFundamentalsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockFundamentalsDAO.class);

    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public StockFundamentalsDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<StockFundamentalsWithNamesVO> getAllStockFundamentalsWithNames(){

        List<StockFundamentalsWithNamesVO> stockFndNamesList = new ArrayList<>();

        String query = """
                select
                	sf.ticker_symbol,
                	sl.ticker_name,
                	sf.market_cap,
                	sf.current_ratio,
                	sl2.sector_name,
                	sl3.subsector_name
                from
                	endeavour.stock_fundamentals sf
                	join endeavour.stocks_lookup sl on sf.ticker_symbol = sl.ticker_symbol
                	join endeavour.sector_lookup sl2 on sf.sector_id = sl2.sector_id
                	join endeavour.subsector_lookup sl3 on sf.subsector_id = sl3.subsector_id
                """;
        LOGGER.info("Query being executed for StockFundamentals is {} ", query);

        try{
            stockFndNamesList = namedParameterJdbcTemplate.query(query, (rs, rowNum) -> { //Lambda function for RowMapper
                StockFundamentalsWithNamesVO stockFundamentalsWithNamesVO = new StockFundamentalsWithNamesVO(
                        rs.getString("ticker_symbol")
                );
                stockFundamentalsWithNamesVO.setTickerName(rs.getString("ticker_name"));
                stockFundamentalsWithNamesVO.setSectorName(rs.getString("sector_name"));
                stockFundamentalsWithNamesVO.setSubSectorName(rs.getString("subsector_name"));
                stockFundamentalsWithNamesVO.setMarketCap(rs.getBigDecimal("market_cap"));
                stockFundamentalsWithNamesVO.setCurrentRatio(rs.getDouble("current_ratio"));
                return stockFundamentalsWithNamesVO;
            });
            LOGGER.info("Returning a List of size {}  retrieved from the database", stockFndNamesList.size());
        } catch (Exception e) {
            LOGGER.error("Retrieving Stock Fundamentals data failed with the reason {}", e.getCause());
            throw new StockException("Retreiveing Stock Fundamentals data failed", e);
        }

        return stockFndNamesList;
    }
}
