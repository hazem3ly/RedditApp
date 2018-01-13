package com.hazem.redditapp.utils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Hazem Ali.
 * On 1/12/2018.
 */

public class Navigator {


    public static void navigateToActivity(Context context, @NonNull Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }

    public static void addFragmentToView(FragmentManager fm, Fragment fragment,
                                         @IdRes int placeHolderId) {
        fm.beginTransaction().add(placeHolderId, fragment).commit();
    }

    public static void replaceFragmentInView(FragmentManager fm, Fragment fragment,
                                             @IdRes int placeHolderId) {
        fm.beginTransaction().replace(placeHolderId, fragment).commit();
    }


}
