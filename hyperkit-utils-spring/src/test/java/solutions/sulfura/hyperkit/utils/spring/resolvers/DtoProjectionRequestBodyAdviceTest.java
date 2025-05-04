package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DtoProjectionRequestBodyAdviceTest {

    @Autowired
    private DtoProjectionRequestBodyAdvice resolver;

    @Test
    @DisplayName("Should support parameters with @DtoProjectionSpec annotation")
    void supports_shouldSupportParametersWithDtoProjectionSpecAnnotation() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(DtoProjectionSpec.class)).thenReturn(true);
        Type targetType = TestDto.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        boolean result = resolver.supports(parameter, targetType, converterType);

        // Then
        assertTrue(result);

    }

    @Test
    @DisplayName("Should support parameters with annotation annotated with @DtoProjectionSpec")
    void supports_shouldSupportParametersWithMetaAnnotation() throws NoSuchMethodException {
        // Given
        Method method = getClass().getDeclaredMethod("methodWithCustomAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        Type targetType = TestDto.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        boolean result = resolver.supports(parameter, targetType, converterType);

        // Then
        assertTrue(result);

    }

    @Test
    @DisplayName("Should not support parameters without @DtoProjectionSpec annotation")
    void supports_shouldNotSupportParametersWithoutAnnotation() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(DtoProjectionSpec.class)).thenReturn(false);
        when(parameter.getParameterAnnotations()).thenReturn(new java.lang.annotation.Annotation[0]);
        Type targetType = TestDto.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        boolean result = resolver.supports(parameter, targetType, converterType);

        // Then
        assertFalse(result);

    }

    @Test
    @DisplayName("Should apply projection to DTO in afterBodyRead")
    void afterBodyRead_shouldApplyProjectionToDto() throws Exception {
        // Given
        Method method = getClass().getDeclaredMethod("methodWithDirectAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        TestDto testDto = new TestDto(1L, "Test", 25);
        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        Type targetType = TestDto.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        Object result = resolver.afterBodyRead(testDto, inputMessage, parameter, targetType, converterType);

        // Then
        assertNotNull(result);
        assertInstanceOf(TestDto.class, result);
        TestDto dto = (TestDto) result;
        assertEquals(ValueWrapper.empty(), dto.id);
        assertEquals(testDto.name, dto.name);
        assertEquals(testDto.age, dto.age);

    }

    @Test
    @DisplayName("Should apply projection to DTO with meta-annotation in afterBodyRead")
    void afterBodyRead_shouldApplyProjectionToDtoWithMetaAnnotation() throws Exception {
        // Given
        Method method = getClass().getDeclaredMethod("methodWithCustomAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        TestDto testDto = new TestDto(1L, "Test", 25);
        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        Type targetType = TestDto.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        Object result = resolver.afterBodyRead(testDto, inputMessage, parameter, targetType, converterType);

        // Then
        assertNotNull(result);
        assertInstanceOf(TestDto.class, result);
        TestDto dto = (TestDto) result;
        assertEquals(ValueWrapper.empty(), dto.id);
        assertEquals(testDto.name, dto.name);
        assertEquals(testDto.age, dto.age);

    }

    @Test
    @DisplayName("Should apply projection to DTOs in StdDtoRequestBody in afterBodyRead")
    void afterBodyRead_shouldApplyProjectionToDtosInStdDtoRequestBody() throws Exception {
        // Given
        Method method = getClass().getDeclaredMethod("methodUsingDtoStdRequest", StdDtoRequestBody.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        TestDto testDto = new TestDto(1L, "Test", 25);
        StdDtoRequestBody<TestDto> dtoRequest = new StdDtoRequestBody<>();
        dtoRequest.setData(List.of(testDto));
        HttpInputMessage inputMessage = mock(HttpInputMessage.class);
        Type targetType = StdDtoRequestBody.class;
        Class<? extends HttpMessageConverter<?>> converterType = MappingJackson2HttpMessageConverter.class;

        // When
        Object result = resolver.afterBodyRead(dtoRequest, inputMessage, parameter, targetType, converterType);

        // Then
        assertNotNull(result);
        assertInstanceOf(StdDtoRequestBody.class, result);
        @SuppressWarnings("unchecked")
        StdDtoRequestBody<TestDto> resultRequest = (StdDtoRequestBody<TestDto>) result;
        assertEquals(1, resultRequest.getData().size());
        TestDto dto = resultRequest.getData().getFirst();
        assertEquals(ValueWrapper.empty(), dto.id);
        assertEquals(testDto.name, dto.name);
        assertEquals(testDto.age, dto.age);

    }

    // Method used for testing
    @SuppressWarnings("unused")
    void methodWithDirectAnnotation(@DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age") TestDto dto) {
    }

    // Method used for testing
    @SuppressWarnings("unused")
    void methodWithCustomAnnotation(@TestProjection TestDto dto) {
    }

    // Method used for testing
    @SuppressWarnings("unused")
    void methodUsingDtoStdRequest(@DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age") StdDtoRequestBody<TestDto> dto) {
    }

    //AnnotationUsedForTesting
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age")
    @interface TestProjection {
    }

}
