package com.ohmgeek.jukebox.common.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

import java.io.*;

public class GenericCodec<T> implements MessageCodec<T, T> {
    private final Class<T> clazz;

    public GenericCodec(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void encodeToWire(Buffer buffer, T t) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ObjectOutput objectOutput = new ObjectOutputStream(outputStream)) {
            // Write the single object, then flush.
            objectOutput.writeObject(t);
            objectOutput.flush();

            byte[] bytes = outputStream.toByteArray();

            // Now we output the data. Length first, bytes second
            buffer.appendInt(bytes.length);
            buffer.appendBytes(bytes);

            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't encode to wire for class " + clazz.getSimpleName());
        }
    }

    @Override
    public T decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        byte[] bytes = buffer.getBytes(length+=4, length += length);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try(ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            @SuppressWarnings("unchecked")
            T msg = (T) objectInputStream.readObject();
            return msg;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public T transform(T t) {
        return t;
    }

    @Override
    public String name() {
        return clazz.getSimpleName() + "codec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
