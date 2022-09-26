package ua.nebezzpeka.bookshopsalemicro.service;

import ua.nebezzpeka.bookshopsalemicro.entity.Book;
import ua.nebezzpeka.bookshopsalemicro.entity.Customer;
import ua.nebezzpeka.bookshopsalemicro.entity.Item;
import ua.nebezzpeka.bookshopsalemicro.entity.Order;
import ua.nebezzpeka.bookshopsalemicro.model.ItemDto;
import ua.nebezzpeka.bookshopsalemicro.repository.BookRepository;
import ua.nebezzpeka.bookshopsalemicro.repository.CustomerRepository;
import ua.nebezzpeka.bookshopsalemicro.repository.ItemRepository;
import ua.nebezzpeka.bookshopsalemicro.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Service
public class SaleService {
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    public SaleService(BookRepository bookRepository, CustomerRepository customerRepository, OrderRepository orderRepository, ItemRepository itemRepository) {
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public Order saleBook(String vendorCode, int bookCount, String userName) {
        Book book = bookRepository.getBookByVendorCode(vendorCode).orElseThrow(() -> {
            return new NullPointerException("Book by vendor code: " + vendorCode + ", not found.");
        });
        if(book.getCount() < bookCount) {
            throw new IllegalArgumentException("Not enough books.");
        }
        book.decrementBooks(bookCount);
        bookRepository.save(book);
        if(!customerRepository.existsByUserName(userName)) {
            throw new NullPointerException("Customer by user name: " + userName + ", not found.");
        }
        Item item = Item.builder()
                .vendorCode(book.getVendorCode())
                .count(bookCount)
                .price(book.getPrice())
                .build();
        Order order = Order.builder()
                .orderCode(UUID.randomUUID().toString())
                .userName(userName)
                .saleDate(LocalDateTime.now())
                .items(List.of(item))
                .build();
        item.setOrder(order);
        itemRepository.save(item);
        orderRepository.save(order);
        return order;
    }

    @Transactional
    public Order saleBooks(List<ItemDto> itemsDto, String userName) {
        if(!customerRepository.existsByUserName(userName)) {
            throw new NullPointerException("Customer by user name: " + userName + ", not found.");
        }
        List<Item> items = new ArrayList<>();
        Order order = Order.builder()
                .orderCode(UUID.randomUUID().toString())
                .userName(userName)
                .saleDate(LocalDateTime.now())
                .build();

        itemsDto.forEach(itemDto -> {
            Book book = bookRepository.getBookByVendorCode(itemDto.getVendorCode()).orElseThrow(() -> {
                throw new NullPointerException("Book by vendor code: " + itemDto.getVendorCode() + ", not found.");
            });
            if(book.getCount() < itemDto.getCount()) {
                throw new IllegalArgumentException("Not enough books.");
            }
            book.decrementBooks(itemDto.getCount());
            bookRepository.save(book);
            items.add(Item.builder()
                            .vendorCode(book.getVendorCode())
                            .count(book.getCount())
                            .price(book.getPrice())
                            .order(order)
                            .build());
        });
        order.setItems(items);
        itemRepository.saveAll(items);
        orderRepository.save(order);

//        List<Item> items = new ArrayList<>();
//        books.forEach(book -> {
//            items.add(Item.builder()
//                            .vendorCode(book.getVendorCode())
//                            .count(bo)
//                            .build());
//        });
        return order;
    }

    @Transactional
    public void refundBook(String orderCode, String userName) {
        if(!customerRepository.existsByUserName(userName)) {
            throw new NullPointerException("Customer by user name: " + userName + ", was not found.");
        }
        Order order = orderRepository.getSaleByOrderCodeAndUserName(orderCode, userName).orElseThrow(() -> {
            return new NullPointerException("Sale not found.");
        });
        List<Item> items = order.getItems();
        items.forEach(item -> {
            Book book = bookRepository.getBookByVendorCode(item.getVendorCode()).orElseThrow(() -> {
                return new NullPointerException("Book not found.");
            });
            book.incrementBooks(item.getCount());
        });
        orderRepository.delete(order);
    }

}
