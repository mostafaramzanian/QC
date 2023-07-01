package com.project.test.utils.usb_utils;

import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utilities {


    private static final byte[] HEX_CHAR = new byte[]{'0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * Helper function that dump an array of bytes in hex form
     *
     * @param buffer The bytes array to dump
     * @return A string representation of the array of bytes
     */
    public static final String dumpBytes(byte[] buffer, int amountToDump) {
        if (buffer == null) {
            return "";
        }

        StringBuffer sb = new StringBuffer();

        amountToDump = Math.min(amountToDump, buffer.length);

        for (int i = 0; i < amountToDump; i++) {
            sb.append("0x")
                    .append((char) (HEX_CHAR[(buffer[i] & 0x00F0) >> 4]))
                    .append((char) (HEX_CHAR[buffer[i] & 0x000F])).append(" ");
        }

        return sb.toString();
    }

    public static int unsignedByte(byte b) {
        return b & 0xff;
    }

    public static int[] byteArrayToIntArray(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        for (int i = 0; i < intArray.length; ++i) {
            intArray[i] = byteArray[i] & 0xff;
        }
        return intArray;
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    public static File commonDocumentDirPath(String FolderName) {
        File dir = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + FolderName);
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/" + FolderName);
        }

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        return dir;
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }
}


