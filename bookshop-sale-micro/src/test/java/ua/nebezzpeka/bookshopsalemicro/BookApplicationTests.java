package ua.nebezzpeka.bookshopsalemicro;

import ua.nebezzpeka.bookshopsalemicro.service.SaleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookApplicationTests {

	@Autowired
	SaleService saleService;

	@Test
	void saleBook() {
		saleService.saleBook("111111", 1, "Yurii");
	}

}
