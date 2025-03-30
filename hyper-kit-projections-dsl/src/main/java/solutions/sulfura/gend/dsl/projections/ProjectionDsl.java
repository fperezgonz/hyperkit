package solutions.sulfura.gend.dsl.projections;

import solutions.sulfura.gend.dtos.Dto;
import solutions.sulfura.gend.dtos.projection.DtoProjection;
import solutions.sulfura.gend.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.DtoListFieldConf;
import solutions.sulfura.gend.dtos.projection.fields.FieldConf;
import solutions.sulfura.gend.dtos.projection.fields.ListFieldConf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

    public static class ProjectionDslParseProcess {


        //TODO parse the spec
        //Context tracking variables
        Class<DtoProjection> rootType;

        protected boolean isTokenTerminator(char c) {
            return Character.isWhitespace(c)
                    || c == ','
                    || c == '\n'
                    || c == '{'
                    || c == '}'
                    || c == '@'
                    || c == Character.MIN_VALUE;
        }

        protected FieldConf createFieldConfForProperty(String propertyName, FieldConf.Presence presence, boolean allowInsert, boolean allowDelete, DtoProjection nestedProjection) {

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
                result = FieldConf.FieldConfBuilder.of(presence)
                        .build();
            } else if (fConfType == DtoFieldConf.class) {
                if (nestedProjection == null) {
                    throw new RuntimeException("property " + propertyName + " in Type " + rootType + " must specify a projection");
                }
                result = DtoFieldConf.DtoFieldConfBuilder.newInstance()
                        .presence(presence)
                        .dtoProjection(nestedProjection)
                        .build();
            } else if (fConfType == ListFieldConf.class) {
                if (nestedProjection != null) {
                    throw new RuntimeException("Illegal declaration of projection on property " + propertyName + " in Type " + rootType);
                }
                result = ListFieldConf.ListConfBuilder.newInstance()
                        .presence(presence)
                        .allowInsert(allowInsert)
                        .allowDelete(allowDelete)
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
                        .build();
            }

            return result;

        }

        protected ParseResult<String> parsePropertyName(CharacterStream characterStream) {

            int charsRead = 0;
            StringBuilder currentToken = new StringBuilder();
            currentToken.setLength(0);

            for (Character c = characterStream.currentChar; c != null && !isTokenTerminator(c); c = characterStream.next(), charsRead++) {
                currentToken.append(c);
            }

            ParseResult<String> result = new ParseResult<>();
            result.parsedValue = currentToken.toString();
            result.charactersRead = charsRead;

            return result;
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

            while (characterStream.currentChar != null
                    && characterStream.currentChar != ','
                    && characterStream.currentChar != '\n'
                    && characterStream.currentChar != '}') {

                char c = characterStream.currentChar;
                //Perform actions for the end of the current state (if applicable) and determine new state
                {

                    //Was not parsing a token
                    if (c == '@') {

                        ParseResult<String> parseResult = parseModifier(characterStream);
                        charsRead += parseResult.charactersRead;
                        String modifier = parseResult.parsedValue;
                        if (modifier.equals(ProjectionDsl.MANDATORY)) {
                            presence = FieldConf.Presence.MANDATORY;
                        } else if (modifier.equals(ProjectionDsl.OPTIONAL)) {
                            presence = FieldConf.Presence.OPTIONAL;
                        } else if (modifier.equals(ProjectionDsl.ALLOW_INSERT)) {
                            allowInsert = true;
                        } else if (modifier.equals(ProjectionDsl.ALLOW_REMOVE)) {
                            allowDelete = true;
                        }

                    } else if (Character.isLetterOrDigit(c)) {

                        ParseResult<String> parseResult = parsePropertyName(characterStream);
                        charsRead += parseResult.charactersRead;
                        propertyName = parseResult.parsedValue;

                    } else if (c == '{') {

                        //nested projections
                        Class rootDtoType = (Class) ((ParameterizedType) rootType.getGenericSuperclass()).getActualTypeArguments()[0];

                        Type rootDtoPropertyType = ((ParameterizedType) rootDtoType.getField(propertyName).getGenericType()).getActualTypeArguments()[0];
                        Class<Dto> nestedDtoClass = null;

                        if (rootDtoPropertyType instanceof ParameterizedType pType) {
                            ParameterizedType listOperationType = (ParameterizedType) pType.getActualTypeArguments()[0];
                            nestedDtoClass = (Class) listOperationType.getActualTypeArguments()[0];
                        } else if (rootDtoPropertyType instanceof Class){
                            nestedDtoClass= (Class) rootDtoPropertyType;
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
            result.parsedValue = createFieldConfForProperty(propertyName, presence, allowInsert, allowDelete, nestedDtoProjection);
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
                P parsedValue = parseProjection(characterStream, rootType).parsedValue;
                return parsedValue;
            } catch (RuntimeException ex) {

                if (characterStream.pos < characterStream.streamData.length) {

                    System.out.println("Error parsing projection\n" +
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

        }

        protected static class PropertyParseResult extends ParseResult<FieldConf> {
            String propertyName;
        }
    }

}