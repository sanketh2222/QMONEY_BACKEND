package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.AnnualizedReturn;

import java.io.Serializable;
import java.util.Comparator;



class AnualSort implements Comparator<AnnualizedReturn>, Serializable {

    
  private static final long serialVersionUID = 1L;

  @Override
  public int compare(AnnualizedReturn a1, AnnualizedReturn a2) {
   
    if (a1.getAnnualizedReturn() > a2.getAnnualizedReturn()) {
      return -1;
    } else {
      return 1;
    }
  }
    
}