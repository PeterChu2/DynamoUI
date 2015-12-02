package com.dynamoui.android.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
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

    protected DynamoContextImpl() {
    }

    @Override
    public synchronized void init(Context context, String appName) {
        Firebase.setAndroidContext(context);
        mRef = new Firebase(String.format("%s/%s", Dynamo.END_POINT, appName));
        if(sTheme != null) {
            if ("DARK".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
            } else if ("LIGHT".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_Light);
            } else if ("DEFAULT".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat);
            } else if ("NO BAR".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_NoActionBar);
            } else if ("COMPACT MENU".equals(sTheme)) {
                context.setTheme(R.style.Theme_AppCompat_CompactMenu);
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
        return values.getString(R.styleable.DynamoButton_dynamo_id);
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
