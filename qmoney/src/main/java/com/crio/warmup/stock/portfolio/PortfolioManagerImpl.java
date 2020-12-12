package com.crio.warmup.stock.portfolio;

// import static java.time.temporal.ChronoUnit.DAYS;
// import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
// import com.crio.warmup.stock.portfolio;
// import java.util.concurrent.ExecutionException;
// import java.util.concurrent.ExecutorService;
// import java.util.concurrent.Executors;
// import java.util.concurrent.Future;
// import java.util.concurrent.TimeUnit;
// import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {




  private RestTemplate restTemplate;

  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  

  private static Double  dateDiffDays(String start,LocalDate end) {
    
    long diff = ChronoUnit.DAYS.between(LocalDate.parse(start), end);
    
    return Long.valueOf(diff).doubleValue() / 365;
  }

  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {

    String url = buildUri(symbol, from, to);
    if ( from.compareTo(to) >= 0) {
          throw new RuntimeException();
    }
    TiingoCandle[] trades = restTemplate.getForObject(url, TiingoCandle[].class);
    if (trades != null) {
      List<Candle> stocks = Arrays.asList(trades);
        return stocks;
      } else {
        return new ArrayList<>();
    }

      
  }

  protected  String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String uriTemplate = "https://api.tiingo.com/tiingo/daily/$SYMBOL/prices?"
            + "startDate=$STARTDATE&endDate=$ENDDATE&token=$APIKEY";

    String token = "0175e650eb18193394fdc2c225b0c0ba954fa0a4";
    String url = uriTemplate.replace("$SYMBOL", symbol)
         .replace("$STARTDATE", startDate.toString())
         .replace("$ENDDATE", endDate.toString())
         .replace("$APIKEY",token);

    return url;
    
  }

  private static  Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }


  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> trades, 
      LocalDate endDate) {

    List<AnnualizedReturn> trds = new ArrayList<>();
    for (PortfolioTrade trade : trades) {
      trds.add(getAnnualizedReturn(endDate, trade));
    }

    Collections.sort(trds, getComparator());
    
    return trds;
  } 

  private AnnualizedReturn getAnnualizedReturn(LocalDate endDate, PortfolioTrade trade) {


    //RestTemplate rest = new RestTemplate(); 
    List<Double> openprices = new ArrayList<>();
    List<Double> closeprices = new ArrayList<>();
    Double buyPrice = 0.0;
    Double sellPrice = 0.0;
    Double years = 0.0;
    RestTemplate rst = new RestTemplate();
    String url = buildUri(trade.getSymbol(), trade.getPurchaseDate(), endDate);
    TiingoCandle[] tds = rst.getForObject(url, TiingoCandle[].class);

      
    for (TiingoCandle t: tds) {
      openprices.add(t.getOpen());
      closeprices.add(t.getClose());
    }
      
    buyPrice = openprices.get(0);
    sellPrice = closeprices.get(closeprices.size() - 1);
    years = dateDiffDays(trade.getPurchaseDate().toString(), endDate);
    Double returns = (sellPrice - buyPrice) / buyPrice;
    Double annualized = Math.pow((1 + returns), (1 / years)) - 1;
        
       
      
      
    return  new AnnualizedReturn(trade.getSymbol(),annualized, returns);
    
  }



}
