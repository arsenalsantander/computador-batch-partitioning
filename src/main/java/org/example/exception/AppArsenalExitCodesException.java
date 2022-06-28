package org.example.exception;

import lombok.AllArgsConstructor;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class AppComputadorExitCodesException extends RuntimeException implements ExitCodeGenerator {

  private static final long serialVersionUID = -8330713163893934146L;

  public AppComputadorExitCodesException(String message) {
    super(message);
  }

  @Override
  public int getExitCode() {
    return 1;
  }
}