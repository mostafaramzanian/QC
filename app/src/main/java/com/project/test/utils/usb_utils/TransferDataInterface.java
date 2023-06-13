package com.project.test.utils.usb_utils;

public interface TransferDataInterface {
    boolean initialConnection();

    String sendDeviceData();

    boolean getServerData(byte[] data);

    String sendFileNames();

    boolean finalHandshake();

}
