package io.github.uginx.core.utils.serialize.impl;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.github.uginx.core.constants.CodecEnum;
import io.github.uginx.core.exception.SerializeException;
import io.github.uginx.core.protocol.message.Message;
import io.github.uginx.core.utils.serialize.Serializer;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author 翁丞健
 * @Date 2022/4/26 22:03
 * @Version 1.0.0
 */

public class KryoSerializer implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        kryo.register(Message.class);
        return kryo;
    });

    @Override
    public byte[] serialize(Object data) throws SerializeException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Output output = new Output(outputStream);

        Kryo kryo = kryoThreadLocal.get();
        kryo.writeObject(output, data);

        kryoThreadLocal.remove();

        return output.toBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) throws SerializeException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);

        Input input = new Input(inputStream);

        Kryo kryo = kryoThreadLocal.get();

        T retVal = kryo.readObject(input, clazz);

        kryoThreadLocal.remove();

        return clazz.cast(retVal);
    }

    @Override
    public CodecEnum getSerializerType() {
        return CodecEnum.KYRO;
    }
}
