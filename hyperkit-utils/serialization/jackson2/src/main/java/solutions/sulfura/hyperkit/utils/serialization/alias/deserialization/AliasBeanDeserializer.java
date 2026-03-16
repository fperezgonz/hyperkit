package solutions.sulfura.hyperkit.utils.serialization.alias.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import solutions.sulfura.hyperkit.dsl.projections.FieldAliasUtils;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter;

import java.io.IOException;

import static solutions.sulfura.hyperkit.utils.serialization.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

public class AliasBeanDeserializer extends BeanDeserializer {

    public AliasBeanDeserializer(BeanDeserializer base) {
        super(base);
    }

    @Override
    public Object deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException {
        DtoProjection<?> projection = (DtoProjection<?>) deserializationContext.getAttribute(HYPERKIT_PROJECTION_ATTR_KEY);
        if (projection == null) {
            return super.deserialize(parser, deserializationContext);
        }

        if (!parser.isExpectedStartObjectToken()) {
            return super.deserialize(parser, deserializationContext);
        }

        Object bean = _valueInstantiator.createUsingDefault(deserializationContext);
        parser.assignCurrentValue(bean);

        for (String propName = parser.nextFieldName(); propName != null; propName = parser.nextFieldName()) {
            parser.nextToken();

            ResolvedProperty resolved = resolveProperty(propName, projection);
            if (resolved == null) {
                handleUnknownVanilla(parser, deserializationContext, bean, propName);
            } else {
                try {
                    // Push the nested projection to the deserialization context
                    if (resolved.nestedProjection != null) {
                        deserializationContext.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, resolved.nestedProjection);
                    }
                    resolved.property.deserializeAndSet(parser, deserializationContext, bean);
                } catch (Exception e) {
                    wrapAndThrow(e, bean, propName, deserializationContext);
                } finally {
                    // Restore the initial projection to the deserialization context
                    deserializationContext.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, projection);
                }
            }
        }

        return bean;
    }

    /**
     * Resolves a projected dto property name, checking aliases first, which take priority over real property names to handle name conflicts.
     * Also resolves the nested projection if the field is a DTO/list field
     */
    @Nullable
    private ResolvedProperty resolveProperty(String propertyName, DtoProjection<?> projection) {
        // When an alias shadows a real property name, the alias takes priority
        FieldAliasUtils.FieldConfData fieldConfDataForAlias = AliasBeanPropertyWriter.FIELD_ALIAS_UTILS_INSTANCE.findFieldConfForPropertyByFieldAlias(projection, propertyName);

        if (fieldConfDataForAlias == null || fieldConfDataForAlias.fieldConf() == null) {
            return new ResolvedProperty(_beanProperties.find(propertyName), null);

        }

        String resolvedPropertyName = fieldConfDataForAlias.fieldName();
        SettableBeanProperty prop = _beanProperties.find(resolvedPropertyName);

        if (prop != null) {
            return new ResolvedProperty(prop, getNestedProjection(fieldConfDataForAlias.fieldConf()));
        }

        return null;
    }

    private static DtoProjection<?> getNestedProjection(FieldConf fieldConf) {
        if (fieldConf instanceof DtoFieldConf<?> dtoFieldConf) {
            return dtoFieldConf.dtoProjection;
        }
        return null;
    }

    private record ResolvedProperty(@NonNull SettableBeanProperty property,
                                    @Nullable DtoProjection<?> nestedProjection) {
    }

}
