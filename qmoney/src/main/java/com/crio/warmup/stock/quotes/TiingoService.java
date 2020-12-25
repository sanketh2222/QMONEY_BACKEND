
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {


  private RestTemplate restTemplate;

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  protected  String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";

    String token = "0175e650eb18193394fdc2c225b0c0ba954fa0a4";
    if (endDate == null) {
      throw new RuntimeException();
    }
    String url = uriTemplate.replace("$SYMBOL", symbol)
         .replace("$STARTDATE", startDate.toString())
         .replace("$ENDDATE", endDate.toString())
         .replace("$APIKEY",token);

    return url;
    
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws JsonProcessingException,
      StockQuoteServiceException {
    // TODO Auto-generated method stub
    String url = buildUri(symbol, from, to);
    if ( from.compareTo(to) >= 0) {
          throw new RuntimeException();
    }
    // RestTemplate rst = new RestTemplate();
    // TiingoCandle[] trades = rst.getForObject(url, TiingoCandle[].class);
    try {
      String resp = restTemplate.getForObject(url, String.class);
      ObjectMapper obj = getObjectMapper();
      TiingoCandle[] trades =  obj.readValue(resp, TiingoCandle[].class);
    
      if (trades != null) {
        List<Candle> stocks = Arrays.asList(trades);
          return stocks;
        } else {
          return new ArrayList<>();
      } } catch (Exception e) {
        throw new StockQuoteServiceException("exc ocuured");
      }
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
