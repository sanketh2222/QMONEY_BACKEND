
package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
// import java.io.FileWriter;
import java.io.IOException;
// import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.logging.log4j.ThreadContext;

import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestTemplate;

public class PortfolioManagerApplication {

  private static Map<Double, String> stocks = new HashMap<>();
  // static List<String> finalstocks = new ArrayList<>();

  private static Double  dateDiffDays(String start,LocalDate end) {
    
    long diff = ChronoUnit.DAYS.between(LocalDate.parse(start), end);
    Long l = Long.valueOf(diff);
    return l.doubleValue() / 365;
  }

  private static List<String> sortByKey() {

    List<String> finalstocks = new ArrayList<>();
    finalstocks.clear();
    // System.out.print(finalstocks);
    // System.out.print(stocks);
    // System.out.print("final stock before returning and clearing before loop" +
    // finalstocks);
    // System.out.print(" stock before returning and clearing before loop" +
    // stocks);
    TreeMap<Double, String> tm = new TreeMap<>();
    tm.putAll(stocks);
    for (Map.Entry<Double, String> entry : tm.entrySet()) {

      // System.out.println(entry.getValue());
      finalstocks.add(entry.getValue());

    }
    tm.clear();
    stocks.clear();
    // System.out.print("final stock before returning and clearing after loop" +
    // finalstocks);
    // System.out.print(" stock before returning and clearing before loop" +
    // stocks);
    return finalstocks;
  }
  

  //implementation with getForEntity
 
 
  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Read the json file provided in the argument[0]. The file will be available in the classpath.
  //    1. Use #resolveFileFromResources to get actual file from classpath.
  //    2. Extract stock symbols from the json file with ObjectMapper provided by #getObjectMapper.
  //    3. Return the list of all symbols in the same order as provided in json.

  //  Note:
  //  1. There can be few unused imports, you will need to fix them to make the build pass.
  //  2. You can use "./gradlew build" to check if your code builds successfully.

  public static List<String> mainReadFile(String[] args) 
      throws IOException, URISyntaxException, Exception {

    
    File ifile = resolveFileFromResources(args[0]);
    ObjectMapper objectMapper = new ObjectMapper();
    
    //  ObjectMapper obj = getObjectMapper();
   
    
    //  obj.get
    //  getObjectMapper()
    //  List<String> symbols= List<String>;
    // List symbol = new ArrayList();
    List<String> symbol = new ArrayList<>();
    //  ArrayList<String> gfg = new ArrayList<String>(); 
    // objectMapper.read
    

    PortfolioTrade[] trades = objectMapper.readValue(ifile, PortfolioTrade[].class);
    //  trades.getObjectMapper();
    // getObjectMapper(trades);
    
    for (PortfolioTrade trade:trades) {
      symbol.add(trade.getSymbol());
    }

    

    

    //  return Collections.emptyList();
    return symbol;

  }


  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.




  // TODO: CRIO_TASK_MODULE_REST_API
  //  Find out the closing price of each stock on the end_date and return the list
  //  of all symbols in ascending order by its close value on end date.

  // Note:
  // 1. You may have to register on Tiingo to get the api_token.
  // 2. Look at args parameter and the module instructions carefully.
  // 2. You can copy relevant code from #mainReadFile to parse the Json.
  // 3. Use RestTemplate#getForObject in order to call the API,
  // and deserialize the results in List<Candle>
  // Note:
  // Remember to confirm that you are getting same results for annualized returns as in Module 3.
  // @Serv

  //implementation with getForObject
  public static List<String> mainReadQuotes(String[] args) 
      throws IOException, URISyntaxException {
    RestTemplate rest = new RestTemplate();
    String endDate = args[1];
    stocks.clear();
    
    
    
    File ifile = resolveFileFromResources(args[0]);
    ObjectMapper obj = new ObjectMapper();
    PortfolioTrade[] trades = obj.readValue(ifile, PortfolioTrade[].class);//failing here
    for (PortfolioTrade trd:trades) {

      // String url = "https://api.tiingo.com/tiingo/daily/" + trd.getSymbol()
      //     + "/prices?startDate=" + trd.getPurchaseDate() + "&endDate=" 
      //     + endDate + "&token=0175e650eb18193394fdc2c225b0c0ba954fa0a4";
      String url = "https://api.tiingo.com/tiingo/daily/" + trd.getSymbol()
          + "/prices?startDate=" + trd.getPurchaseDate().toString() + "&endDate={endDate}"
          + "&token=0175e650eb18193394fdc2c225b0c0ba954fa0a4";
      // URI uri = new URI(url);
      List<Double> close = new ArrayList<>();
      
      
      // TiingoCandle[] emps = rest.getForObject(uri, TiingoCandle[].class);
      // ResponseEntity<TiingoCandle[]> emps=rest.getForEntity(url, TiingoCandle[].class, endDate);
      TiingoCandle[] emps = rest.getForObject(url, TiingoCandle[].class, endDate);//pojo
      // String value = rest.getForObject(url, String.class, endDate);
      // File f=new File("treeres.json");
      // obj.defaultPrettyPrintingWriter()
      // obj.writerWithDefaultPrettyPrinter().writeValueAsString(value);
      // FileWriter myWriter = new FileWriter("treeres.json");
      // myWriter.write(obj.writerWithDefaultPrettyPrinter().writeValueAsString(value));
      
      
      if (emps != null) {
        for (TiingoCandle c:emps) {
        
          close.add(c.getClose());
        
        }
      } else {
        System.out.print("invalid");
      }
      
      Double cprice = close.get(close.size() - 1);
      System.out.print(cprice);//
      
      
      stocks.put(cprice, trd.getSymbol());
      //  TotalReturnsDto ret = new TotalReturnsDto(trd.getSymbol(),cprice);
       
    }

    return sortByKey();
    // System.out.print("the value of ");
    
    // return finalstocks;
  }



  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    return Paths.get(
        Thread.currentThread().getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }


  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  //  Follow the instructions provided in the task documentation and fill up the correct values for
  //  the variables provided. First value is provided for your reference.
  //  A. Put a breakpoint on the first line inside mainReadFile() which says
  //    return Collections.emptyList();
  //  B. Then Debug the test #mainReadFile provided in PortfoliomanagerApplicationTest.java
  //  following the instructions to run the test.
  //  Once you are able to run the test, perform following tasks and record the output as a
  //  String in the function below.
  //  Use this link to see how to evaluate expressions -
  //  https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  //  1. evaluate the value of "args[0]" and set the value
  //     to the variable named valueOfArgument0 (This is implemented for your reference.)
  //  2. In the same window, evaluate the value of expression below and set it
  //  to resultOfResolveFilePathArgs0
  //     expression ==> resolveFileFromResources(args[0])
  //  3. In the same window, evaluate the value of expression below and set it
  //  to toStringOfObjectMapper.
  //  You might see some garbage numbers in the output. Dont worry, its expected.
  //    expression ==> getObjectMapper().toString()
  //  4. Now Go to the debug window and open stack trace. Put the name of the function you see at
  //  second place from top to variable functionNameFromTestFileInStackTrace
  //  5. In the same window, you will see the line number of the function in the stack trace window.
  //  assign the same to lineNumberFromTestFileInStackTrace
  //  Once you are done with above, just run the corresponding test and
  //  make sure its working as expected. use below command to do the same.
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String resultOfResolveFilePathArgs0 = "trades.json";
    String toStringOfObjectMapper = "ObjectMapper";
    String functionNameFromTestFileInStackTrace = "mainReadFile";
    String lineNumberFromTestFileInStackTrace = "";


