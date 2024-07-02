package it.buzz.tahc.core.configuration;

import ch.jalu.configme.properties.MapProperty;
import ch.jalu.configme.properties.types.PropertyType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class BeanMapProperty<K> extends MapProperty<K> {

    //BeanPropertyType.of(VaultGroupPermission.class, DefaultMapper.getInstance()

    public BeanMapProperty(@NotNull String path, @NotNull PropertyType<K> type, @NotNull Map<String, K>  defaultValue) {
        super(path, defaultValue, type);
    }

}