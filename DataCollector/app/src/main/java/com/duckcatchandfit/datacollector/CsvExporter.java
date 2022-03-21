package com.duckcatchandfit.datacollector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.OutputStreamWriter;

/* Sources:
 * https://stackoverflow.com/questions/14376807/read-write-string-from-to-a-file-in-android
 * https://www.geeksforgeeks.org/external-storage-in-android-with-example/
 */
public class CsvExporter {

    //#region Fields

    private final String fileName;
    private final String colSeparator = ";";

    //#endregion

    //#region Initializers

    public CsvExporter(String fileName) {
        this.fileName = fileName;
    }

    //#endregion

    //#region Public Methods

    public boolean requestPermissions(Activity activity) {
        return (requestReadExternalStoragePermission(activity) &&
            requestWriteExternalStoragePermission(activity));
    }

    public void writeHeader(Context context, ICsvData data) {
        writeToFile(context, data.toCsvHeader(this.colSeparator));
    }

    public void writeToFile(Context context, ICsvData data) {
        writeToFile(context, data.toCsvRow(this.colSeparator));
    }

    public void writeToFile(Context context, String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(
                context.openFileOutput(this.fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException ex) {
            Log.e("Exception", "File write failed: " + ex);
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

    //#endregion
}
