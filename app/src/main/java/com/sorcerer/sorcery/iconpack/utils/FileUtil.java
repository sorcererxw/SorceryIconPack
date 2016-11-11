package com.sorcerer.sorcery.iconpack.utils;

import java.io.File;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2016/11/11
 */

public class FileUtil {
    public static long calculateDirectorySize(File directory) {
        long result = 0;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                result += calculateDirectorySize(file);
            } else {
                result += file.length();
            }
        }
        return result;
    }

    public static void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }
}
