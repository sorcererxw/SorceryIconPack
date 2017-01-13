package com.sorcerer.sorcery.iconpack.utils;

import com.annimon.stream.Stream;

import java.io.File;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

public class FileUtil {
    public static long calculateDirectorySize(File directory) {
        if (directory == null || directory.listFiles() == null) {
            return 0;
        }
        return Stream.of(directory.listFiles())
                .mapToLong(file -> {
                    if (file.isDirectory()) {
                        return calculateDirectorySize(file);
                    } else {
                        return file.length();
                    }
                })
                .sum();
    }

    public static void deleteDirectory(File directory) {
        if (directory == null || !directory.exists() || directory.listFiles() == null) {
            return;
        }
        Stream.of(directory.listFiles())
                .forEach(file -> {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                });
        directory.delete();
    }
}
