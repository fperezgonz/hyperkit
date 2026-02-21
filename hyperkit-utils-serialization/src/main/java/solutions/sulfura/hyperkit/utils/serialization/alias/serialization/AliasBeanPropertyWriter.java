package solutions.sulfura.hyperkit.utils.serialization.alias.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.util.NameTransformer;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import solutions.sulfura.hyperkit.utils.serialization.alias.FieldAliasUtils;

public class AliasBeanPropertyWriter extends BeanPropertyWriter {

    public AliasBeanPropertyWriter(BeanPropertyWriter base) {
        super(base);
    }

    public AliasBeanPropertyWriter(BeanPropertyWriter base, NameTransformer unwrapper) {
        super(base);
    }

    public AliasBeanPropertyWriter(BeanPropertyWriter base, PropertyName name) {
        super(base, name);
    }

    @Override
    protected AliasBeanPropertyWriter _new(PropertyName newName) {
        return new AliasBeanPropertyWriter(this, newName);
    }

    @Override
    public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper) {
        return new AliasBeanPropertyWriter(this, unwrapper);
    }

    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        DtoProjection<?> currentProjection = (DtoProjection<?>) prov.getAttribute("hyperkit-projection");

        Object value = get(bean);

        if (value == null) {
            return;
        }

        if (value instanceof ValueWrapper<?> valueWrapper) {

            if (valueWrapper.isEmpty()) {
                return;
            }

            value = valueWrapper.getOrNull();

        }

        if (currentProjection == null) {
            super.serializeAsField(bean, gen, prov);
            return;
        }

        FieldConf fieldConf = null;
        fieldConf = FieldAliasUtils.findFieldConfForProperty(currentProjection, this.getName());
        if (fieldConf == null) {
            throw new RuntimeException("FieldConf not found for property: " + this.getName());
        }
        String serializedName = fieldConf.getFieldAlias() != null ? fieldConf.getFieldAlias() : this.getName();

        if (value == null) {
            if (_nullSerializer != null) {
                gen.writeFieldName(serializedName);
                _nullSerializer.serialize(null, gen, prov);
            }
            return;
        }

        try {
            // Propagate nested projections before serializing a nested dto
            if (fieldConf instanceof DtoFieldConf<?> dtoFieldConf) {
                DtoProjection<?> nestedProjection = null;
                nestedProjection = dtoFieldConf.dtoProjection;

                if (nestedProjection != null) {
                    prov.setAttribute("hyperkit-projection", nestedProjection);
                }
            }

            gen.writeFieldName(serializedName);
            JsonSerializer<Object> ser = getSerializer();
            if (ser == null) {
                ser = prov.findValueSerializer(value.getClass(), this);
            }

            ser.serialize(value, gen, prov);

        } finally {
            // Restore projection
            prov.setAttribute("hyperkit-projection", currentProjection);
        }
    }
}
