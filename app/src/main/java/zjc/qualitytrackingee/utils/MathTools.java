package zjc.qualitytrackingee.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by edmond on 17-1-25.
 */

public class MathTools {
    public static int findMax(int[] numbers){
        int max = Integer.MIN_VALUE;
        if(numbers.length>=0){
            for(int i=0;i<numbers.length;i++){
                if(numbers[i]>max){
                    max = numbers[i];
                }
            }
            return max;
        }else{
            return 0;
        }
    }
    public static String convertMD5(String inStr) {
        Log.e(TAG, "convertMD5: -------------------------");
        char[] a = inStr.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 't');
        }
        String s = new String(a);
        return s;

    }


    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //32位加密
            return buf.toString();
            // 16位的加密
            //return buf.toString().substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
}
