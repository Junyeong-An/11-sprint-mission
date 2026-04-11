package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractFileRepository<T> {
    private final Path filePath;

    protected AbstractFileRepository(String fileDirectory, String fileName) {
        this.filePath = Paths.get(fileDirectory, fileName);
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
            throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, "파일을 읽는 중 오류가 발생했어요.");
        }
    }

    protected void writeAll(List<T> data) {
        try {
            Files.createDirectories(filePath.getParent());
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filePath))) {
                out.writeObject(data);
            }
        } catch (IOException e) {
            throw new DiscodeitException(ErrorCode.INTERNAL_SERVER_ERROR, "파일을 저장하는 중 오류가 발생했어요.");
        }
    }
}
