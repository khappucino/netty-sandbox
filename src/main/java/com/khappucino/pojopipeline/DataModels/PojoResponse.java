package com.khappucino.pojopipeline.DataModels;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
