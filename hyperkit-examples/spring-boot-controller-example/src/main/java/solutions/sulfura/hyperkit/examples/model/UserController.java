package solutions.sulfura.hyperkit.examples.model;

import io.github.perplexhub.rsql.RSQLJPASupport;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import solutions.sulfura.hyperkit.dsl.projections.DtoProjectionSpec;
import solutions.sulfura.hyperkit.dtos.UserDto;
import solutions.sulfura.hyperkit.utils.GenericCustomResponseExample;
import solutions.sulfura.hyperkit.utils.GenericCustomResponseExample.ErrorData;
import solutions.sulfura.hyperkit.utils.spring.HyperRepository;
import solutions.sulfura.hyperkit.utils.spring.SingleDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.StdDtoRequestBody;
import solutions.sulfura.hyperkit.utils.spring.hypermapper.HyperMapper;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Tag(name = "Users")
public class UserController {

    private final HyperMapper<Object> hyperMapper;
    private final HyperRepository<Object> userRepository;

    public UserController(HyperMapper<Object> hyperMapper, HyperRepository<Object> userRepository) {
        this.hyperMapper = hyperMapper;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/api/v1/users/", consumes = "application/json", produces = "application/json")
    @StdUserResponseProjection
    @Transactional
    public ResponseEntity<GenericCustomResponseExample<UserDto>> postUsers(@StdUserPostRequestProjection
                                                                    @RequestBody StdDtoRequestBody<UserDto> userData,
                                                                           UserDto.Projection returnValueProjection) {

        List<ErrorData> errors = new ArrayList<>();

        // Validate Dtos
        List<UserDto> data = userData.getData();
        for (int i = 0; i < data.size(); i++) {

            UserDto userDto = data.get(i);

            // POST data validations
            // The entity id is projected to avoid creating new entities by mistake
            if (!userDto.id.isEmpty()) {
                errors.add(ErrorData.Builder.newInstance()
                        .errorCode("ERR.USER_ID.NOT_EMPTY")
                        .message("User.Id must be empty for POST operations")
                        .build());
            }

        }

        // If there were validation errors, return them
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .errors(errors).build());
        }

        var results = hyperMapper.persistDtosToEntities(data, null);

        // Map to dtos, applying the projection defined for return values
        var result = hyperMapper.mapEntitiesToDtos(results, UserDto.class, returnValueProjection);

