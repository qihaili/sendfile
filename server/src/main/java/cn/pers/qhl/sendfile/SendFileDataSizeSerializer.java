package cn.pers.qhl.sendfile;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SendFileDataSizeSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if ("-1".equals(s)) {
            jsonGenerator.writeString("任意大小");
        } else {
            jsonGenerator.writeString(s);
        }
    }
}
