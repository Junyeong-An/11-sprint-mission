package com.sprint.mission.discodeit.service.file;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileStore<T extends Serializable> {
    private final Path filePath;

    protected AbstractFileStore(String fileName) {
        this.filePath = Paths.get("data", fileName);
    }

    @SuppressWarnings("unchecked")
    protected List<T> readAll() {
        if (Files.notExists(filePath)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filePath))) {
            Object obj = in.readObject();
            return (List<T>) obj;
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("파일 읽기 실패: " + filePath, e);
        }
    }

    protected void writeAll(List<T> data) {
        try {
            Files.createDirectories(filePath.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new IllegalStateException("파일 쓰기 실패: " + filePath, e);
        }
    }
}
