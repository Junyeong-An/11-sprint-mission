package com.sprint.mission.discodeit.repository.file;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileRepository<T extends Serializable> {
    private final Path filePath;

    protected AbstractFileRepository(String fileName) {
        this.filePath = Paths.get("data", fileName);
    }

    @SuppressWarnings("unchecked")
    protected List<T> readAll() {
        if (Files.notExists(filePath)) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filePath))) {
            return (List<T>) in.readObject();
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Failed to read file: " + filePath, e);
        }
    }

    protected void writeAll(List<T> data) {
        try {
            Files.createDirectories(filePath.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write file: " + filePath, e);
        }
    }
}
