package com.project.test.view.activity.usb_sync;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbAccessory;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.project.test.R;
import com.project.test.model.Database;
import com.project.test.model.DatabaseReportsAsJson;
import com.project.test.utils.usb_utils.Connection;
import com.project.test.utils.usb_utils.TransferDataInterface;
import com.project.test.utils.usb_utils.UsbConnection;
import com.project.test.utils.usb_utils.Utilities;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.Objects;

public class SyncDataActivity extends Activity implements Callback, Runnable, TransferDataInterface {

    static final byte CMD_SETTINGS = 12; // () ->
    private static final boolean gLogPackets = true;
    private static SyncDataActivity sSyncDataActivity = null;
    Connection mConnection;
    private Handler mDeviceHandler;
    private UsbManager mUSBManager;
    private byte[] mQueryBuffer = new byte[4];
    private byte[] mEmptyPayload = new byte[0];
    private byte[] myBuffer = null;
    private int totalByteTransferred = 0;
    private int lastCommand = -1;
    private ProgressBar progressBarView;
    private TextView syncPercent;
    private String tmpFileName = null;

    public static SyncDataActivity get() {
        return sSyncDataActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sync_data);

        syncPercent = findViewById(R.id.sync_percent);

        progressBarView = findViewById(R.id.sync_progress_bar);
        progressBarView.setMax(100);
        progressBarView.setProgress(0);
        Log.d("~~~~~~~~~~", String.valueOf(progressBarView));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
//            Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
//            startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri));
//        }

        mDeviceHandler = new Handler(this);

        mUSBManager = (UsbManager) getSystemService(USB_SERVICE);

        connectToAccessory();

        sSyncDataActivity = this;

