/**
 * @author c61
 * All the global Methods LocatorApp
 */

package com.powerbtc.Constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.powerbtc.MainActivity;
import com.powerbtc.R;
import com.powerbtc.activity.SignInActivity;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;


@SuppressWarnings("ALL")
@SuppressLint("InflateParams")
public class AppGlobal {


    public static final String PREFS_NAME = "ezyexPref";
    // Email Pattern
    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
            .compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    public final static Pattern PASSWORD_NUMBER_PATTERN = Pattern
            .compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{6,}$");
    // Email Pattern
    public final static Pattern PHONE_NUMBER_PATTERN = Pattern
            .compile("^[7-9][0-9]{9}$");
    static final String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static ProgressDialog progressDialog;
    public static ViewStub stubLoader;
    public static Animation alert_show, alert_hide;
    public static boolean isAlert = false;
    public static Toast toast = null;
    public static DecimalFormat df = new DecimalFormat("#.########");

    /**
     * Email validation
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static boolean checkPhoneNumber(String phone) {
        return PHONE_NUMBER_PATTERN.matcher(phone).matches();
    }

    public static boolean checkPassword(String password) {
        return PASSWORD_NUMBER_PATTERN.matcher(password).matches();
    }

    public static void mustBeLoginToast(Context context) {
        Toast.makeText(context, "You must be login first...", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display Toast
     *
     * @param context
     * @param message
     * @param status  0 - Alert
     *                1 - success
     *                2 - error
     */
    public static void showToast(Context context, String message, int status) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Show Progress Dialog
     *
     * @param context
     * @param msg
     */
    public static void showProgressDialog(Context context) {
        try {
            if (context != null) {
                //progressDialog = CustomProgressDialog.ctor(context, msg);
                //  progressDialog.show();
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void hideProgressDialog(Context context) {
        // progressDialog.dismiss();
        try {
            if (context != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayAlertDilog(Context mContext, String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCustomTitle(View.inflate(mContext, R.layout.alert_back, null));

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button nbutton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        nbutton.setBack
        nbutton.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
    }

    /**
     * check Network Connection
     *
     * @param context
     * @return
     */
    public static boolean isNetwork(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * Store String to Preference
     *
     * @param c
     * @param value
     * @param key
     * @return
     */
    static public boolean setStringPreference(Context c, String value,
                                              String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * get String from Preference
     *
     * @param c
     * @param key
     * @return
     */
    static public String getStringPreference(Context c, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        String value = settings.getString(key, "");
        return value;
    }

    /**
     * Store integer to Preference.
     *
     * @param c
     * @param value
     * @param key
     * @return
     */
    static public boolean setIntegerPreference(Context c, int value, String key) {

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();

    }

    /**
     * get Integer from Preference
     *
     * @param c
     * @param key
     * @return
     */
    static public int getIntegerPreference(Context c, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        int value = settings.getInt(key, -1);
        return value;
    }

    /**
     * Store double to Preference.
     *
     * @param c
     * @param value
     * @param key
     * @return
     */
    static public boolean setFloatPreference(Context c, float value, String key) {

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();

    }

    /**
     * get Integer from Preference
     *
     * @param c
     * @param key
     * @return
     */
    static public float getFloatPreference(Context c, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        float value = settings.getFloat(key, -1);
        return value;
    }

    /**
     * Store boolean to Preference
     *
     * @param c
     * @param value
     * @param key
     * @return
     */
    static public boolean setBooleanPreference(Context c, Boolean value, String key) {
        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * get boolean from Preference
     *
     * @param c
     * @param key
     * @return
     */
    static public Boolean getBooleanPreference(Context c, String key) {

        SharedPreferences settings = c.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        Boolean value = settings.getBoolean(key, false);
        return value;
    }

    /**
     * get the height of the device
     *
     * @param displayMetrics - see {@link DisplayMetrics}
     * @return height of the device
     */
    public static int getDeviceHeight(Context mContex) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContex).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * get the width of the device
     *
     * @param displayMetrics - see {@link DisplayMetrics}
     * @return width of the device
     */
    public static int getDeviceWidth(Context mContex) {

        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContex).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;

    }

    /**
     * Hide Keyboard
     *
     * @param mContext
     */
    public static void hideKeyboard(Context mContext) {

        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                .getCurrentFocus().getWindowToken(), 0);

    }

    /**
     * Convert dp to PIx
     *
     * @param res
     * @param dp
     * @return
     */
    public static int dpToPx(Resources res, int dp) {

        // final float scale = res.getDisplayMetrics().density;
        // int pixels = (int) (dp * scale + 0.5f);

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                res.getDisplayMetrics());

    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /*
     * generate Unique number
     */
    public static String generateUniqueNumber() {

        char[] chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                .toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        String output = sb.toString();
        return output;

    }

    public static String GetUTCdatetimeAsString() {

        SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT,
                java.util.Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    /**
     * Checks if the application is being sent in the background (i.e behind
     * another application's Activity).
     *
     * @param context the context
     * @return <code>true</code> if another application will be above this one.
     */
    @SuppressWarnings("deprecation")
    public static boolean isApplicationSentToBackground(final Context context) {

        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // Convert String to date
    public static Date convertStringToDate(String strdate) {
        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        Date parsedDate = null;
        try {
            parsedDate = sourceFormat.parse(strdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TimeZone tz = TimeZone.getDefault();

        SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault());

        destFormat.setTimeZone(tz);

        String destDate = destFormat.format(parsedDate);

        Date date = null;
        try {
            date = destFormat.parse(destDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getNextDayDateTime(String dateTime) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateTime.split("-")[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateTime.split("-")[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(dateTime.split("-")[2]));

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static String getDateTimeInFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getNextDayDateTimeInFormat() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date date = calendar.getTime();
        return dateFormat.format(date);
    }

    public static String getImagePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static String convertToFormat(double value) {
        return df.format(value);
    }

    public static String pasteData(Context mContext) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard.hasPrimaryClip()) {
            ClipDescription description = clipboard.getPrimaryClipDescription();
            android.content.ClipData data = clipboard.getPrimaryClip();
            if (data != null && description != null && description.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                return "" + data.getItemAt(0).getText();
            }

        }
        return "";
    }

    public static String createAPIString() {
        String strendem = randomString(30);
        char[] charArray = strendem.toCharArray();
        charArray[4] = 'F';
        charArray[15] = '@';
        charArray[21] = 'm';
        charArray[27] = '8';
        String token = new String(charArray);
        Log.e("PowerBTC", "Token : " + token);
        return token;
    }

    /*public static String random(int length) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        char tempChar;
        for (int i = 0; i < 30; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }*/

    // get only date
    public String getDateString(String strdate) {
        Date date = convertStringToDate(strdate);
        SimpleDateFormat tmp = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        String str = tmp.format(date);
        return str;
    }

    public static String randomString(int len) {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void logoutApplication(Activity mContext) {
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_REGID);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_NAME);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_USERNAME);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_EMAIL);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_PASSSWORD);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_TRANSACTION_PASSSWORD);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_MOBILE);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_PROFILE_PIC);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_STATUS);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_EMAIL_VERIFY);

        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_LOGIN_SET_PIN);

        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_ADDRESS_RES);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_CITY);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_STATE);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_PICODE);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_COUNTRY);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_COUNTRY_CODE);

        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_WALLET_BTC_ADDRESS);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_WALLET_GUC_ADDRESS);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_WALLET_GUC_BALANCE);
        AppGlobal.setStringPreference(mContext, "", WsConstant.SP_WALLET_GUC_FEES);

        Intent intent = new Intent(mContext, SignInActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
        mContext.finish();
    }
}
