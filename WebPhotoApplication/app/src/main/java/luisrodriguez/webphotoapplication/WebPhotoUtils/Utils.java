package luisrodriguez.webphotoapplication.WebPhotoUtils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import luisrodriguez.webphotoapplication.R;

/**
 * Created by Luis.Rodriguez on 3/24/2017.
 */

public class Utils {

    public static final int WRITE_EXTERNAL_STORAGE = 5;
    public static final String PHOTO_ITEMS_DATA = "photoItemsData";
    public static final int PHOTO_DETAILS = 4;


    public static String getCurrentDateTimeFormat() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy_HH_mm_ss", Locale.US);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_error_title))
                .setMessage(message)
                .setNeutralButton(R.string.dialog_action_ok, null);
        return alertDialog.create();
    }
}
