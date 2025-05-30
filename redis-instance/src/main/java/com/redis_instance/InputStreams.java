package com.redis_instance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

class InputStreams {

  static String asString(InputStream inputStream) {
    return tryToConvertToString(inputStream, UTF_8);
  }

  static byte[] asByteArray(InputStream inputStream) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (inputStream) {
      transferBytes(inputStream, byteArrayOutputStream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return byteArrayOutputStream.toByteArray();
  }

  static String tryToConvertToString(InputStream inputStream, Charset charset) {
    Scanner scanner = new Scanner(inputStream, charset).useDelimiter("\\A");
    String response = scanner.hasNext() ? scanner.next() : "";
    return response.replaceAll("\u0000.*", "");
  }

  private static void transferBytes(
    InputStream inputStream,
    ByteArrayOutputStream byteArrayOutputStream
  ) throws IOException {
    byte[] byteChunk = new byte[4096];
    int n;
    while ((n = inputStream.read(byteChunk)) > 0)
      byteArrayOutputStream.write(byteChunk, 0, n);
  }

}
