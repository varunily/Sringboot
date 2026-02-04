package io.endeavour.stocks.rowmapper;

import io.endeavour.stocks.Vo.StockPriceHistoryVO;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceHistoryRowMapper implements RowMapper<StockPriceHistoryVO> {

    /***
     * This method processes 1 row coming back from the Query and populates a single VO object
     * 1 Row -> 1 VO object mapper
     * @param rs
     * @param rowNum
     * @return
     * @throws SQLException
     */
    @Override
    public StockPriceHistoryVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        StockPriceHistoryVO stockPriceHistoryVO = new StockPriceHistoryVO(
                rs.getString("ticker_symbol"),
                rs.getDate("trading_date").toLocalDate()
        );
        stockPriceHistoryVO.setOpenPrice(rs.getBigDecimal("open_price"));
        stockPriceHistoryVO.setClosePrice(rs.getBigDecimal("close_price"));
        stockPriceHistoryVO.setHighPrice(rs.getBigDecimal("high_price"));
        stockPriceHistoryVO.setLowPrice(rs.getBigDecimal("low_price"));
        return stockPriceHistoryVO;
    }
}

