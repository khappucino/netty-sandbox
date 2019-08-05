package com.khappucino.pojopipeline.DataModels;

import java.nio.charset.StandardCharsets;

/**
 * PojoResponse is used to model the response data.
 * It encapsulates a String payload and it can generate the equivalent byte data from it.
 */
public class PojoResponse {
  private final String payload;

  public PojoResponse(String payload) {
      this.payload = payload;
  }

  public String getPayload() {
    return this.payload;
  }

  public byte[] getPayloadBytes() {
    return this.payload.getBytes(StandardCharsets.UTF_8);
  }
}
