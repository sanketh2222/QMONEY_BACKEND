package com.crio.warmup.stock.exception;

public class StockQuoteServiceException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public StockQuoteServiceException(String s) 
    { 
        // Call constructor of parent Exception 
        super(s); 
    } 

}
