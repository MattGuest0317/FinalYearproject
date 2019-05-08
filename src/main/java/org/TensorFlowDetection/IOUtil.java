package org.TensorFlowDetection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.List;

/**
 * Util class to read image, graphDef and label files.
 */
public final class IOUtil {
    private final static Logger LOG = LoggerFactory.getLogger(IOUtil.class);
    private IOUtil() {}
    /**
     * reads in a file and returns the byte[] of the file
     * @param filePath
     * @return
     */
    static byte[] readBytesFromFile(String filePath) {

        FileInputStream fileInputStream = null;
        byte[] bytesArray = null;

        try {

            File file = new File(filePath);
            bytesArray = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bytesArray);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return bytesArray;

    }
    /**
     * 
     * @param filename
     * @return
     */
    public static List<String> readAllLinesOrExit(final String filename) {
        try {
            File file = new File(filename);
            return Files.readAllLines(file.toPath(), Charset.forName("UTF-8"));
        } catch (IOException ex) {
        	LOG.error("Failed to read [{}]", filename, ex.getMessage());
            return null;
        }
    }
    /**
     * If no output dir exsists makes a output dir
     * @param directory
     */
    public static void createDirIfNotExists(final File directory) {
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
    /**
     * 
     * @param path
     * @return
     */
    public static String getFileName(final String path) {
        return path.substring(path.lastIndexOf("/") + 1, path.length());
    }
}

