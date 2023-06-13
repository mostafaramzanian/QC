package com.project.test.utils.usb_utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.project.test.view.activity.usb_sync.SyncDataActivity;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UsbConnection extends Connection {
    private static final String ACTION_USB_PERMISSION = "com.google.android.DemoKit.action.USB_PERMISSION";
    Activity mActivity;
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_ACCESSORY_DETACHED.equals(action)) {
                Log.i("~~~~~~", "closing accessory");

                mActivity.unregisterReceiver(mUsbReceiver);

                ((SyncDataActivity) mActivity).terminateAfterDetached();

//				UsbAccessory accessory = intent.getParcelableExtra(UsbManager.EXTRA_ACCESSORY);
//				UsbAccessory accessory = UsbManager.getAccessory(intent);
//				if (accessory != null && accessory.equals(mAccessory)) {
//					Log.i("~~~~~~", "closing accessory");
//					Intent connectIntent = new Intent(mActivity,ConnectActivity.class);
//					connectIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					mActivity.startActivity(connectIntent);
//				}
            }
        }
    };
    FileInputStream mInputStream;
    FileOutputStream mOutputStream;
    ParcelFileDescriptor mFileDescriptor;
    UsbAccessory mAccessory;

    public UsbConnection(Activity activity, UsbManager usbManager,
                         UsbAccessory accessory) {
        mActivity = activity;
        mFileDescriptor = usbManager.openAccessory(accessory);
        if (mFileDescriptor != null) {
            mAccessory = accessory;
            FileDescriptor fd = mFileDescriptor.getFileDescriptor();
            mInputStream = new FileInputStream(fd);
            mOutputStream = new FileOutputStream(fd);
        }
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_DETACHED);
        mActivity.registerReceiver(mUsbReceiver, filter);
    }

    @Override
    public InputStream getInputStream() {
        return mInputStream;
    }

    @Override
    public OutputStream getOutputStream() {
        return mOutputStream;
    }

    public void close() throws IOException {
        if (mFileDescriptor != null) {
            mFileDescriptor.close();
        }

    }

}
