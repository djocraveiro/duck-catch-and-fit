package com.duckcatchandfit.datacollector.storage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.*;

/* Sources:
 * https://stackoverflow.com/questions/14376807/read-write-string-from-to-a-file-in-android
 * https://www.geeksforgeeks.org/external-storage-in-android-with-example/
 */
public class CsvStorage {

    //#region Fields

    private final String fileName;
    private final String colSeparator = ";";
    private boolean hasHeader = false;

    //#endregion

    //#region Properties

    public String getFileName() { return fileName; }

    public boolean hasHeader() { return hasHeader; }

    //#endregion

    //#region Initializers

    public CsvStorage(String fileName) {
        this.fileName = fileName;
    }

    //#endregion

    //#region Public Methods

    public boolean requestPermissions(Activity activity) {
        return (requestReadExternalStoragePermission(activity) &&
            requestWriteExternalStoragePermission(activity));
    }

    public void writeHeader(Context context, ICsvData data) {
        if (!getFile(context).exists()) {
            writeToFile(context, data.toCsvHeader(this.colSeparator));
        }

        hasHeader = true;
    }

    public void writeToFile(Context context, ICsvData data) {
        writeToFile(context, data.toCsvRow(this.colSeparator));
    }

    public void writeToFile(Context context, String data) {
        try {
            FileWriter writer = new FileWriter(getFile(context), true);
            writer.append(data).append("\n");
            writer.flush();
            writer.close();
        }
        catch (IOException ex) {
            Log.e("CsvStorage", "File write failed: " + ex);
        }
    }

    //#endregion

    //#region Private Methods

    private boolean requestReadExternalStoragePermission(Activity activity) {
        int canReadState = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.READ_EXTERNAL_STORAGE);

        boolean canRead = canReadState != PackageManager.PERMISSION_GRANTED;

        if (!canRead) {
            ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                canReadState);
        }

        return canRead;
    }

    private boolean requestWriteExternalStoragePermission(Activity activity) {
        int canWriteState = ContextCompat.checkSelfPermission(activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE);

        boolean canWrite = canWriteState != PackageManager.PERMISSION_GRANTED;

        if (!canWrite) {
            ActivityCompat.requestPermissions(activity,
                new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                canWriteState);
        }

        return canWrite;
    }

    private File getFile(Context context) {
        return new File(context.getFilesDir() + "/" + fileName);
    }

    //#endregion
}
