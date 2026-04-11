package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.annotations.Dto;
import solutions.sulfura.hyperkit.utils.spring.HyperRepositoryImpl;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.PlainStockKeyDto;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto.StockWithPlainCompositeKeyDto;
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

    WarehouseLocation createTestWarehouseLocation(String id, String name) {
        WarehouseLocation location = new WarehouseLocation();
        location.id = id;
        location.name = name;
        return hyperRepository.save(location, null);
    }

    Product createTestProduct(String id, String sku, String name) {
        Product product = new Product();
        product.id = id;
        product.sku = sku;
        product.name = name;
        return hyperRepository.save(product, null);
    }

    Stock createTestStock(WarehouseLocation location, Product product, BigDecimal quantity) {
        Stock stock = new Stock();
        stock.id = new Stock.StockKey();
        stock.id.product = product;
        stock.id.warehouseLocation = location;
        stock.quantity = quantity;
        return hyperRepository.save(stock, null);
    }

    StockWithPlainCompositeKey createTestStockWithPlainCompositeKey(String warehouseLocationId, String productId, BigDecimal quantity) {
        StockWithPlainCompositeKey stock = new StockWithPlainCompositeKey();
        stock.id = new StockWithPlainCompositeKey.PlainStockKey();
        stock.id.productId = productId;
        stock.id.warehouseLocationId = warehouseLocationId;
        stock.quantity = quantity;
        return hyperRepository.save(stock, null);
    }

    @Test
    @DisplayName("Should map and persist new entity with composite id")
    @Transactional
    void testMapAndPersistCompositeId() {
        // Given a persisted warehouse location, a persisted product and a dto of a non-persisted stock
        WarehouseLocation location = createTestWarehouseLocation("LOC1", "Location 1");
        Product product = createTestProduct("PROD1", "SKU1", "Product 1");

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
        WarehouseLocation location = createTestWarehouseLocation("LOC1", "Location 1");
        Product product = createTestProduct("PROD1", "SKU1", "Product 1");
        Stock stock = createTestStock(location, product, BigDecimal.ZERO);
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


    @Test
    @DisplayName("Should map and persist new entity with composite id made of non-entity types")
    @Transactional
    void testMapAndPersistPlainCompositeId() {

        String locationId = "LOC1";
        String productId = "PROD1";

        StockWithPlainCompositeKeyDto plainStockDto = new StockWithPlainCompositeKeyDto();
        PlainStockKeyDto id = new PlainStockKeyDto();
        id.warehouseLocationId = ValueWrapper.of(locationId);
        id.productId = ValueWrapper.of(productId);

        plainStockDto.id = ValueWrapper.of(id);
        plainStockDto.quantity = ValueWrapper.of(new BigDecimal("100.00"));

        // When persisting the stock dto
        HyperMapper.MappingResult<?> resultObj = dtoMapper.mapDtoToEntity(plainStockDto, null);
        StockWithPlainCompositeKey plainStock = (StockWithPlainCompositeKey) resultObj.mappedValue();

        // Then the resulting stock should have the correct composite id and load the persisted entities referenced by the composite id
        assertNotNull(plainStock);
        assertNotNull(plainStock.id);
        assertEquals("LOC1", plainStock.id.warehouseLocationId);
        assertEquals("PROD1", plainStock.id.productId);
        assertEquals(locationId, plainStock.id.warehouseLocationId);
        assertEquals(productId, plainStock.id.productId);
    }

    @Test
    @DisplayName("Should map and update persisted entity data with composite id made of non-entity types")
    @Transactional
    void testMapAndUpdatePersistedPlainCompositeId() {

        String locationId = "LOC1";
        String productId = "PROD1";

        StockWithPlainCompositeKey stock = createTestStockWithPlainCompositeKey(locationId, productId, BigDecimal.valueOf(5));

        entityManager.flush();

        StockWithPlainCompositeKeyDto stockDto = new StockWithPlainCompositeKeyDto();
        PlainStockKeyDto id = new PlainStockKeyDto();
        id.warehouseLocationId = ValueWrapper.of(locationId);
        id.productId = ValueWrapper.of(productId);

        stockDto.id = ValueWrapper.of(id);
        stockDto.quantity = ValueWrapper.of(new BigDecimal("100.00"));

        // When persisting the stock dto
        var result = dtoMapper.mapDtoToEntity(stockDto, null);
        StockWithPlainCompositeKey updatedStock = result.mappedValue();

        // Then the resulting stock should have the correct composite id and load the persisted entities referenced by the composite id
        assertNotNull(updatedStock);
        assertNotNull(updatedStock.id);
        assertEquals(locationId, updatedStock.id.warehouseLocationId);
        assertEquals(productId, updatedStock.id.productId);
    }

    @Entity
    @Dto(destPackageName = "solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto")
    public static class StockWithPlainCompositeKey {
        @EmbeddedId
        public PlainStockKey id;
        public BigDecimal quantity;

        @Embeddable
        @Dto(destPackageName = "solutions.sulfura.hyperkit.utils.spring.hypermapper.entities.dto")
        public static class PlainStockKey {
            public String warehouseLocationId;
            public String productId;
        }
    }

}
