package com.backend.application.repository;

import com.backend.application.entities.UserTrade;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

public class UserTradeSpecifications {

  public static Specification<UserTrade> hasInstrumentIn(List<String> instruments) {
    return (root, query, criteriaBuilder) -> {
      if (instruments == null || instruments.isEmpty()) {
        return criteriaBuilder.conjunction();
      }
      return root.get("instrument").in(instruments);
    };
  }

  public static Specification<UserTrade> hasDateGreaterThanOrEqualTo(LocalDate beginDate) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("date"), beginDate);
  }

  public static Specification<UserTrade> hasDateLessThanOrEqualTo(LocalDate endDate) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate);
  }
}
