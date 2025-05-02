package solutions.sulfura.hyperkit.utils.spring.resolvers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class DtoProjectionArgumentResolverTest {

    @Autowired
    private DtoProjectionArgumentResolver resolver;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Should support parameters with @DtoProjectionSpec annotation")
    void supportsParameter_shouldSupportParametersWithDtoProjectionSpecAnnotation() {

        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(DtoProjectionSpec.class)).thenReturn(true);

        // When
        boolean result = resolver.supportsParameter(parameter);

        // Then
        assertTrue(result);

    }

    @Test
    @DisplayName("Should support parameters with annotation annotated with @DtoProjectionSpec")
    void supportsParameter_shouldSupportParametersWithMetaAnnotation() throws NoSuchMethodException {

        // Given
        Method method = getClass().getDeclaredMethod("methodWithCustomAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);

        // When
        boolean result = resolver.supportsParameter(parameter);

        // Then
        assertTrue(result);

    }

    @Test
    @DisplayName("Should not support parameters without @DtoProjectionSpec annotation")
    void supportsParameter_shouldNotSupportParametersWithoutAnnotation() {

        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.hasParameterAnnotation(DtoProjectionSpec.class)).thenReturn(false);
        when(parameter.getParameterAnnotations()).thenReturn(new java.lang.annotation.Annotation[0]);

        // When
        boolean result = resolver.supportsParameter(parameter);

        // Then
        assertFalse(result);

    }

    @Test
    @DisplayName("Should resolve argument with @DtoProjectionSpec annotation")
    void resolveArgument_shouldResolveArgumentWithAnnotation() throws Exception {

        // Given
        Method method = getClass().getDeclaredMethod("methodWithDirectAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        TestDto testDto = new TestDto(1L, "Test", 25);
        NativeWebRequest webRequest = getMockRequest(testDto);

        // When
        Object result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertInstanceOf(TestDto.class, result);
        TestDto dto = (TestDto) result;
        assertEquals(ValueWrapper.empty(), dto.id);
        assertEquals(testDto.name, dto.name);
        assertEquals(testDto.age, dto.age);

    }

    @Test
    @DisplayName("Should resolve argument with annotation annotated with @DtoProjectionSpec")
    void resolveArgument_shouldResolveArgumentWithMetaAnnotation() throws Exception {

        // Given
        Method method = getClass().getDeclaredMethod("methodWithCustomAnnotation", TestDto.class);
        MethodParameter parameter = new MethodParameter(method, 0);
        TestDto testDto = new TestDto(1L, "Test", 25);
        NativeWebRequest webRequest = getMockRequest(testDto);

        // When
        Object result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertInstanceOf(TestDto.class, result);
        TestDto dto = (TestDto) result;
        assertEquals(ValueWrapper.empty(), dto.id);
        assertEquals(testDto.name, dto.name);
        assertEquals(testDto.age, dto.age);

    }

    NativeWebRequest getMockRequest(TestDto testDto) throws IOException {

        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        String requestBody = objectMapper.writeValueAsString(testDto);

        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        BufferedReader reader = new BufferedReader(new StringReader(requestBody));

        when(webRequest.getNativeRequest(HttpServletRequest.class)).thenReturn(servletRequest);
        when(servletRequest.getReader()).thenReturn(reader);

        return webRequest;

    }

    // Method used for testing
    void methodWithDirectAnnotation(@DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age")TestDto dto) {
    }

    // Method used for testing
    void methodWithCustomAnnotation(@TestProjection TestDto dto) {
    }

    //AnnotationUsedForTesting
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @DtoProjectionSpec(projectedClass = TestDto.class, value = "name, age")
    @interface TestProjection {
    }

}
