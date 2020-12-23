
package com.crio.warmup.stock.quotes;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
import com.crio.warmup.stock.dto.Candle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class AlphavantageService implements StockQuotesService {

  private RestTemplate restTemplate;

  protected AlphavantageService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }
  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to) throws
   JsonProcessingException, StockQuoteServiceException {
    // TODO Auto-generated method stub
    String url = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
        +"&symbol="+symbol+"&outputsize=full&apikey=DN6BYSTHCP5EE4HV";
    RestTemplate rst = new RestTemplate();
    // restTemplate = new RestTemplate();
    try {

    
      String resp = restTemplate.getForObject(url,String.class);
      
      System.out.println("the response is "+resp);
      ObjectMapper obj = getObjectMapper();
      Map<LocalDate,AlphavantageCandle> ap1 = obj.readValue(resp, AlphavantageDailyResponse.class).getCandles();
      AlphavantageDailyResponse ap = rst.getForObject(url, AlphavantageDailyResponse.class);
      // AlphavantageDailyResponse ap = rst.getForObject(url, AlphavantageDailyResponse.class);
      // AlphavantageCandle[] ap1 = rst.getForObject(url, AlphavantageCandle[].class);
      // List<Candle> c = Arrays.asList(ap1);
      List<Candle> cd = new ArrayList<>();
      Collection<AlphavantageCandle> ac1 = new ArrayList<>();
      for (Map.Entry<LocalDate, AlphavantageCandle> mapElement : ap1.entrySet()) { 
    
        LocalDate key = mapElement.getKey(); 
        if ( key.compareTo(from) >= 0 && key.compareTo(to) <= 0) {
            mapElement.getValue().setDate(key);
            ac1.add(mapElement.getValue());
        }

        // Finding the value 
        AlphavantageCandle value = mapElement.getValue(); 

        // print the key : value pair 
        System.out.println(key + " : " + value); 
    } 
      // Collection<AlphavantageCandle> ac = ap.getCandles().values();
      
      for (AlphavantageCandle a : ac1) {
        
          cd.add(a);
      }
      
      Collections.reverse(cd);


      
      
      // AlphavantageCandle ap1 = (AlphavantageCandle) ap.getCandles().values();
      // List<Candle> v = Arrays.asList(ap1);
      // ap.setCandles(candles);
      
      // Map<LocalDate, AlphavantageCandle> c = ap.getCandles();
      // // List<Candle> f = c.
      // ap.setCandles(c);
  
      // // c4.
      // // List<AlphavantageCandle> c2 = c.values();
      // c.values();
      // // System.out.prsintln(url);
      // c.keySet();
      // AlphavantageCandle c2 = c.get(from);
      // LocalDate date = LocalDate.parse("2020-12-18");
      // c2.getClose();
      // c.get(to);
      // List<Candle> stocks = Arrays.asList(ap);//returns List<Alphavantagedaily response
      // c.get(LocalDate.parse("2020-12-18"))
      
      
      //need to sort and filter with the start and end date
      
      return cd; } catch(Exception e) {
        throw new StockQuoteServiceException("exc occured");
      }
  }

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement the StockQuoteService interface as per the contracts. Call Alphavantage service
  //  to fetch daily adjusted data for last 20 years.
  //  Refer to documentation here: https://www.alphavantage.co/documentation/
  //  --
  //  The implementation of this functions will be doing following tasks:
  //    1. Build the appropriate url to communicate with third-party.
  //       The url should consider startDate and endDate if it is supported by the provider.
  //    2. Perform third-party communication with the url prepared in step#1
  //    3. Map the response and convert the same to List<Candle>
  //    4. If the provider does not support startDate and endDate, then the implementation
  //       should also filter the dates based on startDate and endDate. Make sure that
  //       result contains the records for for startDate and endDate after filtering.
  //    5. Return a sorted List<Candle> sorted ascending based on Candle#getDate
  // Note:
  // 1. Make sure you use {RestTemplate#getForObject(URI, String)} else the test will fail.
  // 2. Run the tests using command below and make sure it passes:
  //    ./gradlew test --tests AlphavantageServiceTest
  //CHECKSTYLE:OFF
    //CHECKSTYLE:ON
  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  1. Write a method to create appropriate url to call Alphavantage service. The method should
  //     be using configurations provided in the {@link @application.properties}.
  //  2. Use this method in #getStockQuote.

  public static void main(String[] args) throws JsonProcessingException, StockQuoteServiceException {
    RestTemplate r = new RestTemplate();
    AlphavantageService al = new AlphavantageService(r);
    al.getStockQuote("AAPL",LocalDate.parse("2020-11-12"),LocalDate.parse("2020-12-12"));
    System.out.println("success");
  }

}

