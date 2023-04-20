package io.fluentqa.agile.repo;

import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import java.util.Collection;
//PageableHandlerMethodArgumentResolverCustomizer
//SortHandlerMethodArgumentResolverCustomizer
//QuerydslPredicateExecutor
//QuerydslBinderCustomizer
//https://blinkfox.github.io/
//https://techwithmaddy.com/
//https://github.com/tratif/jvm-bloggers
//https://howtodoinjava.com/spring/spring-sql-sqlgroup-and-sqlmergemode/
//https://github.com/OpenLiberty/open-liberty
//https://docs.spring.io/spring-data/commons/docs/current/reference/html/
//https://www.jpa-buddy.com/documentation/dto-generator/#java-records-support
public class AnAggregateRoot {

    @DomainEvents
    Collection<Object> domainEvents() {
        // … return events you want to get published here
      return null;
    }

    @AfterDomainEventPublication
    void callbackMethod() {
       // … potentially clean up domain events list
    }
}