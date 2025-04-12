package solutions.sulfura.hyperkit.utils.spring.resolvers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class SortArgumentResolverTest {

    @Autowired
    private SortArgumentResolver resolver;

    @BeforeEach
    void setUp() {
        resolver.setTreatNullAsUnsorted(true);
    }

    @Test
    @DisplayName("Should support Sort parameters")
    void supportsParameter_shouldSupportSortParameters() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        //noinspection unchecked,rawtypes
        when(parameter.getParameterType()).thenReturn((Class) Sort.class);

        // When
        boolean result = resolver.supportsParameter(parameter);

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("Should not support non-Sort parameters")
    void supportsParameter_shouldNotSupportNonSortParameters() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        when(parameter.getParameterType()).thenReturn((Class) String.class);

        // When
        boolean result = resolver.supportsParameter(parameter);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("Should use default parameter name 'sort' when no RequestParam annotation is present")
    void resolveArgument_shouldUseDefaultParameterName() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn("name:asc");

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.toList().size());
        assertEquals("name", result.toList().getFirst().getProperty());
        assertEquals(Sort.Direction.ASC, result.toList().getFirst().getDirection());
    }

    @Test
    @DisplayName("Should use parameter name from RequestParam annotation")
    void resolveArgument_shouldUseParameterNameFromAnnotation() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        RequestParam requestParam = mock(RequestParam.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(requestParam);
        when(requestParam.name()).thenReturn("orderBy");
        when(requestParam.value()).thenReturn("");
        when(webRequest.getParameter("orderBy")).thenReturn("name:desc");

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.toList().size());
        assertEquals("name", result.toList().getFirst().getProperty());
        assertEquals(Sort.Direction.DESC, result.toList().getFirst().getDirection());
    }

    @Test
    @DisplayName("Should use value from RequestParam annotation when name is empty")
    void resolveArgument_shouldUseValueFromAnnotation() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        RequestParam requestParam = mock(RequestParam.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(requestParam);
        when(requestParam.name()).thenReturn("");
        when(requestParam.value()).thenReturn("sortBy");
        when(webRequest.getParameter("sortBy")).thenReturn("name:asc");

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.toList().size());
        assertEquals("name", result.toList().getFirst().getProperty());
        assertEquals(Sort.Direction.ASC, result.toList().getFirst().getDirection());
    }

    @Test
    @DisplayName("Should return unsorted when parameter is empty")
    void resolveArgument_shouldReturnUnsortedWhenParameterIsEmpty() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn("");

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isUnsorted());
    }

    @Test
    @DisplayName("Should return unsorted when parameter is null and treatNullAsUnsorted is true")
    void resolveArgument_shouldReturnUnsortedWhenParameterIsNullAndTreatNullAsUnsortedIsTrue() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn(null);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isUnsorted());
    }

    @Test
    @DisplayName("Should return null when parameter is null and treatNullAsUnsorted is false")
    void resolveArgument_shouldReturnNullWhenParameterIsNullAndTreatNullAsUnsortedIsFalse() {
        // Given
        resolver.setTreatNullAsUnsorted(false);
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn(null);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNull(result);
    }

    @ParameterizedTest
    @MethodSource("provideSortFormats")
    @DisplayName("Should parse different sort formats correctly")
    void resolveArgument_shouldParseDifferentSortFormats(String sortParam, String expectedProperty, Sort.Direction expectedDirection) {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn(sortParam);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.toList().size());
        assertEquals(expectedProperty, result.toList().getFirst().getProperty());
        assertEquals(expectedDirection, result.toList().getFirst().getDirection());
    }

    @Test
    @DisplayName("Should parse multiple sort fields correctly")
    void resolveArgument_shouldParseMultipleSortFields() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(null);
        when(webRequest.getParameter("sort")).thenReturn("name:asc,age:desc");

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.toList().size());
        assertEquals("name", result.toList().getFirst().getProperty());
        assertEquals(Sort.Direction.ASC, result.toList().getFirst().getDirection());
        assertEquals("age", result.toList().get(1).getProperty());
        assertEquals(Sort.Direction.DESC, result.toList().get(1).getDirection());
    }

    @Test
    @DisplayName("Should use default value from RequestParam when parameter is missing")
    void resolveArgument_shouldUseDefaultValueFromRequestParam() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        RequestParam requestParam = mock(RequestParam.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(requestParam);
        when(requestParam.name()).thenReturn("sort");
        when(requestParam.defaultValue()).thenReturn("name:asc");
        when(webRequest.getParameter("sort")).thenReturn(null);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertEquals(1, result.toList().size());
        assertEquals("name", result.toList().getFirst().getProperty());
        assertEquals(Sort.Direction.ASC, result.toList().getFirst().getDirection());
    }

    @Test
    @DisplayName("Should ignore Spring's magic default value and return unsorted when treatNullAsUnsorted is true")
    void resolveArgument_shouldIgnoreSpringMagicDefaultValueAndReturnUnsortedWhenTreatNullAsUnsortedIsTrue() {
        // Given
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        RequestParam requestParam = mock(RequestParam.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(requestParam);
        when(requestParam.name()).thenReturn("sort");
        when(requestParam.defaultValue()).thenReturn("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n");
        when(webRequest.getParameter("sort")).thenReturn(null);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNotNull(result);
        assertTrue(result.isUnsorted());
    }

    @Test
    @DisplayName("Should ignore Spring's magic default value and return null when treatNullAsUnsorted is false")
    void resolveArgument_shouldIgnoreSpringMagicDefaultValueAndReturnNullWhenTreatNullAsUnsortedIsFalse() {
        // Given
        resolver.setTreatNullAsUnsorted(false);
        MethodParameter parameter = mock(MethodParameter.class);
        NativeWebRequest webRequest = mock(NativeWebRequest.class);
        RequestParam requestParam = mock(RequestParam.class);

        when(parameter.getParameterAnnotation(RequestParam.class)).thenReturn(requestParam);
        when(requestParam.name()).thenReturn("sort");
        when(requestParam.defaultValue()).thenReturn("\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n");
        when(webRequest.getParameter("sort")).thenReturn(null);

        // When
        Sort result = resolver.resolveArgument(parameter, null, webRequest, null);

        // Then
        assertNull(result);
    }

    private static Stream<Arguments> provideSortFormats() {
        return Stream.of(
                Arguments.of("name:asc", "name", Sort.Direction.ASC),
                Arguments.of("name:desc", "name", Sort.Direction.DESC),
                Arguments.of("+name", "name", Sort.Direction.ASC),
                Arguments.of("-name", "name", Sort.Direction.DESC)
        );
    }

}
