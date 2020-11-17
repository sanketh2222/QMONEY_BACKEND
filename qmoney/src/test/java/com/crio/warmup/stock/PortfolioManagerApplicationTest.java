
package com.crio.warmup.stock;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import java.util.List;

import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;

class PortfolioManagerApplicationTest {

  @Test
  void mainReadFile() throws Exception {
    // given
    String filename = "trades.json";
    List<String> expected = Arrays.asList(new String[] { "AAPL", "MSFT", "GOOGL" });

    // when
    List<String> results = PortfolioManagerApplication
        .mainReadFile(new String[] { filename });// np

    // then
    System.out.print("actual is " + results);
    System.out.print("expected is " + expected);
    Assertions.assertEquals(expected, results);
  }

  @Test
  void mainReadQuotes() throws Exception {
    // given
    String filename = "trades.json";
    List<String> expected = Arrays.asList(new String[] { "MSFT", "AAPL", "GOOGL" });

    // when
    List<String> actual = PortfolioManagerApplication
        .mainReadQuotes(new String[] { filename, "2019-12-12" });// np

    // then
    System.out.print("actual is " + actual);
    System.out.print("expected is " + expected);
    Assertions.assertEquals(expected, actual);
  }

  @Test
  public void testDebugValues() {
    List<String> responses = PortfolioManagerApplication.debugOutputs();
    Assertions.assertTrue(responses.get(0).contains("trades.json"));
  }


}

