
package kaizone.android.b89.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.Log;

public class Utils {

    private Utils() {
    };

    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= 9;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= 11;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= 12;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 16;
    }

    public static boolean isNumber(char c) {
        if ((int) c >= 48 && (int) c <= 57)
            return true;
        return false;
    }

    public static boolean isDecimal(char c) {
        if ((int) c == 46)
            return true;
        return false;
    }

    public static int StringToInt(String str) {
        if (str == null)
            return 0;
        StringBuilder sb = new StringBuilder();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (isNumber(c[i]))
                sb.append(c[i]);
        }
        return Integer.valueOf(sb.toString());
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9.]*");
        return pattern.matcher(str).matches();
    }

    public static String numeric(String str) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static String nonNull(String str) {
        if (str == null)
            return "";
        if (str.equals("null"))
            return "";
        return str;
    }

    public static String subString(String source, String x) {
        if (TextUtils.isEmpty(source))
            return "";
        int index = source.indexOf(x);
        if (index < 0 || index > source.length())
            return source;
        return source.substring(0, index);
    }

    public static double StringToDouble(String str) {
        if (TextUtils.isEmpty(str))
            return 0.0;
        StringBuilder sb = new StringBuilder();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (isNumber(c[i]))
                sb.append(c[i]);
            if (isDecimal(c[i]))
                sb.append(c[i]);
        }
        return Double.valueOf(sb.toString());
    }

    public static String date() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        Calendar c = Calendar.getInstance();
        String date = format.format(c.getTime());
        return date;
    }

    public static String date2() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String date = format.format(c.getTime());
        return date;
    }

    public static String dateHZ() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        String date = format.format(new Date());
        return date;
    }

    public static String monthHZ() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "月";
    }

    public static String dateAfter(String datestring, int after) {
        String afterDateString = "";
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = df.parse(datestring);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, after);
            date = calendar.getTime();
            afterDateString = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterDateString;
    }

    public static String dateAfterHZ(String datestring, int after) {
        String afterDateString = "";
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            date = df.parse(datestring);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_MONTH, after);
            date = calendar.getTime();
            afterDateString = df.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterDateString;
    }

    public static Calendar stringToCalendar(String datestring) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = df.parse(datestring);
            calendar.setTime(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static String getWeekOfStringHZ(String datestring) {
        if (TextUtils.isEmpty(datestring))
            return "";
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            date = df.parse(datestring);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getWeekOfDateHZ(date);
    }

    public static String getWeekOfDateHZ(Date date) {
        String[] weekDays = {
                "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
        };
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static int compareToDay(String datestring) {
        Calendar today = Calendar.getInstance();
        Calendar moday = stringToCalendar(datestring);
        return today.compareTo(moday);
    }

    public static int percentage(int current, int total) {
        NumberFormat format = NumberFormat.getInstance();
        double percent = (double) current / total;
        String string = format.format(percent);
        string = string.replaceAll("%", "");
        double percentagef = StringToDouble(string) * 100;
        int percentage = (int) percentagef;
        return percentage;
    }

    public static String timeMMSS(int second) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(second * 1000);
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        String time = format.format(gc.getTime());
        return time;
    }

    public static void saveFile(Context context, String fileName, InputStream in) {
        try {
            FileOutputStream out = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            int end = 0;
            byte[] buffer = new byte[1024];
            while ((end = in.read(buffer)) != -1) {
                out.write(buffer, 0, end);
            }
            out.flush();
            // in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static InputStream getSaveFile(Context context, String fileName) {
        InputStream in = null;
        try {
            in = context.openFileInput(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    public static String streamToString(InputStream is, final String enc) throws Exception {
        if (is == null)
            return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, enc));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String streamToString(InputStream is) {
        if (is == null)
            return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static InputStream getAssetsFile(Context context, String fileName) {
        InputStream in = null;
        try {
            in = context.getAssets().open(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return in;
    }

    public static Date StringToDate(String datestring) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = df.parse(datestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static double doubleTo00(double d) {
        BigDecimal b = new BigDecimal(d);
        double d00 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d00;
    }

    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime()
                .getTime()) / (1000 * 60 * 60 * 24));
    }

    public static Drawable scaleDrawable(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
                drawable.getIntrinsicHeight() / 2);
        return drawable;
    }

    public static Drawable scaleDrawable(Context context, int resId, float f) {
        Drawable drawable = context.getResources().getDrawable(resId);
        int w = (int) (drawable.getIntrinsicWidth() * f);
        int h = (int) (drawable.getIntrinsicHeight() * f);
        drawable.setBounds(0, 0, w, h);
        return drawable;
    }

    public static ImageSpan imageSpan(Context context, int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth() / 2,
                drawable.getIntrinsicHeight() / 2);
        ImageSpan imageSpan = new ImageSpan(drawable);
        return imageSpan;
    }

    public static File uriToImageMedia(Activity activity, Uri uri) {
        String[] proj = {
                MediaStore.Images.Media.DATA
        };
        Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
        if (actualimagecursor == null)
            return null;
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);
        File file = new File(img_path);
        return file;
    }

    public static String uriToImageMediaPath(Activity activity, Uri uri) {
        String[] proj = {
                MediaStore.Images.Media.DATA
        };
        // Cursor actualimagecursor = activity.managedQuery(uri, proj, null,
        // null, null);
        Cursor actualimagecursor = activity.getContentResolver().query(uri, proj, null, null, null);
        if (actualimagecursor == null)
            return null;
        int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        actualimagecursor.moveToFirst();

        String img_path = actualimagecursor.getString(actual_image_column_index);
        return img_path;
    }

    public static Cursor contects(Context context) {
        // Run query
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
        };
        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" +
                "1" + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        return context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
    }

    public static void compressImage(File oriFile, File newFile, int max) {
        if (oriFile == null)
            return;
        if (oriFile.isDirectory())
            return;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(oriFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            int options = 100;
            while (baos.toByteArray().length > max) {
                Log.e("Compress Image", "compressing len =" + baos.toByteArray().length);
                if (options < 10) {
                    return;
                }
                baos.reset();
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                options -= 5;
            }

            if (!newFile.exists()) {
                newFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(newFile);
            fos.write(baos.toByteArray(), 0, baos.toByteArray().length);
            fos.close();
            baos.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void openBrowser(Context context, String urlString) {
        if (TextUtils.isEmpty(urlString))
            return;
        if (!urlString.startsWith("http"))
            return;
        Intent intent = new Intent();
        Uri uri = Uri.parse(urlString);
        intent.setData(uri);
        intent.setAction("android.intent.action.VIEW");
        context.startActivity(intent);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp,
            final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.JPEG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String randomNum() {
        long random = System.currentTimeMillis();
        return String.valueOf(random);
    }

    public static String packageName(Context ctx) {
        return ctx.getPackageName();
    }

    public static int appVersionCode(Context ctx) {
        String packgeName = packageName(ctx);
        try {
            return ctx.getPackageManager().getPackageInfo(packgeName, 0).versionCode;
        } catch (NameNotFoundException e) {

        }
        return 0;
    }

    public static String appVersionName(Context ctx) {
        String packgeName = packageName(ctx);
        try {
            return ctx.getPackageManager().getPackageInfo(packgeName, 0).versionName;
        } catch (NameNotFoundException e) {

        }
        return null;
    }

    public static String metaData(Context ctx, String key) {
        String packageName = ctx.getPackageName();
        PackageManager packageManager = ctx.getPackageManager();
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName, 128);
            Bundle bd = info.metaData;
            if (bd != null) {
                Object value = bd.get(key);
                return value.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 进行MD5加密
     * 
     * @param info 要加密的信息
     * @return String 加密后的字符串
     */
    public static String encryptToMD5(String info) {
        byte[] digesta = null;
        try {
            // 得到一个md5的消息摘要
            MessageDigest alga = MessageDigest.getInstance("MD5");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串
        String rs = byte2hex(digesta);
        return rs;
    }

    /**
     * 将二进制转化为16进制字符串
     * 
     * @param b 二进制字节数组
     * @return String
     */
    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
}
