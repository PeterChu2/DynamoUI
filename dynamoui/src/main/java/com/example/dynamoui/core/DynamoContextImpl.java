package com.example.dynamoui.core;

import android.content.Context;
import android.content.res.Resources;
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
    @Override
    public synchronized void init(Context context, String appName) {
        Firebase.setAndroidContext(context);
        mRef = new Firebase(String.format("%s/%s", Dynamo.END_POINT, appName));
        initializeThemeListener(context);
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
        themeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String theme = null;
                if(dataSnapshot.getValue() != null) {
                    theme = dataSnapshot.getValue().toString();
                }
                if(theme != null) {
                    if("DARK".equals(theme)) {
                        context.setTheme(R.style.Theme_AppCompat_Light_DarkActionBar);
                    } else if("LIGHT".equals(theme)) {
                        context.setTheme(R.style.Theme_AppCompat_Light);
                    } else if("DEFAULT".equals(theme)) {
                        context.setTheme(R.style.Theme_AppCompat);
                    } else if("NO BAR".equals(theme)) {
                        context.setTheme(R.style.Theme_AppCompat_NoActionBar);
                    }
//                    context.setTheme(R.style);
//                    context.setTheme(Resources.Theme.AppCompat.Light.DarkActionBar);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // NOP
            }
        });
    }
}
