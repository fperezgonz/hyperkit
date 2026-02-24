package solutions.sulfura.hyperkit.dsl.projections;

import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dtos.Dto;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.ProjectionUtils;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.ListFieldConf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public class ProjectionDsl {

    final static String OPTIONAL = "@Optional";
    final static String MANDATORY = "@Mandatory";
    final static String READONLY = "@Readonly";
    final static String ALLOW_INSERT = "@Insert";
    final static String ALLOW_REMOVE = "@Remove";
    //final static String ALLOW_UPDATE = "@Update";

    public static <P extends DtoProjection> P parse(String projectionDef, Class<P> rootType) {
        return new ProjectionDslParseProcess().parseProjection(projectionDef, rootType);
    }

    public static <P extends DtoProjection> P parse(DtoProjectionSpec annotation, Class<P> rootType) {
        return ProjectionDsl.parse(annotation.value(), rootType);
    }

    public static DtoProjection parse(DtoProjectionSpec annotation) {
        Class<? extends DtoProjection> projectionClass = ProjectionUtils.findDefaultProjectionClass(annotation.projectedClass());
        return ProjectionDsl.parse(annotation, projectionClass);
    }

    @SuppressWarnings("unchecked")
    public static class ProjectionDslParseProcess {


        //TODO parse the spec
        //Context tracking variables
        Class<DtoProjection> rootType;

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        protected boolean isTokenTerminator(char c) {
            return Character.isWhitespace(c)
                    || c == ','
                    || c == '\n'
                    || c == '{'
                    || c == '}'
                    || c == '@'
                    || c == ':'
                    || c == Character.MIN_VALUE;
        }

        protected ParseResult<String> skipNonTerminatingWhiteSpace(CharacterStream characterStream) {

            ParseResult<String> result = new ParseResult<>();
            result.charactersRead = 0;
            if (characterStream.currentChar == null) {
                return result;
            }

            while (characterStream.currentChar != '\n' && Character.isWhitespace(characterStream.currentChar) && characterStream.next() != null) {
                result.charactersRead++;
            }

            return result;

        }

        protected FieldConf createFieldConfForProperty(String propertyName,
                                                       FieldConf.Presence presence,
                                                       boolean allowInsert,
                                                       boolean allowDelete,
                                                       @Nullable String fieldAlias,
                                                       @Nullable String projectionTypeAlias,
                                                       DtoProjection nestedProjection) {

            FieldConf result = null;
            Field f = null;

            try {
                f = rootType.getField(propertyName);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

            Class<? extends FieldConf> fConfType = (Class<? extends FieldConf>) f.getType();
            if (fConfType == FieldConf.class) {
                if (nestedProjection != null) {
                    throw new RuntimeException("Illegal declaration of projection on property " + propertyName + " in Type " + rootType);
                }
                result = FieldConf.FieldConfBuilder.of(presence, fieldAlias)
                        .build();
            } else if (fConfType == DtoFieldConf.class) {
                if (nestedProjection == null) {
                    throw new RuntimeException("property " + propertyName + " in Type " + rootType + " must specify a projection");
                }
                result = DtoFieldConf.DtoFieldConfBuilder.newInstance()
                        .presence(presence)
                        .dtoProjection(nestedProjection)
                        .fieldAlias(fieldAlias)
                        .projectionTypeAlias(projectionTypeAlias)
                        .build();
            } else if (fConfType == ListFieldConf.class) {
                if (nestedProjection != null) {
                    throw new RuntimeException("Illegal declaration of projection on property " + propertyName + " in Type " + rootType);
                }
                result = ListFieldConf.ListConfBuilder.newInstance()
                        .presence(presence)
                        .allowInsert(allowInsert)
                        .allowDelete(allowDelete)
                        .alias(fieldAlias)
                        .build();
            } else if (fConfType == DtoListFieldConf.class) {
                if (nestedProjection == null) {
                    throw new RuntimeException("property " + propertyName + " in Type " + rootType + " must specify a projection");
                }
                result = DtoListFieldConf.DtoListConfBuilder.newInstance()
                        .presence(presence)
                        .allowInsert(allowInsert)
                        .allowDelete(allowDelete)
                        .dtoProjection(nestedProjection)
                        .alias(fieldAlias)
                        .projectionTypeAlias(projectionTypeAlias)
                        .build();
            }

            return result;

        }

        protected ParseResult<String> parsePropertyName(CharacterStream characterStream) {

            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            long charsRead = skipNonTerminatingWhiteSpace(characterStream).charactersRead;

            for (Character c = characterStream.currentChar; c != null && !isTokenTerminator(c); c = characterStream.next(), charsRead++) {
                if (c != '\\') {
                    currentToken.append(c);
                    continue;
                }

                ParseResult<String> escapeCharResult = handleEscapeCharacter(characterStream);
                currentToken.append(escapeCharResult.parsedValue);
                charsRead += escapeCharResult.charactersRead;
            }

            return ParseResult.valueOf(charsRead, currentToken.toString());

        }

        protected ParseResult<String> parseAsKeyword(CharacterStream characterStream) {
            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            long charsRead = skipNonTerminatingWhiteSpace(characterStream).charactersRead;

            if (characterStream.currentChar == null) {
                return ParseResult.valueOf(charsRead, null);
            }

            if ((characterStream.currentChar != 'a' && characterStream.currentChar != 'A')
                    || (characterStream.nextChar != 's' && characterStream.nextChar != 'S')) {
                return ParseResult.valueOf(charsRead, null);
            }

            characterStream.next();

            if (characterStream.nextChar == null || !Character.isWhitespace(characterStream.nextChar)) {
                characterStream.previous();
                return ParseResult.valueOf(charsRead, null);
            }

            characterStream.next();
            charsRead += 2;

            return ParseResult.valueOf(charsRead, currentToken.toString());

        }

        protected ParseResult<String> handleEscapeCharacter(CharacterStream characterStream) {
            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            if (characterStream.currentChar != '\\') {
                throw new RuntimeException("Expected opening '\\'");
            }

            if (characterStream.nextChar == '`') {
                characterStream.next();
                currentToken.append('`');
            } else if (characterStream.nextChar == '\\') {
                characterStream.next();
                currentToken.append('\\');
            } else {
                throw new RuntimeException("Escape character \\ reserved to escape '`' and '\\' in literals");
            }

            return ParseResult.valueOf(1, currentToken.toString());
        }

        protected ParseResult<String> parseLiteral(CharacterStream characterStream) {

            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            if (characterStream.currentChar != '`') {
                throw new RuntimeException("Expected opening '`' literal");
            }

            characterStream.next();
            long charsRead = 1;

            for (Character c = characterStream.currentChar; c != null && c != '`'; c = characterStream.next(), charsRead++) {
                if (c != '\\') {
                    currentToken.append(c);
                    continue;
                }

                ParseResult<String> escapeCharResult = handleEscapeCharacter(characterStream);
                currentToken.append(escapeCharResult.parsedValue);
                charsRead += escapeCharResult.charactersRead;

            }

            if (characterStream.currentChar == null) {
                throw new RuntimeException("Expected closing '`' literal");
            }

            characterStream.next();
            charsRead++;

            return ParseResult.valueOf(charsRead, currentToken.toString());

        }

        protected ParseResult<String> parseFieldAlias(CharacterStream characterStream) {

            long charsRead = skipNonTerminatingWhiteSpace(characterStream).charactersRead;

            if (characterStream.currentChar == null) {
                return ParseResult.valueOf(charsRead, null);
            }

            if (characterStream.currentChar == '`') {
                ParseResult<String> literalResult = parseLiteral(characterStream);
                charsRead += literalResult.charactersRead;
                return ParseResult.valueOf(charsRead, literalResult.parsedValue);
            }

            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            for (Character c = characterStream.currentChar; c != null && !isTokenTerminator(c); c = characterStream.next(), charsRead++) {

                if (c == '`') {
                    throw new RuntimeException("Projection type alias cannot contain '`'");
                }

                if (c != '\\') {
                    currentToken.append(c);
                    continue;
                }

                ParseResult<String> escapeCharResult = handleEscapeCharacter(characterStream);
                currentToken.append(escapeCharResult.parsedValue);
                charsRead += escapeCharResult.charactersRead;
            }

            ParseResult<String> result = new ParseResult<>();
            result.parsedValue = currentToken.isEmpty() ? null : currentToken.toString();

            return result;

        }

        protected ParseResult<String> parseProjectionTypeAlias(CharacterStream characterStream) {

            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            long charsRead = skipNonTerminatingWhiteSpace(characterStream).charactersRead;

            if (characterStream.currentChar == null) {
                return ParseResult.valueOf(charsRead, null);
            }

            if (characterStream.currentChar != ':') {
                return ParseResult.valueOf(charsRead, null);
            }

            characterStream.next();
            charsRead++;

            charsRead += skipNonTerminatingWhiteSpace(characterStream).charactersRead;

            if (characterStream.currentChar == null) {
                return ParseResult.valueOf(charsRead, null);
            }

            if (characterStream.currentChar == '`') {
                ParseResult<String> literalResult = parseLiteral(characterStream);
                charsRead += literalResult.charactersRead;
                return ParseResult.valueOf(charsRead, literalResult.parsedValue);
            }

            for (Character c = characterStream.currentChar; c != null && !isTokenTerminator(c); c = characterStream.next(), charsRead++) {

                if (c == '`') {
                    throw new RuntimeException("Projection type alias cannot contain '`'");
                }

                if (c != '\\') {
                    currentToken.append(c);
                    continue;
                }

                ParseResult<String> escapeCharResult = handleEscapeCharacter(characterStream);
                currentToken.append(escapeCharResult.parsedValue);
                charsRead += escapeCharResult.charactersRead;
            }

            return ParseResult.valueOf(charsRead, currentToken.toString());

        }

        protected ParseResult<String> parseModifier(CharacterStream characterStream) {
            int charsRead = 0;
            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);
            do {
                currentToken.append(characterStream.currentChar);
                charsRead++;
            } while (characterStream.next() != null && !isTokenTerminator(characterStream.currentChar));

            ParseResult<String> result = new ParseResult<>();
            result.parsedValue = currentToken.toString();
            result.charactersRead = charsRead;

            return result;
        }

        protected PropertyParseResult parseProperty(CharacterStream characterStream) throws NoSuchFieldException {

            long charsRead = 0;
            boolean allowInsert = false;
            boolean allowDelete = false;
            String propertyName = null;
            FieldConf.Presence presence = FieldConf.Presence.OPTIONAL;
            DtoProjection<?> nestedDtoProjection = null;
            String fieldAlias = null;
            String projectionTypeAlias = null;

            ParseResult<String> propertyNameResult = parsePropertyName(characterStream);
            charsRead += propertyNameResult.charactersRead;
            propertyName = propertyNameResult.parsedValue;

            ParseResult<String> asResult = parseAsKeyword(characterStream);
            charsRead += asResult.charactersRead;

            ParseResult<String> fieldAliasResult = parseFieldAlias(characterStream);
            charsRead += fieldAliasResult.charactersRead;
            fieldAlias = fieldAliasResult.parsedValue;

            ParseResult<String> projectionTypeAliasResult = parseProjectionTypeAlias(characterStream);
            charsRead += projectionTypeAliasResult.charactersRead;
            projectionTypeAlias = projectionTypeAliasResult.parsedValue;

            ParseResult<String> wsResult = skipNonTerminatingWhiteSpace(characterStream);
            charsRead += wsResult.charactersRead;
            if (characterStream.currentChar != null
                    && characterStream.currentChar != ','
                    && characterStream.currentChar != '\n'
                    && characterStream.currentChar != '}'
                    && characterStream.currentChar != '{'
                    && characterStream.currentChar != '@') {
                throw new RuntimeException("Illegal character '" + characterStream.currentChar + "' at pos " + characterStream.getPos());
            }

            while (characterStream.currentChar != null
                    && characterStream.currentChar != ','
                    && characterStream.currentChar != '\n'
                    && characterStream.currentChar != '}') {

                char c = characterStream.currentChar;
                // Perform actions for the end of the current state (if applicable) and determine the new state
                {

                    //Was not parsing a token
                    if (c == '@') {

                        ParseResult<String> parseResult = parseModifier(characterStream);
                        charsRead += parseResult.charactersRead;
                        String modifier = parseResult.parsedValue;
                        switch (modifier) {
                            case ProjectionDsl.MANDATORY -> presence = FieldConf.Presence.MANDATORY;
                            case ProjectionDsl.OPTIONAL -> presence = FieldConf.Presence.OPTIONAL;
                            case ProjectionDsl.ALLOW_INSERT -> allowInsert = true;
                            case ProjectionDsl.ALLOW_REMOVE -> allowDelete = true;
                        }

                    } else if (c == '{') {

                        //nested projections
                        Class rootDtoType = (Class) ((ParameterizedType) rootType.getGenericSuperclass()).getActualTypeArguments()[0];

                        Type rootDtoPropertyType = ((ParameterizedType) rootDtoType.getField(propertyName).getGenericType()).getActualTypeArguments()[0];
                        Class<Dto> nestedDtoClass = null;

                        if (rootDtoPropertyType instanceof ParameterizedType pType) {
                            ParameterizedType listOperationType = (ParameterizedType) pType.getActualTypeArguments()[0];
                            nestedDtoClass = (Class) listOperationType.getActualTypeArguments()[0];
                        } else if (rootDtoPropertyType instanceof Class) {
                            nestedDtoClass = (Class) rootDtoPropertyType;
                        }

                        Class<DtoProjection> propertyProjectionClass = null;

                        for (Class<?> clazz : nestedDtoClass.getDeclaredClasses()) {
                            if (DtoProjection.class.isAssignableFrom(clazz)) {
                                propertyProjectionClass = (Class<DtoProjection>) clazz;
                            }
                        }

                        ParseResult<DtoProjection> parseResult = new ProjectionDslParseProcess().parseProjection(characterStream, propertyProjectionClass);
                        nestedDtoProjection = parseResult.parsedValue;
                        charsRead += parseResult.charactersRead;

                    } else {

                        charsRead++;
                        characterStream.next();

                    }

                }
            }

            PropertyParseResult result = new PropertyParseResult();
            result.propertyName = propertyName;
            result.parsedValue = createFieldConfForProperty(propertyName,
                    presence,
                    allowInsert,
                    allowDelete,
                    fieldAlias,
                    projectionTypeAlias,
                    nestedDtoProjection);
            result.charactersRead = charsRead;

            return result;

        }

        protected <P extends DtoProjection> ParseResult<P> parseProjection(CharacterStream characterStream, Class<P> rootType) {

            long charsRead = 0;

            try {

                this.rootType = (Class<DtoProjection>) rootType;

                P result = rootType.getDeclaredConstructor().newInstance();

                while (characterStream.currentChar != null) {

                    char c = characterStream.currentChar;

                    if (Character.isLetterOrDigit(c) || c == '@') {
                        PropertyParseResult parseResult = parseProperty(characterStream);
                        charsRead += parseResult.charactersRead;
                        FieldConf fieldConf = parseResult.parsedValue;
                        rootType.getField(parseResult.propertyName).set(result, fieldConf);
                    } else if (Character.isWhitespace(c)
                            || c == ','
                            || c == '{') {
                        charsRead++;
                        characterStream.next();
                        //Skip
                        //noinspection UnnecessaryContinue
                        continue;
                    } else if (c == '}') {
                        charsRead++;
                        characterStream.next();
                        break;
                    } else {
                        throw new RuntimeException("Illegal character '" + c + "' at pos " + characterStream.getPos());
                    }

                }

                ParseResult<P> parseResult = new ParseResult<>();
                parseResult.parsedValue = result;
                parseResult.charactersRead = charsRead;

                return parseResult;

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }

        }

        public <P extends DtoProjection> P parseProjection(String projectionDef, Class<P> rootType) {

            CharacterStream characterStream = new CharacterStream(projectionDef.toCharArray());

            try {

                return parseProjection(characterStream, rootType).parsedValue;

            } catch (RuntimeException ex) {

                if (characterStream.pos < characterStream.streamData.length) {

                    System.err.println("Error parsing projection\n" +
                            "  Message: " + ex.getMessage() + "\n" +
                            "  Pos " + characterStream.pos + "\n" +
                            "  Rest of the Stream: " + String.valueOf(characterStream.streamData).substring(characterStream.pos));

                }

                throw ex;

            }
        }

        protected static class ParseResult<T> {

            T parsedValue = null;
            long charactersRead = 0;

            static <T> ParseResult<T> valueOf(long charactersRead, T parsedValue) {
                ParseResult<T> result = new ParseResult<>();
                result.charactersRead = charactersRead;
                result.parsedValue = parsedValue;
                return result;
            }

        }

        protected static class PropertyParseResult extends ParseResult<FieldConf> {
            String propertyName;
        }
    }

}