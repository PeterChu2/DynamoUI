package com.dynamoui.android.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.example.dynamoui.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by peter on 28/11/15.
 */
public class DynamoContextImpl implements DynamoContext {
    private Firebase mRef;
    private static String sTheme = null;
    private static final String THEME_PREFS = "DYNAMO_PREF_KEY";

    protected DynamoContextImpl() {
    }

    @Override
    public synchronized void init(Context context, String appName) {
        Firebase.setAndroidContext(context);
        mRef = new Firebase(String.format("%s/%s", Dynamo.END_POINT, appName));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(prefs.contains(THEME_PREFS) && sTheme == null) {
            sTheme = prefs.getString(THEME_PREFS, "DEFAULT");
        }
        if(sTheme != null) {
            if ("DARK".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar1);
            } else if ("LIGHT".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_Light1);
            } else if ("DEFAULT".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat1);
            } else if ("NO BAR".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_NoActionBar1);
            } else if ("COMPACT MENU".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_CompactMenu1);
            }
        } else {
            initializeThemeListener(context);
        }
    }
    public Firebase getFirebaseRef() {
        return mRef;
    }
    public String getDynamoId(Context context, AttributeSet attrs) {
        TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.DynamoButton);
        String id = values.getString(R.styleable.DynamoButton_dynamo_id);
        values.recycle();
        return id;
    }
    private void initializeThemeListener(final Context context) {
        final Firebase themeRef = mRef.child("theme");
        if (context instanceof Activity) {
            themeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String theme = null;
                    if (dataSnapshot.getValue() != null) {
                        theme = dataSnapshot.getValue().toString();
                    }
                    if (theme != null) {
                        sTheme = theme;
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor e = prefs.edit();
                        e.putString(THEME_PREFS, theme);
                        e.apply();
                        Activity activity = (Activity) context;
                        activity.finish();
                        activity.startActivity(new Intent(activity, activity.getClass()));
                    }
                }
                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    // NOP
                }
            });
        }
    }
}
