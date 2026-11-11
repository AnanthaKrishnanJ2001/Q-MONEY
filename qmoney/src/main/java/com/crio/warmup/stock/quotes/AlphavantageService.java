
package com.crio.warmup.stock.quotes;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;
import com.crio.warmup.stock.dto.AlphavantageCandle;
import com.crio.warmup.stock.dto.AlphavantageDailyResponse;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.crio.warmup.stock.util.AlphavantageCandleComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class AlphavantageService implements StockQuotesService {

private static String token = "620ZQZ9LQJTB9OR7";
private RestTemplate restTemplate;

protected AlphavantageService(RestTemplate restTemplate) {
  this.restTemplate = restTemplate;
}

public static String getToken() {
  return token;
}

private ObjectMapper getObjectMapper(){
  ObjectMapper objectMapper = new ObjectMapper();
  objectMapper.registerModule(new JavaTimeModule());
  return objectMapper;
}

protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
  // String uri = "https://api.tiingo.com/tiingo/daily/"+ symbol +"/prices?startDate="+ startDate +"&endDate="+ endDate +"&token=" + token;
  String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+ symbol +"&apikey=" + token;
  return url;
}


@Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
    // TODO Auto-generated method stub
    // String url = buildUri(symbol, from, to);
    // String dailyResponseString = this.restTemplate.getForObject(url, String.class);
    // AlphavantageDailyResponse dailyResponse = getObjectMapper().readValue(dailyResponseString, AlphavantageDailyResponse.class);
    // List<Candle> alphaCandles = new ArrayList<>();
    // for(LocalDate date : dailyResponse.getCandles().keySet()){
    //   AlphavantageCandle candle = dailyResponse.getCandles().get(date);
    //   candle.setDate(date);

    //   if(date.isEqual(from) || date.isEqual(to) || (date.isBefore(to) && date.isAfter(from))){
    //       alphaCandles.add(candle);
    //   }
    // }
    // Collections.sort(alphaCandles, new AlphavantageCandleComparator());
    // return alphaCandles;

    String url = buildUri(symbol, from, to);

    try {
      String dailyResponseString = this.restTemplate.getForObject(url, String.class);
  
      if (dailyResponseString == null || dailyResponseString.isEmpty()) {
        throw new StockQuoteServiceException("Empty response from AlphaVantage API for symbol: " + symbol);
      }
  
      AlphavantageDailyResponse dailyResponse = getObjectMapper()
          .readValue(dailyResponseString, AlphavantageDailyResponse.class);
  
      if (dailyResponse.getCandles() == null || dailyResponse.getCandles().isEmpty()) {
        throw new StockQuoteServiceException("No candle data found for symbol: " + symbol);
      }
  
      List<Candle> alphaCandles = new ArrayList<>();
  
      for (LocalDate date : dailyResponse.getCandles().keySet()) {
        if (date != null &&
            (date.isEqual(from) || date.isEqual(to) || (date.isAfter(from) && date.isBefore(to)))) {
          AlphavantageCandle candle = dailyResponse.getCandles().get(date);
          if (candle != null) {
            candle.setDate(date);
            alphaCandles.add(candle);
          }
        }
      }
  
      if (alphaCandles.isEmpty()) {
        throw new StockQuoteServiceException("No candles in selected date range for symbol: " + symbol);
      }
  
      Collections.sort(alphaCandles, new AlphavantageCandleComparator());
  
      return alphaCandles;
  
    } catch (JsonProcessingException e) {
      throw new StockQuoteServiceException("Failed to parse AlphaVantage response for symbol: " + symbol, e);
    } catch (Exception e) {
      throw new StockQuoteServiceException("Unexpected error occurred in AlphaVantageService", e);
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
  //  IMP: Do remember to write readable and maintainable code, There will be few functions like
  //    Checking if given date falls within provided date range, etc.
  //    Make sure that you write Unit tests for all such functions.
  //  Note:
  //  1. Make sure you use {RestTemplate#getForObject(URI, String)} else the test will fail.
  //  2. Run the tests using command below and make sure it passes:
  //    ./gradlew test --tests AlphavantageServiceTest
  //CHECKSTYLE:OFF
    //CHECKSTYLE:ON
  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  1. Write a method to create appropriate url to call Alphavantage service. The method should
  //     be using configurations provided in the {@link @application.properties}.
  //  2. Use this method in #getStockQuote.
  }
}

