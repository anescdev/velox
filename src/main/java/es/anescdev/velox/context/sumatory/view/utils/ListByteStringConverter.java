package es.anescdev.velox.context.sumatory.view.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import javafx.util.StringConverter;

/**
 * @author AnesCDev
 */
public class ListByteStringConverter extends StringConverter<List<Byte>> {
    private final String separator;

    public ListByteStringConverter() {
        this(" ,");
    }

    public ListByteStringConverter(String separator) {
        this.separator = separator;
    }

    @Override
    public String toString(List<Byte> object) {
        StringJoiner joiner = new StringJoiner(separator);
        object.forEach(day -> joiner.add(day.toString()));
        return joiner.toString();
    }

    @Override
    public List<Byte> fromString(String string) {
        List<Byte> byteList = new ArrayList<>();
        for (String byteString : string.split(separator)) {
            byteList.add(Byte.parseByte(byteString));
        }
        return byteList;
    }

}
