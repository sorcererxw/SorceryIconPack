package com.sorcererxw.sorcery.icons.packtools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @description:
 * @author: Sorcerer
 * @date: 2017/5/1
 */

public class Utils {
    static String readFile(File file) throws FileNotFoundException {
        return new Scanner(file).useDelimiter("\\Z").next();
    }

    static void writeFile(File file, String s) throws IOException {
//        FileUtils.writeStringToFile(file,s,"UTF-8");
        FileUtils.write(file, s, Charset.defaultCharset(), false);
    }

}
