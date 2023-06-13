package com.project.test.utils.usb_utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Connection {
    public abstract InputStream getInputStream() throws IOException;

    public abstract OutputStream getOutputStream() throws IOException;

    public abstract void close() throws IOException;
}