        // Return result
        return ResponseEntity.ok(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                .data(result)
                .build());

    }

    @PatchMapping(value = "/api/v1/users/{userId}", consumes = "application/json", produces = "application/json")
    @StdUserResponseProjection
    @Transactional
    public ResponseEntity<GenericCustomResponseExample<UserDto>> patchUser(@StdUserPatchRequestProjection
                                                                    @RequestBody SingleDtoRequestBody<UserDto> requestBody,
                                                                           @PathVariable Long userId,
                                                                           UserDto.Projection returnValueProjection) {

        List<GenericCustomResponseExample.ErrorData> errors = new ArrayList<>();

        // Validate Dto data
        if (requestBody.getData().isEmpty()) {

            errors.add(GenericCustomResponseExample.ErrorData.Builder.newInstance()
                    .errorCode("ERR.REQUEST_DATA.EMPTY")
                    .message("The request body does not contain user data")
                    .build());

            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .errors(errors).build());

        }

        if (requestBody.getData().get().id.isEmpty()) {

            errors.add(GenericCustomResponseExample.ErrorData.Builder.newInstance()
                    .errorCode("ERR.USER_ID.EMPTY")
                    .message("The user data does not contain an id")
                    .build());

            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .errors(errors)
                    .build());

        }

        if (!Objects.equals(requestBody.getData().get().id.get(), userId)) {
            errors.add(GenericCustomResponseExample.ErrorData.Builder.newInstance()
                    .errorCode("ERR.USER_ID.NOT_MATCH")
                    .message("The userId in the endpoint path does not match the userId in the body")
                    .build());
        }

        // If there were validation errors, return them
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .errors(errors).build());
        }

        var results = hyperMapper.persistDtoToEntity(requestBody.getData().get(), null);

        // Map to dtos, applying the projection defined for return values
        var result = hyperMapper.mapEntityToDto(results, UserDto.class, returnValueProjection);

        return ResponseEntity.ok(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                .data(List.of(result))
                .build());

    }

    @GetMapping(value = "/api/v1/users/", produces = "application/json")
    @StdUserResponseProjection
    @Transactional
    public ResponseEntity<GenericCustomResponseExample<UserDto>> getUsers(@RequestParam(name = "filter", required = false) String rsqlFilter,
                                                                          @RequestParam(name = "page.size", required = false, defaultValue = "25") Integer pageSize,
                                                                          @RequestParam(name = "page.number", required = false, defaultValue = "0") Integer pageNumber,
                                                                          UserDto.Projection returnValueProjection) {

        final int MAX_PAGE_SIZE = 3000;

        if (pageSize > MAX_PAGE_SIZE) {
            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .data(new ArrayList<>())
                    .errors(
                            ErrorData.Builder.newInstance()
                                    .errorCode("ERR_MAX_PAGE_SIZE.LIMIT_EXCEEDED")
                                    .message("The page size value cannot be higher than" + MAX_PAGE_SIZE)
                                    .build()
                    )
                    .build()
            );
        }

        List<User> users;

        // Apply RSQL filter
        if (rsqlFilter != null) {
            Specification<User> jpaSpecification = RSQLJPASupport.toSpecification(rsqlFilter, true);
            users = userRepository.findAll(User.class, jpaSpecification, PageRequest.of(pageNumber, pageSize), null).toList();
        } else {
            users = userRepository.findAll(User.class, PageRequest.of(pageNumber, pageSize), null).toList();
        }

        // Map to dtos applying the projection defined for return values
        var result = users.stream()
                .map(entity -> hyperMapper.mapEntityToDto(entity, UserDto.class, returnValueProjection))
                .toList();

        return ResponseEntity.ok(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                .data(result)
                .build());

    }

    @GetMapping(value = "/api/v1/users/{userId}", produces = "application/json")
    @StdUserResponseProjection
    @Transactional
    public ResponseEntity<GenericCustomResponseExample<UserDto>> getUser(@PathVariable Long userId,
                                                                         UserDto.Projection returnValueProjection) {

        if (userId == null) {
            return ResponseEntity.badRequest().body(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                    .data(new ArrayList<>())
                    .errors(ErrorData.Builder.newInstance()
                            .errorCode("ERR.USER_ID.MISSING")
                            .message("The userId parameter is required for this endpoint")
                            .build())
                    .build());
        }

        Optional<User> user = userRepository.findById(User.class, userId, null);

        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Map to dto, applying the projection defined for return values
        var result = hyperMapper.mapEntityToDto(user.get(), UserDto.class, returnValueProjection);

        return ResponseEntity.ok(GenericCustomResponseExample.Builder.<UserDto>newInstance()
                .data(List.of(result))
                .build());

    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.METHOD})
    @DtoProjectionSpec(
            projectedClass = UserDto.class,
            value = """
                    id
                    name
                    email
                    roles {
                        id
                        name
                        permissions {
                            id
                            name
                        }
                    }
                    """
    )
    public @interface StdUserResponseProjection {
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.METHOD})
    @DtoProjectionSpec(
            projectedClass = UserDto.class,
            value = """
                    id
                    name
                    email
                    """
    )
    public @interface StdUserPostRequestProjection {
    }

    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.PARAMETER, ElementType.TYPE_USE, ElementType.METHOD})
    @DtoProjectionSpec(
            projectedClass = UserDto.class,
            value = """
                    id
                    name
                    email
                    """
    )
    public @interface StdUserPatchRequestProjection {
    }

}
