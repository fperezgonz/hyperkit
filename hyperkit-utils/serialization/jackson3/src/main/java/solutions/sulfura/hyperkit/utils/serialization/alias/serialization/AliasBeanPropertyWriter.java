package solutions.sulfura.hyperkit.utils.serialization.alias.serialization;

import solutions.sulfura.hyperkit.dsl.projections.FieldAliasUtils;
import solutions.sulfura.hyperkit.dtos.ValueWrapper;
import solutions.sulfura.hyperkit.dtos.projection.DtoProjection;
import solutions.sulfura.hyperkit.dtos.projection.fields.DtoFieldConf;
import solutions.sulfura.hyperkit.dtos.projection.fields.FieldConf;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.PropertyName;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ser.BeanPropertyWriter;
import tools.jackson.databind.util.NameTransformer;

public class AliasBeanPropertyWriter extends BeanPropertyWriter {

    public static String HYPERKIT_PROJECTION_ATTR_KEY = "hyperkit-projection";
    public static FieldAliasUtils FIELD_ALIAS_UTILS_INSTANCE = new FieldAliasUtils(true);

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
    public void serializeAsProperty(Object bean, JsonGenerator gen, SerializationContext prov) throws Exception {
        DtoProjection<?> currentProjection = (DtoProjection<?>) prov.getAttribute(HYPERKIT_PROJECTION_ATTR_KEY);

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
            super.serializeAsProperty(bean, gen, prov);
            return;
        }

        FieldConf fieldConf = null;
        fieldConf = FIELD_ALIAS_UTILS_INSTANCE.findFieldConfForProperty(currentProjection, this.getName());
        if (fieldConf == null) {
            throw new RuntimeException("FieldConf not found for property: " + this.getName());
        }
        String serializedName = fieldConf.getFieldAlias() != null ? fieldConf.getFieldAlias() : this.getName();

        if (value == null) {
            if (_nullSerializer != null) {
                gen.writeName(serializedName);
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
                    prov.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, nestedProjection);
                }
            }

            gen.writeName(serializedName);
            ValueSerializer<Object> ser = getSerializer();
            if (ser == null) {
                ser = prov.findValueSerializer(value.getClass());
            }

            ser.serialize(value, gen, prov);

        } finally {
            // Restore projection
            prov.setAttribute(HYPERKIT_PROJECTION_ATTR_KEY, currentProjection);
        }
    }
}
