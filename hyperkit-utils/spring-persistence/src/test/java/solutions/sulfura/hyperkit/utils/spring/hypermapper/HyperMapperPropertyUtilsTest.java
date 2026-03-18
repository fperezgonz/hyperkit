package solutions.sulfura.hyperkit.utils.spring.hypermapper;

import jakarta.persistence.Id;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapperPropertyUtils.PropertyDescriptor;

import static org.junit.jupiter.api.Assertions.*;

class HyperMapperPropertyUtilsTest {

    // Test classes
    @SuppressWarnings("unused")
    static class SimpleBean {
        public String publicField;
        private String privateField;

        public String getPrivateField() {
            return privateField;
        }

        public void setPrivateField(String privateField) {
            this.privateField = privateField;
        }

        public boolean isActive() {
            return true;
        }

        public void setActive(boolean active) {
            // Do nothing
        }
    }

    static class EntityBean {
        @Id
        public Long id;
        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    @DisplayName("getPropertiesMap should return all properties of a class")
    void getPropertiesMapShouldReturnAllProperties() {
        // Given a simple bean class

        // When getting the property map
        var propertiesMap = HyperMapperPropertyUtils.getPropertiesMap(SimpleBean.class);

        // Then it should contain all properties
        assertNotNull(propertiesMap);
        assertTrue(propertiesMap.containsKey("publicField"));
        assertTrue(propertiesMap.containsKey("privateField"));
        assertTrue(propertiesMap.containsKey("active"));
    }

    @Test
    @DisplayName("getProperties should return all property descriptors")
    void getPropertiesShouldReturnAllPropertyDescriptors() {
        // When getting properties
        Collection<PropertyDescriptor> properties =
                HyperMapperPropertyUtils.getProperties(SimpleBean.class);

        // Then it should return all property descriptors
        assertNotNull(properties);
        assertFalse(properties.isEmpty());
        assertEquals(3, properties.size());
    }

    @Test
    @DisplayName("getPropertyDescriptor should return descriptor for a specific property")
    void getPropertyDescriptorShouldReturnDescriptorForProperty() {
        // When getting property descriptor for a specific property
        PropertyDescriptor descriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(SimpleBean.class, "privateField");

        // Then it should return the correct descriptor
        assertNotNull(descriptor);
        assertEquals("privateField", descriptor.getPropertyName());
        assertTrue(descriptor.canRead());
        assertTrue(descriptor.canWrite());
    }

    @Test
    @DisplayName("getIdPropertyDescriptor should find property with @Id annotation")
    void getIdPropertyDescriptorShouldFindIdProperty() {
        // Given an entity bean with a property annotated with @Id

        // When getting ID property descriptor
        PropertyDescriptor idDescriptor =
                HyperMapperPropertyUtils.getIdPropertyDescriptor(EntityBean.class);

        // Then it should return the property with @Id annotation
        assertNotNull(idDescriptor);
        assertEquals("id", idDescriptor.getPropertyName());
        assertNotNull(idDescriptor.getAnnotation(Id.class));
    }

    @Test
    @DisplayName("getProperty should return property value")
    void getPropertyShouldReturnPropertyValue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Given a bean with property values
        SimpleBean bean = new SimpleBean();
        bean.publicField = "public value";
        bean.setPrivateField("private value");

        // When getting property values
        Object publicValue = HyperMapperPropertyUtils.getProperty(bean, "publicField");
        Object privateValue = HyperMapperPropertyUtils.getProperty(bean, "privateField");
        Object activeValue = HyperMapperPropertyUtils.getProperty(bean, "active");

        // Then it should return the correct values
        assertEquals("public value", publicValue);
        assertEquals("private value", privateValue);
        assertEquals(true, activeValue);
    }

    @Test
    @DisplayName("setProperty should set property value")
    void setPropertyShouldSetPropertyValue() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // Given a bean
        SimpleBean bean = new SimpleBean();

        // When setting property values
        HyperMapperPropertyUtils.setProperty(bean, "publicField", "new public value");
        HyperMapperPropertyUtils.setProperty(bean, "privateField", "new private value");

        // Then the values should be set correctly
        assertEquals("new public value", bean.publicField);
        assertEquals("new private value", bean.getPrivateField());
    }

    @Test
    @DisplayName("PropertyDescriptor.getValue should return property value")
    void propertyDescriptorGetValueShouldReturnPropertyValue() {
        // Given a bean with property values
        SimpleBean bean = new SimpleBean();
        bean.publicField = "test value";

        // When getting property descriptor and value
        PropertyDescriptor descriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(SimpleBean.class, "publicField");
        Object value = descriptor.getValue(bean);

        // Then it should return the correct value
        assertEquals("test value", value);
    }

    @Test
    @DisplayName("PropertyDescriptor.setValue should set property value")
    void propertyDescriptorSetValueShouldSetPropertyValue() {
        // Given a bean and property descriptor
        SimpleBean bean = new SimpleBean();
        PropertyDescriptor descriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(SimpleBean.class, "publicField");

        // When setting value through descriptor
        descriptor.setValue(bean, "descriptor set value");

        // Then the value should be set correctly
        assertEquals("descriptor set value", bean.publicField);
    }

    @Test
    @DisplayName("PropertyDescriptor.getAnnotation should return annotation from field")
    void propertyDescriptorGetAnnotationShouldReturnAnnotation() {
        // Given an entity bean with an annotated field
        PropertyDescriptor descriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(EntityBean.class, "id");

        // When getting annotation
        Id idAnnotation = descriptor.getAnnotation(Id.class);

        // Then it should return the annotation
        assertNotNull(idAnnotation);
    }

    @Test
    @DisplayName("PropertyDescriptor.canRead and canWrite should return correct values")
    void propertyDescriptorCanReadAndWriteShouldReturnCorrectValues() {
        // Given property descriptors
        PropertyDescriptor publicFieldDescriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(SimpleBean.class, "publicField");
        PropertyDescriptor privateFieldDescriptor =
                HyperMapperPropertyUtils.getPropertyDescriptor(SimpleBean.class, "privateField");

        // Then canRead and canWrite should return correct values
        assertTrue(publicFieldDescriptor.canRead());
        assertTrue(publicFieldDescriptor.canWrite());
        assertTrue(privateFieldDescriptor.canRead());
        assertTrue(privateFieldDescriptor.canWrite());
    }

}