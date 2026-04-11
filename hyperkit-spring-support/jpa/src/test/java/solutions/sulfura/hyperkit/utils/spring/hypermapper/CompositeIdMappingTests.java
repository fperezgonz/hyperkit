package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.test.model.dtos.ProductDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.StockDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.StockKeyDto;
import solutions.sulfura.hyperkit.utils.test.model.dtos.WarehouseLocationDto;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Product;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.Stock;
import solutions.sulfura.hyperkit.utils.test.model.scm.inventory.WarehouseLocation;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class CompositeIdMappingTests {

    @Autowired
    private HyperMapper<Object> dtoMapper;
    @Autowired
    private HyperRepositoryImpl<Object> hyperRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @DisplayName("Should map and persist new entity with composite id")
    @Transactional
    void testMapAndPersistCompositeId() {
        // Given a persisted warehouse location, a persisted product and a dto of a non-persisted stock
        WarehouseLocation location = new WarehouseLocation();
        location.id = "LOC1";
        location.name = "Location 1";
        hyperRepository.save(location, null);

        Product product = new Product();
        product.id = "PROD1";
        product.sku = "SKU1";
        product.name = "Product 1";
        hyperRepository.save(product, null);

        entityManager.flush();

        StockDto stockDto = new StockDto();
        StockKeyDto id = new StockKeyDto();
        id.warehouseLocation = ValueWrapper.of(WarehouseLocationDto.Builder.newInstance()
                .id(ValueWrapper.of(location.id))
                .build());
        id.product = ValueWrapper.of(ProductDto.Builder.newInstance()
                .id(ValueWrapper.of(product.id))
                .build());

        stockDto.id = ValueWrapper.of(id);
        stockDto.quantity = ValueWrapper.of(new BigDecimal("100.00"));

        // When persisting the stock dto
        HyperMapper.MappingResult<?> resultObj = dtoMapper.mapDtoToEntity(stockDto, null);
        Stock stock = (Stock) resultObj.mappedValue();

        // Then the resulting stock should have the correct composite id and load the persisted entities referenced by the composite id
        assertNotNull(stock);
        assertNotNull(stock.id);
        assertEquals("LOC1", stock.id.warehouseLocation.id);
        assertEquals("PROD1", stock.id.product.id);
        assertEquals(location.id, stock.id.warehouseLocation.id);
        assertEquals(location.name, stock.id.warehouseLocation.name);
        assertEquals(product.id, stock.id.product.id);
        assertEquals(product.sku, stock.id.product.sku);
        assertEquals(product.name, stock.id.product.name);
    }

    @Test
    @DisplayName("Should map and update persisted entity data with composite id")
    @Transactional
    void testMapAndUpdatePersistedCompositeId() {
        // Given a persisted warehouse location, a persisted product and a dto of a non-persisted stock
        WarehouseLocation location = new WarehouseLocation();
        location.id = "LOC1";
        location.name = "Location 1";
        hyperRepository.save(location, null);

        Product product = new Product();
        product.id = "PROD1";
        product.sku = "SKU1";
        product.name = "Product 1";
        hyperRepository.save(product, null);

        entityManager.flush();

        Stock stock = new Stock();
        stock.id = new Stock.StockKey();
        stock.id.product = product;
        stock.id.warehouseLocation = location;
        stock.quantity = BigDecimal.ZERO;
        hyperRepository.save(stock, null);

        StockDto stockDto = new StockDto();
        StockKeyDto id = new StockKeyDto();
        id.warehouseLocation = ValueWrapper.of(WarehouseLocationDto.Builder.newInstance()
                .id(ValueWrapper.of(location.id))
                .build());
        id.product = ValueWrapper.of(ProductDto.Builder.newInstance()
                .id(ValueWrapper.of(product.id))
                .build());

        stockDto.id = ValueWrapper.of(id);
        stockDto.quantity = ValueWrapper.of(new BigDecimal("100.00"));

        // When persisting the stock dto
        var result = dtoMapper.mapDtoToEntity(stockDto, null);
        Stock updatedStock = result.mappedValue();

        // Then the resulting stock should have the correct composite id and load the persisted entities referenced by the composite id
        assertNotNull(updatedStock);
        assertNotNull(updatedStock.id);
        assertEquals(location.id, updatedStock.id.warehouseLocation.id);
        assertEquals(location.name, updatedStock.id.warehouseLocation.name);
        assertEquals(product.id, updatedStock.id.product.id);
        assertEquals(product.sku, updatedStock.id.product.sku);
        assertEquals(product.name, updatedStock.id.product.name);
    }
}
