package solutions.sulfura.hyperkit.utils.serialization.alias;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;

import java.io.IOException;
import java.util.Objects;

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
        DtoProjection<?> currentProjection = (DtoProjection<?>) deserializationContext.getAttribute("hyperkit-projection");
        if (currentProjection == null) {
            super.deserializeAndSet(parser, deserializationContext, instance);
            return;
        }

        FieldConf fieldConf = null;
        try {
            var fieldConfData = FieldAliasUtils.findFieldConfForPropertyByFieldAlias(currentProjection, this.getName());
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
                deserializationContext.setAttribute("hyperkit-projection", nestedProjection);
            }

            super.deserializeAndSet(parser, deserializationContext, instance);
        } finally {
            deserializationContext.setAttribute("hyperkit-projection", currentProjection);
        }
    }

    @Override
    public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException {
        DtoProjection<?> currentProjection = (DtoProjection<?>) ctxt.getAttribute("hyperkit-projection");
        FieldConf fieldConf = null;
        if (currentProjection != null) {
            try {
                var fieldConfData = FieldAliasUtils.findFieldConfForPropertyByFieldAlias(currentProjection, this.getName());
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
                ctxt.setAttribute("hyperkit-projection", nestedProjection);
            }
            return super.deserializeSetAndReturn(p, ctxt, instance);
        } finally {
            ctxt.setAttribute("hyperkit-projection", currentProjection);
        }
    }
}
