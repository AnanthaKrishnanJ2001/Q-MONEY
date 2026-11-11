
package com.crio.warmup.stock.quotes;

import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.exception.StockQuoteServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class TiingoService implements StockQuotesService {

  private static String token = "bd726b474f5ed0a9adb9f87955c499df6eec8f65";

  public static String getToken() {
    return token;
  }

  private RestTemplate restTemplate;

  protected TiingoService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  private ObjectMapper getObjectMapper(){
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
    String url = "https://api.tiingo.com/tiingo/daily/"+ symbol +"/prices?startDate="+ startDate +"&endDate="+ endDate +"&token=" + token;
    return url;
  }

  @Override
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException, StockQuoteServiceException {
    // TODO Auto-generated method stub
  //     String url = buildUri(symbol, from, to);
  //     String candleString = this.restTemplate.getForObject(url, String.class);
  //   TiingoCandle[] candles = getObjectMapper().readValue(candleString, TiingoCandle[].class);
  //  return List.of(candles);

  String url = buildUri(symbol, from, to);
  try {
    String response = this.restTemplate.getForObject(url, String.class);
    if (response == null || response.isEmpty()) {
      throw new StockQuoteServiceException("Empty response from Tiingo");
    }

    TiingoCandle[] candles = getObjectMapper().readValue(response, TiingoCandle[].class);

    if (candles == null || candles.length == 0) {
      throw new StockQuoteServiceException("No data in Tiingo response");
    }

    return List.of(candles);

  } catch (HttpClientErrorException.TooManyRequests e) {
    throw new StockQuoteServiceException("Tiingo rate limit exceeded (429). Please try again later.", e);
  } catch (JsonProcessingException e) {
    throw new StockQuoteServiceException("Failed to parse Tiingo response", e);
  } catch (Exception e) {
    throw new StockQuoteServiceException("Unexpected error while calling Tiingo", e);
  }
  
  }


  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Implement getStockQuote method below that was also declared in the interface.

  // Note:
  // 1. You can move the code from PortfolioManagerImpl#getStockQuote inside newly created method.
  // 2. Run the tests using command below and make sure it passes.
  //    ./gradlew test --tests TiingoServiceTest


  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_ADDITIONAL_REFACTOR
  //  Write a method to create appropriate url to call the Tiingo API.

}
