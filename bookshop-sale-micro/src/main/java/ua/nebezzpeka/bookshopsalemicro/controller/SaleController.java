package ua.nebezzpeka.bookshopsalemicro.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.nebezzpeka.bookshopsalemicro.entity.Order;
import ua.nebezzpeka.bookshopsalemicro.model.ItemDto;
import ua.nebezzpeka.bookshopsalemicro.model.OrderDto;
import ua.nebezzpeka.bookshopsalemicro.service.SaleService;

@RestController
@RequestMapping("/shop")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping(
            value = "/sale",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Order> sale(@RequestBody ItemDto itemDto) {
        Order order = saleService.saleBook(itemDto.getVendorCode(), itemDto.getCount(), "Yurii");
        return ResponseEntity.ok(order);
    }

    @PostMapping(
            value = "/refund",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity.BodyBuilder refund(@RequestBody OrderDto orderDto) {
        saleService.refundBook(orderDto.getOrderCode(), "Yurii");
        return ResponseEntity.ok();
    }

}