    return Arrays.asList(new String[]{valueOfArgument0, resultOfResolveFilePathArgs0,
        toStringOfObjectMapper, functionNameFromTestFileInStackTrace,
        lineNumberFromTestFileInStackTrace});
  }


  

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Return the populated list of AnnualizedReturn for all stocks.
  //  Annualized returns should be calculated in two steps:
  //   1. Calculate totalReturn = (sell_value - buy_value) / buy_value.
  //      1.1 Store the same as totalReturns
  //   2. Calculate extrapolated annualized returns by scaling the same in years span.
  //      The formula is:
  //      annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //      2.1 Store the same as annualized_returns
  //  Test the same using below specified command. The build should be successful.
  //     ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn


  
  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice)  {
    Double years = dateDiffDays(trade.getPurchaseDate().toString(), endDate);
    Double returns = (sellPrice - buyPrice) / buyPrice;
    Double annualized = Math.pow((1 + returns), (1 / years)) - 1;
    
    // AnnualizedReturn ret = new AnnualizedReturn(trade.getSymbol(), annualized, returns);
    // if (trade != null) {
    //   return new AnnualizedReturn(trade.getSymbol(), annualized, returns);
    // } else {
    //   return null;
    // }
    return new AnnualizedReturn(trade.getSymbol(), annualized, returns);
    
      
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Now that you have the list of PortfolioTrade and their data, calculate annualized returns
  //  for the stocks provided in the Json.
  //  Use the function you just wrote #calculateAnnualizedReturns.
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.

  // Note:
  // 1. You may need to copy relevant code from #mainReadQuotes to parse the Json.
  // 2. Remember to get the latest quotes from Tiingo API.

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
        
    List<AnnualizedReturn> trades = new ArrayList<>();
    List<Double> openprices = new ArrayList<>();
    List<Double> closeprices = new ArrayList<>();
    ObjectMapper obj = new ObjectMapper();
    RestTemplate rst = new RestTemplate();
    PortfolioTrade[] trds = obj.readValue(resolveFileFromResources(args[0]), 
      PortfolioTrade[].class);
    for (PortfolioTrade trd: trds) {
      String url = "https://api.tiingo.com/tiingo/daily/" + trd.getSymbol()
          + "/prices?startDate=" + trd.getPurchaseDate().toString() + "&endDate=" + args[1]
          + "&token=0175e650eb18193394fdc2c225b0c0ba954fa0a4";

      // PortfolioTrade[] tds=rst.getForObject(url, PortfolioTrade[].class);
      TiingoCandle[] tds = rst.getForObject(url, TiingoCandle[].class);

      if (tds != null) {
        for (TiingoCandle t: tds) {
          openprices.add(t.getOpen());
          closeprices.add(t.getClose());
        }
      }
      Double buyPrice = openprices.get(0);
      Double sellPrice = closeprices.get(closeprices.size() - 1);
        
      AnnualizedReturn ar = calculateAnnualizedReturns(LocalDate.parse(args[1]), trd, 
          buyPrice, sellPrice);
      trades.add(ar);
      System.out.println(trades);
      openprices.clear();
      closeprices.clear();
        
        
    }
   
    
    return trades;
    
  }













  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    // String filename = "trades.json";
    // List<String> results = PortfolioManagerApplication
    //     .mainReadFile(new String[]{filename});
    // System.out.print(actual);
    // restFuncEntity("trades.json");
    printJsonObject(mainCalculateSingleReturn(args));
    

  





    // printJsonObject(mainCalculateSingleReturn(args));

  }
}

