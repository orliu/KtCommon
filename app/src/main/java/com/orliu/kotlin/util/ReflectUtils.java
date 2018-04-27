package com.orliu.kotlin.util;

import android.os.Build;
import android.os.CountDownTimer;
import android.text.InputType;
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * Created by orliu on 01/03/2018.
 */

public class ReflectUtils {

    public static void showMouseCursor(EditText et) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            et.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> clazz = EditText.class;
            Method method;
            try {
                method = clazz.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }

            try {
                method = clazz.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(et, false);
            } catch (Exception e) {
            }
        }

    }
}
