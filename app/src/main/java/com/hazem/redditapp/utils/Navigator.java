package com.hazem.redditapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

/**
 * Created by Hazem Ali.
 * On 1/12/2018.
 */

public class Navigator {


    public static void navigateToActivity(Context context,@NonNull Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

}
