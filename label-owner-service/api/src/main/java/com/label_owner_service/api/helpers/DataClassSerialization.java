package com.label_owner_service.api.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.util.concurrent.Callable;

public class DataClassSerialization {

  static final ObjectMapper MAPPER = JsonMapper.builder().build();

  public static String serialize(Object input) {
    return unchecked(() -> MAPPER.writeValueAsString(input));
  }

  public static <T> T deserialize(String source, Class<T> type) {
    return unchecked(() -> MAPPER.readValue(source, type));
  }

  private static <T> T unchecked(Callable<T> callable) {
    try {
      return callable.call();
    } catch (RuntimeException e) {
      throw e;
  }   catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
