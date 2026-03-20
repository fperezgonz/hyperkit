package solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.serialization.AliasBeanPropertyWriter;

import java.io.IOException;
import java.util.Objects;

import static solutions.sulfura.hyperkit.utils.serialization.jackson2.alias.serialization.AliasBeanPropertyWriter.HYPERKIT_PROJECTION_ATTR_KEY;

public class AliasSettableBeanProperty extends SettableBeanProperty.Delegating {

    public AliasSettableBeanProperty(SettableBeanProperty delegate) {
        super(delegate);
    }

    @Override
    protected SettableBeanProperty withDelegate(SettableBeanProperty d) {
        return new AliasSettableBeanProperty(d);
    }

    @Override
    public void deserializeAndSet(JsonParser parser, DeserializationContext deserializationContext, Object instance) throws IOException {
        DtoProjection<?> currentProjection = (DtoProjection<?>) deserializationContext.getAttribute(HYPERKIT_PROJECTION_ATTR_KEY);
        if (currentProjection == null) {
            super.deserializeAndSet(parser, deserializationContext, instance);
            return;
        }

        FieldConf fieldConf = null;
        try {
            var fieldConfData = AliasBeanPropertyWriter.FIELD_ALIAS_UTILS_INSTANCE.findFieldConfForPropertyByFieldAlias(currentProjection, this.getName());
            if (fieldConfData != null) {
                fieldConf = fieldConfData.fieldConf();
            }
        } catch (Exception ignored) {
        }

        if (fieldConf == null || fieldConf.getFieldAlias() == null) {
            super.deserializeAndSet(parser, deserializationContext, instance);
            return;
        }

        if (!Objects.equals(fieldConf.getFieldAlias(), this.getName())) {
            this.withSimpleName(fieldConf.getFieldAlias()).deserializeAndSet(parser, deserializationContext, instance);
            return;
        }

        DtoProjection<?> nestedProjection = null;

        if (fieldConf instanceof DtoFieldConf<?> dtoFieldConf) {
            nestedProjection = dtoFieldConf.dtoProjection;
        }

        try {
            if (nestedProjection != null) {
                deserializationContext.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, nestedProjection);
            }

            super.deserializeAndSet(parser, deserializationContext, instance);
        } finally {
            deserializationContext.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, currentProjection);
        }
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        DtoProjection<?> currentProjection = (DtoProjection<?>) ctxt.getAttribute(HYPERKIT_PROJECTION_ATTR_KEY);
        FieldConf fieldConf = null;
        if (currentProjection != null) {
            try {
                var fieldConfData = AliasBeanPropertyWriter.FIELD_ALIAS_UTILS_INSTANCE.findFieldConfForPropertyByFieldAlias(currentProjection, this.getName());
                if (fieldConfData != null) {
                    fieldConf = fieldConfData.fieldConf();
                }
            } catch (Exception ignored) {
            }
        }

        DtoProjection<?> nestedProjection = null;
        if (fieldConf instanceof DtoFieldConf<?> dtoFieldConf) {
            nestedProjection = dtoFieldConf.dtoProjection;
        }

        try {
            if (nestedProjection != null) {
                ctxt.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, nestedProjection);
            }
            return super.deserializeSetAndReturn(p, ctxt, instance);
        } finally {
            ctxt.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, currentProjection);
        }
    }
}
