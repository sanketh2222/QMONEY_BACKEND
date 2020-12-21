
package com.crio.warmup.stock.portfolio;

import com.crio.warmup.stock.quotes.StockQuoteServiceFactory;
import com.crio.warmup.stock.quotes.StockQuotesService;


import org.springframework.web.client.RestTemplate;

public class PortfolioManagerFactory {

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Implement the method to return new instance of PortfolioManager.
  //  Remember, pass along the RestTemplate argument that is provided to the new instance.
  private StockQuotesService stockQuotesService;
  
  public static PortfolioManager getPortfolioManager(RestTemplate restTemplate) {
    //  restTemplate = new RestTemplate();
    return  new PortfolioManagerImpl(restTemplate);
    // return p;//need to change
  }
  public static PortfolioManager getPortfolioManager(String provider,RestTemplate restTemplate) {
    //  restTemplate = new RestTemplate();
    return  new PortfolioManagerImpl(restTemplate);
    // return p;//need to change
  }




}
