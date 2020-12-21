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
import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;

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

  private StockQuotesService stockQuotesService;

  // Caution: Do not delete or modify the constructor, or else your build will break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  //added because of the test case issue(PortfolioManagerTest)
  //why do we need this? method is not being used from factory
  protected PortfolioManagerImpl(StockQuotesService stockQuotesService) {
    this.stockQuotesService = stockQuotesService;
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
    RestTemplate rst = new RestTemplate();
    TiingoCandle[] trades = rst.getForObject(url, TiingoCandle[].class);
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
    if (endDate == null) {
      throw new RuntimeException();
    }
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


    if (endDate == null) {
      throw new RuntimeException();
    }

    List<AnnualizedReturn> trds = new ArrayList<>();
    for (PortfolioTrade trade : trades) {
      trds.add(getAnnualizedReturn(endDate, trade));
    }

    Collections.sort(trds, getComparator());
    
    return trds;
  } 

  private AnnualizedReturn getAnnualizedReturn(LocalDate endDate, PortfolioTrade trade) {


    //RestTemplate rest = new RestTemplate(); 
    AnnualizedReturn annualizedReturn;
    try {
      List<Candle> stocksstartToEndDate;

      stocksstartToEndDate = getStockQuote(trade.getSymbol(), trade.getPurchaseDate(), endDate);

      Candle stockStartDate = stocksstartToEndDate.get(0);

      Candle stockLatest = stocksstartToEndDate.get(stocksstartToEndDate.size() - 1);

      // if (!stockLatest.getDate().toString().equals(endDate.toString())) {
      //    stockLatest = stocksstartToEndDate.get(stocksstartToEndDate.size() - 2);
      // }

      Double buyPrice = stockStartDate.getOpen();
      Double sellPrice = stockLatest.getClose();

      Double years = 0.0;
      years = dateDiffDays(trade.getPurchaseDate().toString(), endDate);
      Double returns = (sellPrice - buyPrice) / buyPrice;
      Double annualized = Math.pow((1 + returns), (1 / years)) - 1;

      annualizedReturn = new AnnualizedReturn(trade.getSymbol(),annualized, returns);


    } catch (JsonProcessingException e) {
      annualizedReturn = new AnnualizedReturn(trade.getSymbol(),Double.NaN, Double.NaN);
    }
    // List<Double> openprices = new ArrayList<>();
    // List<Double> closeprices = new ArrayList<>();
    // Double buyPrice = 0.0;
    // Double sellPrice = 0.0;
    // Double years = 0.0;
    // RestTemplate rst = new RestTemplate();
    // if (endDate != null) {
    //   String url = buildUri(trade.getSymbol(), trade.getPurchaseDate(), endDate);
    //   TiingoCandle[] tds = rst.getForObject(url, TiingoCandle[].class);
    //   for (TiingoCandle t: tds) {
    //     openprices.add(t.getOpen());
    //     closeprices.add(t.getClose());
    //   }
    // } else {
    //   throw new RuntimeException();
    // }
    return annualizedReturn;
    
      
    // buyPrice = openprices.get(0);
    // sellPrice = closeprices.get(closeprices.size() - 1);
    // years = dateDiffDays(trade.getPurchaseDate().toString(), endDate);
    // Double returns = (sellPrice - buyPrice) / buyPrice;
    // Double annualized = Math.pow((1 + returns), (1 / years)) - 1;
    // openprices.clear();
    // closeprices.clear();
        
       
      
      
    // return  new AnnualizedReturn(trade.getSymbol(),annualized, returns);
    
  }



  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Modify the function #getStockQuote and start delegating to calls to
  //  stockQuoteService provided via newly added constructor of the class.
  //  You also have a liberty to completely get rid of that function itself, however, make sure
  //  that you do not delete the #getStockQuote function.

}
