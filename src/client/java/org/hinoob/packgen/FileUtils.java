package org.hinoob.packgen;

import org.apache.commons.io.FileDeleteStrategy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtils {

    public static void rename(File f, String newName) {
        f.renameTo(new File(f.getParent(), newName));
    }

    public static void cut(File source, File destination) {
        try {
            org.apache.commons.io.FileUtils.copyDirectory(source, destination);
            delete(source);
        } catch (IOException e) {
            System.err.println("An error occurred while moving the file: " + e.getMessage());
        }
    }

    public static void delete(File f) {
        try {
            if(f.isDirectory()) {
                for(File c : f.listFiles()) {
                    delete(c);
                }
            }

            FileDeleteStrategy.FORCE.delete(f);
        } catch (IOException e) {
            System.err.println("An error occurred while deleting the file: " + e.getMessage());
        }
    }
}
