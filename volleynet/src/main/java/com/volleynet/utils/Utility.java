package com.volleynet.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.android.volley.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * All the generic helper methods being used across different projects live here.
 */
public class Utility {

    /**
     * @param seconds seconds
     * @return formatted time in mm:ss, can be modified to any format.
     */
    public static String convertSecondsToMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        //long h = (seconds / (60 * 60)) % 24;
        return String.format("%02d:%02d", m, s);
    }

    /**
     * converts bytes to hexString
     *
     * @param bytes bytes
     * @return hex String
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Validate email
     * modified by nischay : added a additional check for empty string.
     *
     * @param email String
     * @return if email is valid or not
     */
    public static boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Used for converting a JSONObject to a HashMap.
     *
     * @param object json object
     * @return HashMap
     * @throws JSONException
     */
    public static Map<String, String> toMap(JSONObject object) throws JSONException {
        Map<String, String> map = new HashMap<>();
        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);
            map.put(key, value.toString());
        }
        return map;
    }

    public static Map<String, String> toMap(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        return toMap(jsonObject);
    }

    /**
     * Determines whether the number is a 10 digit number or not.
     *
     * @param mobileNumber mobile number
     * @return is mobile valid or not
     */
    public static boolean isTenDigitMobileNumber(String mobileNumber) {
        //TODO: Improve if required.
        return (!isNullOrEmpty(mobileNumber) && mobileNumber.trim().length() == 10);
    }

    /**
     * Determines whether the otp is a 4 digit otp or not
     *
     * @param otp OTP
     * @return is otp valid or not
     */
    public static boolean isOTPValid(String otp) {
        return (!isNullOrEmpty(otp) && otp.trim().length() == 4);
    }

    /**
     * checks a string for null & empty values
     *
     * @param str sting
     * @return is null/empty or not true, if string is Null or Empty
     */
    public static boolean isNullOrEmpty(String str) {
        return !(str != null && !str.isEmpty());
    }

    /**
     * Generates a unique device id. May not be suitable for all cases but until that's figured out, using this.
     *
     * @param context context
     * @return unique device id
     */
    public static String getUniqueDeviceID(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, androidId;
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            AppLog.d("Unique ID" + deviceUuid);
            return deviceUuid.toString();
        }
        return "";
    }

    private static int checkSelfPermission(String readPhoneState) {
        return 0;
    }

    /**
     * Checks whether internet is connected to phone.
     *
     * @param context context
     * @return true if internet is connected, false if not.
     */
    public static boolean isConnectingToInternet(Context context) {
        if (null != context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] infoArray = connectivity.getAllNetworkInfo();
                for (NetworkInfo info : infoArray) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param map Hash map to be converted into url query string
     * @return url encoded parameter string
     */
    public static String buildQueryString(final HashMap<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        TreeMap<String, String> sortedData = new TreeMap<>(map);
        for (Map.Entry<String, String> entry : sortedData.entrySet()) {
            final String encoding = "UTF-8";
            try {
                final String key = URLEncoder.encode(entry.getKey(), encoding);
                final String value = URLEncoder.encode(entry.getValue(), encoding);
                if (stringBuilder.length() > 0) {
                    stringBuilder.append("&");
                }
                stringBuilder.append(key);
                if (!isNullOrEmpty(value)) {
                    stringBuilder.append("=").append(value);
                }
            } catch (UnsupportedEncodingException exception) {
                AppLog.e(exception);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * to convert to String to  Camel case
     *
     * @param s String normal form
     * @return String Camel case
     */
    public static String convertToNameCase(String s) {
        if (s != null) {
            StringBuilder b = new StringBuilder();
            String[] split = s.split(" ");
            for (String srt : split) {
                if (srt.length() > 0) {
                    b.append(srt.substring(0, 1).toUpperCase()).append(srt.substring(1).toLowerCase()).append(" ");
                }
            }
            return b.toString().trim();
        } else {
            return null;
        }
    }

    /**
     * Creates the application link for play store
     *
     * @param context context needed to get the package name
     * @return play store link
     */
    public static String getPlayStoreLink(Context context) {
        // create play store url
        return "https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }

    /**
     * Share the text via sharing dialog
     *
     * @param context                       context needed to start the activity
     * @param sharingText                   text to be shared
     * @param sharingActivityDisplayMessage display message at the top of the sharing dialog
     */
    public static void sharePainText(Context context, String sharingText, String sharingActivityDisplayMessage) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingText);
        context.startActivity(Intent.createChooser(sharingIntent, sharingActivityDisplayMessage));
    }

    /**
     * Converts the Request.Method to the corresponding string.
     *
     * @param method http method e.g. Request.Method.POST
     * @return http method in string
     */
    public static String getHttpMethodString(int method) {
        if (method == Request.Method.DEPRECATED_GET_OR_POST) {
            return "DEPRECATED_GET_OR_POST";
        } else if (method == Request.Method.GET) {
            return "GET";
        } else if (method == Request.Method.POST) {
            return "POST";
        } else if (method == Request.Method.PUT) {
            return "PUT";
        } else if (method == Request.Method.DELETE) {
            return "DELETE";
        } else if (method == Request.Method.HEAD) {
            return "HEAD";
        } else if (method == Request.Method.OPTIONS) {
            return "OPTIONS";
        } else if (method == Request.Method.TRACE) {
            return "TRACE";
        } else if (method == Request.Method.PATCH) {
            return "PATCH";
        } else {
            return "";
        }
    }

    /**
     * This method parse string into integer.
     *
     * @param string       input
     * @param defaultValue default value in case of error.
     * @return final value.
     */
    public static int getIntegerFromString(String string, int defaultValue) {
        int number;
        try {
            number = Integer.parseInt(string);
        } catch (NumberFormatException e) {
            AppLog.e("NumberFormatException : Not valid string " + string);
            number = defaultValue;
        } catch (Exception exp) {
            number = defaultValue;
            AppLog.d("Exception : String is null " + string);
        }
        return number;
    }

    /**
     * This method parse string into double.
     *
     * @param string       input
     * @param defaultValue default value in case of error.
     * @return final value.
     */
    public static double getDoubleFromString(String string, double defaultValue) {
        double number = defaultValue;
        try {
            number = Double.parseDouble(string);
        } catch (NumberFormatException e) {
            AppLog.d("NumberFormatException : Not valid string " + string);
            number = defaultValue;
        } catch (Exception exp) {
            AppLog.d("Exception : String is null " + string);
        }
        return number;
    }

    /**
     * This method parse string double into int. .
     *
     * @param string       input
     * @param defaultValue default value in case of error.
     * @return final value.
     */
    public static int getIntegerFromDoubleString(String string, int defaultValue) {
        int number;
        try {
            number = (int) Double.parseDouble(string);
        } catch (NumberFormatException e) {
            AppLog.d("NumberFormatException : Not valid string " + string);
            number = defaultValue;
        }
        return number;
    }

    /**
     * This will provide the relative time of time in millis
     * HH 24hr format
     *
     * @param srcDate date string
     * @return relative time.
     */
    public static String getRelativeTime(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String relativeTime = DateUtils.getRelativeTimeSpanString(dateInMillis).toString();
        return relativeTime.replace("minute", "min").replace("hour", "hr").replace("second", "sec").trim();
    }

    /**
     * Returns suffix for day of the month like 1/'st'/
     *
     * @param day int day of the month
     * @return String like st,nd,rd,th
     */
    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /**
     * @param context    : Context of activity/fragment
     * @param permission : boolean value of permission
     * @return : true/false
     * @TargetAPI M : Check API level is >= M and particular permission is granted  or not
     */
    public static boolean hasPermission(Context context, boolean permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permission) {
            return true;
        }
        return false;
    }

    /**
     * Check whether permission is given to aap or not
     *
     * @param grantResults : int array of permission granted
     * @return : true/false
     */
    public static boolean checkPermissionGranted(int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public static String parseDateFormatToEEEddMMMyyyy(String dateValue, String format) {
        AppLog.d("selectedDate = " + dateValue);
        SimpleDateFormat format1 = new SimpleDateFormat(format);
        SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");


        Date date = null;
        try {
            date = format1.parse(dateValue);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        AppLog.d("selectedDate = " + format2.format(date));
        return format2.format(date);
    }

//  public static String parseDateFormatToMMMDDYYYY(String dateValue, String format) {
//    SimpleDateFormat format1 = new SimpleDateFormat(format);
//    SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
//
//
//    Date date = null;
//    try {
//      date = format1.parse(dateValue);
//    } catch (ParseException e) {
//      e.printStackTrace();
//    }
////    AppLog.d("Date value = "+format2.format(date));
//    if (date != null)
//      return format2.format(date);
//    else
//      return null;
//  }

    /**
     * Return the value mapped by the given key, or {@code null} if not present or null.
     */
    public static String optString(JSONObject json, String key) {
        if (json.isNull(key))
            return null;
        else
            return json.optString(key, null);
    }

    /**
     * @param PhoneNumber number as string
     * @return true if number is valid indian number that is either starting from 9,8,7 and following by 9 digits.
     */
    public static boolean isValidIndianMobileNumber(String PhoneNumber) {
        Pattern VALID_INDIAN_MOBILE_NUMBER_PATTERN = Pattern.compile("^[789]\\d{9}$");
        return VALID_INDIAN_MOBILE_NUMBER_PATTERN.matcher(PhoneNumber).matches();
    }

    /**
     * Validating a phone number using regular expression is tricky because the phone number can be written in many formats and can have extensions also.
     * <p>
     * For example, here are some of the common way of writing phone numbers:
     * <p>
     * 1234567890
     * 123-456-7890
     * 123-456-7890 x1234
     * 123-456-7890 ext1234
     * (123)-456-7890
     * 123.456.7890
     * 123 456 7890
     * <p>
     * <p>
     * to validate any of these format phone numbers. You can use these phone number
     *
     * @param phoneNo
     * @return
     */
    public static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{8}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{9}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{11}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{12}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{13}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{14}")) return true;
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{15}")) return true;
            //validating phone number with -, . or spaces
        else if (phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if (phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if (phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;
            //return false if nothing matches the input
        else return false;
    }
}