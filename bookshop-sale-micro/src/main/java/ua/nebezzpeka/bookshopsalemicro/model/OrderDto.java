package ua.nebezzpeka.bookshopsalemicro.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDto {
    private String orderCode;
}
