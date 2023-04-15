package io.fluentqa.agile;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
//@SpringBootConfiguration
@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("io.fluentqa")
public class AgileTestApp {

  public static void main(String[] args) {
    SpringApplication.run(AgileTestApp.class);
  }
}
