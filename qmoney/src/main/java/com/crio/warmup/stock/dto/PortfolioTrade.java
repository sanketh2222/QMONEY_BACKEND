package com.crio.warmup.stock.dto;

// import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

// import com.fasterxml.jackson.databind.annotation.JsonSerialize;
// import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
// import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.LocalDate;



public class PortfolioTrade {

  public PortfolioTrade() {  }

  public static enum TradeType {
    BUY,
    SELL
  }

  private String symbol;
  private int quantity;
  private TradeType tradeType;

  // @JsonDeserialize(using = LocalDateDeserializer.class)
  // @JsonSerialize(using = LocalDateSerializer.class)
  private LocalDate purchaseDate;

  public PortfolioTrade(String symbol, int quantity, LocalDate purchaseDate) {
    this.symbol = symbol;
    this.quantity = quantity;
    this.purchaseDate = purchaseDate;
    this.tradeType = TradeType.BUY;
  }

 

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setTradeType(TradeType tradeType) {
    this.tradeType = tradeType;
  }

  public void setPurchaseDate(LocalDate purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  //solution
  public String getSymbol() {
    return symbol;
  }

  public int getQuantity() {
    return quantity;
  }

  public LocalDate getPurchaseDate() {
    return purchaseDate;
  }

  public TradeType getTradeType() {
    return tradeType;
  }
  //solution

  @Override
  public String toString() {
    return "PortfolioTrade [purchaseDate=" + purchaseDate + ", quantity="
        + quantity + ", symbol=" + symbol
        + ", tradeType=" + tradeType + "]";
  }


  // public static void main(String[] args) {

  //   // PortfolioTrade tr1=new PortfolioTrade("AAPL",100,"2020-10-13");
  //   // PortfolioTrade tr=new PortfolioTrade("AAPL",100,LocalDate.parse("2020-10-12"));
   
  //   // String res=tr1.toString();
  //   // String res1=tr.toString();
  //   // System.out.println(res);
  //   // System.out.println(res1);
  //   // System.out.println(tr.getPurchaseDate());
  //   // System.out.println(tr1.getPurchaseDate());
  // }

}
