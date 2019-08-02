package cn.pers.qhl.sendfile;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class SendFileDurationSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String duration, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
//        jsonGenerator.writeString(Util.parseDuration(duration));
    }
}