        findViewById(R.id.close_window).setOnClickListener(v -> {
            disconnectFromAccessory();
            disconnect();
        });


//
//        findViewById(R.id.clock_button).setOnClickListener(v -> {
//            sendCommand(1, 0, "DQysVDUBDlrxLtybUeUPqBgbOHFyIfhUpBFKWNMcHcPJbtPTPcMcUWlCiecgvMuEoZBEPpCFuuQhIwRshEEROptafgdNqxxTBOBKVGzGsGKSppikQwDuPvtkpxPnGABSzzIMZvCnPKMyoBQmMAqcAduxfGYYwmqPEDMSmRXMZMRoKINdYYAZMnBrnCZEAuCvcZBUgreTTPHAPCvhHoefjuaJOCeDDQysVDUBDlrxLtybUeUPqBgbOHFyIfhUpBFKWNMcHcPJbtPTPcMcUWlCiecgvMuEoZBEPpCFuuQhIwRshEEROptafgdNqxxTBOBKVGzGsGKSppikQwDuPvtkpxPnGABSzzIMZvCnPKMyoBQmMAqcAduxfGYYwmqPEDMSmRXMZMRoKINdYYAZMnBrnDQysVDUBDlrxLtybUeUPqBgbOHFyIfhUpBFKWNMcHcPJbtPTPcMcUWlCiecgvMuEoZBEPpCFuuQhIwRshEEROptafgdNqxxTBOBKVGzGsGKSppikQwDuPvtkpxPnGABSzzIMZvCnPKMyoBQmMAqcAduxfGYYwmqPEDMSmRXMZMRoKINdYYAZMnBrnDQysVDUBDlrxLtybUeUPqBgbOHFyIfhUpBFKWNMcHcPJbtPTPcMcUWlCiecgvMuEoZBEPpCFuuQhIwRshEEROptafgdNqxxTBOBKVGzGsGKSppikQwDuPvtkpxPnGABSzzIMZvCnPKMyoBQmMAqcAduxfGYYwmqPEDMSmRXMZMRoKINdYYAZMnBrnCZEAuCvcZBUgreTTPHAPCvhHoefjuaJOCeDeQSdVnMeOJNXMOpfRJFiPJDDogWVZDCWxoLXrIVvrdAdEiFVWBTZPRUyDbFxigsLLoKCQSCZEAuCvcZBUgreTTPHAPCvhHoefjuaJOCeDeQSdVnMeOJNXMOpfRJFiPJDDogWVZDCWxoLXrIVvrdAdEiFVWBTZPRUyDbFxigsLLoKCQSZEAuCvcZBUgreTTPHAPCvhHoefjuaJOCeDeQSdVnMeOJNXMOpfRJFiPJDDogWVZDCWxoLXrIVvrdAdEiFVWBTZPRUyDbFxigsLLoKCQSeQSdVnMeOJNXMOpfRJFiPJDDogWVZDCWxoLXrIVvrdAdEiFVWBTZPRUyDbFxigsLLoKCQS".getBytes());
//        });
//        findViewById(R.id.alarm_button).setOnClickListener(v -> {
//            sendCommand(2, 0);
//        });
//        findViewById(R.id.volume_button).setOnClickListener(v -> {
//            sendCommand(3, 0);
//        });
//        findViewById(R.id.lock_button).setOnClickListener(v -> {
//            disconnectFromAccessory();
//            disconnect();
//
//        });


    }

    public void writeIntoFile(Context context, String fileName, byte[] content) throws IOException {
//        File appSpecificInternalStorageDirectory = context.getFilesDir();
//        File file = new File(appSpecificInternalStorageDirectory, fileName);
        File appSpecificExternalStorageDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        File dir = new File(appSpecificExternalStorageDirectory + "/" + "instructions");

        // Make sure the path directory exists.
        if (!dir.exists()) {
            // Make it, if it doesn't exit
            boolean success = dir.mkdirs();
            if (!success) {
                dir = null;
            }
        }
        File file = new File(dir, fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file, false);
        fos.write(content);
        fos.close();
    }

    public String readFromFile(String filePath) throws IOException {
//        File appSpecificInternalStorageDirectory = this.getFilesDir();
//        File file = new File(appSpecificInternalStorageDirectory, filePath);
        File appSpecificExternalStorageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        File file = new File(appSpecificExternalStorageDirectory + "/" + "instructions", filePath);
        FileInputStream fis = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        fis.close();
        return builder.toString();
    }


    private void copyDatabase() {
//        Log.w(TAG, "copying database from assets...");


        String mDatabasePath = getApplicationInfo().dataDir + "/databases";
        String dest = mDatabasePath + "/QC.db";

        InputStream is = null;
        try {
            is = openFileInput("tmp_db.db");

            try {
                File f = new File(mDatabasePath + "/");
                if (!f.exists()) {
                    f.mkdir();
                }

                Utilities.writeExtractedFileToDisk(is, new FileOutputStream(dest));

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean initialConnection() {
        return false;
    }

    @Override
    public String sendDeviceData() {


//        try {
//            JSONObject j = new JSONObject();
//            j.put("cp_reports", new JSONArray());
//            j.put("cp_reports_info", new JSONArray());
//            j.put("cp_reports_parameters", new JSONArray());
//            return j.toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "{}";

        try {
            DatabaseReportsAsJson databaseReportsAsJson = new DatabaseReportsAsJson(this);
            return databaseReportsAsJson.getJson().toString();

        } catch (Exception e) {
            return "{}";
        }

    }

    @Override
    public boolean getServerData(byte[] data) {

        try (FileOutputStream fos = openFileOutput("tmp_db.db", Context.MODE_PRIVATE)) {
            fos.write(myBuffer);

            try (Database database = new Database(this)) {
                if (database.getWritableDatabase().isOpen()) {
                    database.getWritableDatabase().close();
                    database.close();
                }
            }

            copyDatabase();

            return true;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return false;

    }

    @Override
    public String sendFileNames() {

        JSONArray j = new JSONArray();

        try {
            File appSpecificExternalStorageDirectory = this.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            File file = new File(appSpecificExternalStorageDirectory + "/" + "instructions");
            for (File listFile : Objects.requireNonNull(file.listFiles())) {
                j.put(listFile.getName());
            }

        } catch (Exception e) {

        }

        return j.toString();
    }

    @Override
    public boolean finalHandshake() {
        return false;
    }

    private void disconnect() {
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        sSyncDataActivity = null;
        if (mConnection != null) {
            try {
                mConnection.close();
            } catch (IOException e) {
            } finally {
                mConnection = null;
            }
        }
        super.onDestroy();
        System.exit(0);
    }

    public void connectToAccessory() {
        // bail out if we're already connected
        if (mConnection != null)
            return;

        // assume only one accessory (currently safe assumption)
        UsbAccessory[] accessories = mUSBManager.getAccessoryList();
        UsbAccessory accessory = (accessories == null ? null : accessories[0]);
        if (accessory != null) {
            if (mUSBManager.hasPermission(accessory)) {
                openAccessory(accessory);
            } else {
            }
        } else {
            // Log.d(TAG, "mAccessory is null");
        }


    }

    private void saveFile(String fileName, byte[] file) {

        Log.d("~~~~~~~~~", fileName + "  " + file.length);
        try {
            writeIntoFile(this, fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void disconnectFromAccessory() {
        closeAccessory();
    }

    private void openAccessory(UsbAccessory accessory) {
        mConnection = new UsbConnection(this, mUSBManager, accessory);
        performPostConnectionTasks();
    }

    private void performPostConnectionTasks() {
        Thread thread = new Thread(null, this, "ADK 2012");
        thread.start();

        mDeviceHandler.postDelayed(() -> {
            sendCommand(1, 0);
        }, 3000);
    }

    public void closeAccessory() {
        try {
            mConnection.close();
        } catch (Exception e) {
        } finally {
            mConnection = null;
        }
    }

    public void terminateAfterDetached() {
        runOnUiThread(() -> {
            ((TextView) findViewById(R.id.sync_text)).setText("دستگاه جدا شد.\nدر حال بستن برنامه...");
        });
        mDeviceHandler.postDelayed(this::onDestroy, 2000);
    }

    private void process_full_packet(int command) {

        Log.i("~~~~~~", "command " + command);
        Log.i("~~~~~~", "read " + totalByteTransferred);

        switch (command) {
            case 100: {

                sendCommand(2, 0, sendDeviceData().getBytes());
                runOnUiThread(() -> {
                    progressBarView.setProgress(10);
                    syncPercent.setText("10%");
                });
                break;
            }
            case 101: {
                if (getServerData(myBuffer)) {
                    sendCommand(3, 0, sendFileNames().getBytes());
                    runOnUiThread(() -> {
                        progressBarView.setProgress(30);
                        syncPercent.setText("30%");
                    });
                } else {
                    sendCommand(22, 0);
                }
                break;
            }
            case 102: {
                tmpFileName = new String(myBuffer);
                break;
            }
            case 103: {
                if (tmpFileName != null) {
                    saveFile(tmpFileName, myBuffer);
                    tmpFileName = null;
                }
                break;
            }
            case 104: {
                runOnUiThread(() -> {
                    progressBarView.setProgress(80);
                    syncPercent.setText("80%");
                });
                sendCommand(4, 0);
                break;
            }
            case 105: {
                runOnUiThread(() -> {
                    progressBarView.setProgress(100);
                    syncPercent.setText("100%");
                    ((TextView) findViewById(R.id.sync_text)).setText("تبادل با موفقیت به اتمام رسید.\nدستگاه را جدا نمایید.");
                    findViewById(R.id.large_progressbar).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.sync_image)).setBackgroundResource(R.drawable.connected);
                    progressBarView.setVisibility(View.GONE);
                    syncPercent.setVisibility(View.GONE);
                    findViewById(R.id.close_window).setVisibility(View.VISIBLE);
                });

                mDeviceHandler.postDelayed(this::disconnectFromAccessory, 1000);
                break;
            }

            // START ERROR HANDLING CASES
            case 201:
            case 202:
            case 203:
            case 204: {
                runOnUiThread(() -> {
                    progressBarView.setVisibility(View.GONE);
                    ((TextView) findViewById(R.id.sync_text)).setText("خطا در تبادل اطلاعات.\nدستگاه را جدا نموده و مجددا وصل کنید.");
                    findViewById(R.id.large_progressbar).setVisibility(View.GONE);
                    ((ImageView) findViewById(R.id.sync_image)).setBackgroundResource(R.drawable.disconnected);
                    syncPercent.setVisibility(View.GONE);
                    findViewById(R.id.close_window).setVisibility(View.VISIBLE);

                    mDeviceHandler.postDelayed(this::disconnectFromAccessory, 1000);
                });

                break;
            }
        }

        myBuffer = null;
        totalByteTransferred = 0;
        lastCommand = -1;
    }


    public void run() {
        int ret = 0;

        byte[] buffer = new byte[16384];
        int bufferUsed = 0;

//        ProtocolHandler ph = null;

        while (ret >= 0) {
            try {
                ret = mConnection.getInputStream().read(buffer);


                Log.d("~~~~~~", "RETURN BYTE = " + ret);

                if (buffer[0] == 44 && buffer[1] == 45 && buffer[2] == 46) {

                    runOnUiThread(() -> {
                        progressBarView.setIndeterminate(false);
                    });

                    lastCommand = Utilities.unsignedByte(buffer[3]);
                    Log.d("~~~~~~", "COMMAND = " + lastCommand);

                    int payloadLength = ((buffer[4] & 0xff) << 24) | ((buffer[5] & 0xff) << 16) | ((buffer[6] & 0xff) << 8) | (buffer[7] & 0xff);
                    Log.d("~~~~~~", "PAYLOAD LENGTH = " + payloadLength);

                    myBuffer = new byte[payloadLength];

                    if (ret < 16384) {

                        if (buffer[ret - 1] == 0 && buffer[ret - 2] == 1 && buffer[ret - 3] == 2 && buffer[ret - 4] == 3) {
                            System.arraycopy(buffer, 8, myBuffer, 0, ret - 12);
                            totalByteTransferred = ret - 12;
                            process_full_packet(lastCommand);
                        } else {
                            System.arraycopy(buffer, 8, myBuffer, 0, ret - 8);
                            totalByteTransferred = ret - 8;
//                            Log.d("~~~~~~~", "INVALID PACKET BUFFER");
                        }

                    }

                } else {

                    if (buffer[ret - 1] == 0 && buffer[ret - 2] == 1 && buffer[ret - 3] == 2 && buffer[ret - 4] == 3) {
                        System.arraycopy(buffer, 0, myBuffer, totalByteTransferred, ret - 4);
                        totalByteTransferred += ret - 4;
                        process_full_packet(lastCommand);
                    } else {
                        System.arraycopy(buffer, 0, myBuffer, totalByteTransferred, ret);
                        totalByteTransferred += ret;
                    }

                }


            } catch (IOException e) {
                Log.d("~~~~~~~~~~~~~~~E run", e.toString());
                break;
            }
        }

    }

    public byte[] sendCommand(int command, byte[] payload, byte[] buffer) {
        Log.i("~~~~~~", String.format("payload length %d", payload.length));
        int bufferLength = payload.length + 12;
        if (buffer == null || buffer.length < bufferLength) {
            Log.i("~~~~~~", "allocating new command buffer of length " + bufferLength);
            buffer = new byte[bufferLength];
        }

        buffer[0] = (byte) 41;
        buffer[1] = (byte) 42;
        buffer[2] = (byte) 43;
        buffer[3] = (byte) command;

//        buffer[3] = (byte) (payload.length & 0xff);
        if (payload.length > 0) {
            byte[] hbhbytes = ByteBuffer.allocate(4).putInt(payload.length).array();
            System.arraycopy(hbhbytes, 0, buffer, 4, 4);
            System.arraycopy(payload, 0, buffer, 8, payload.length);
        } else {
            byte[] hbhbytes = ByteBuffer.allocate(4).putInt(0).array();
            System.arraycopy(hbhbytes, 0, buffer, 4, 4);
        }
        buffer[payload.length + 8] = '\3';
        buffer[payload.length + 9] = '\2';
        buffer[payload.length + 10] = '\1';
        buffer[payload.length + 11] = '\0';


        if (mConnection != null && buffer[1] != -1) {
            try {
                if (gLogPackets) {
                    Log.i("~~~~~~", "sendCommand: " + Utilities.dumpBytes(buffer, buffer.length));
                }
                mConnection.getOutputStream().write(buffer);
            } catch (IOException e) {
                Log.e("~~~~~~", "accessory write failed", e);
            }
        }
        return buffer;
    }

    public void sendCommand(int command, int sequence, byte[] payload) {
//        sendCommand(command, sequence, payload, null);
        sendCommand(command, payload, null);

    }

    private void sendCommand(int command, int sequence) {
//        sendCommand(command, sequence, mEmptyPayload, mQueryBuffer);
        sendCommand(command, mEmptyPayload, mQueryBuffer);
    }


    public boolean handleMessage(Message msg) {
        if (msg.getTarget() == mDeviceHandler) {
            return handleDeviceMethod(msg);
        } else {
            return true;
        }
    }

    private boolean handleDeviceMethod(Message msg) {
        switch (msg.what) {
            case CMD_SETTINGS:
//                handleSettingsCommand((byte[]) msg.obj);
                return true;

        }
        return false;
    }


}
