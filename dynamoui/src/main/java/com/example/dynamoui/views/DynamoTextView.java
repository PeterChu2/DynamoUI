package com.example.dynamoui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.dynamoui.core.Dynamo;
import com.example.dynamoui.core.DynamoContextImpl;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by peter on 27/11/15.
 */
public class DynamoTextView extends TextView {
    private DynamoContextImpl mContext;
    private Firebase mRef;
    private String mDynamoId;

    public DynamoTextView(Context context) {
        super(context);
        initializeListener(null, null);
    }

    public DynamoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeListener(context, attrs);
    }

    public DynamoTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeListener(context, attrs);
    }

    private void initializeListener(Context context, AttributeSet attrs) {
        if (context == null) {
            throw new RuntimeException("dynamoId must be specified in an attribute in the xml.");
        }
        mContext = (DynamoContextImpl) Dynamo.getContext();
        mDynamoId = mContext.getDynamoId(context, attrs);
        if(mContext == null || mContext.getFirebaseRef() == null) {
            return;
        }
        mRef = mContext.getFirebaseRef().child("text_views").child(mDynamoId);
        mRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> value = null;
                        if (dataSnapshot.getValue() != null) {
                            value = (HashMap<String, String>) dataSnapshot.getValue();
                        }
                        if (value != null) {
                            String text = value.get("text");
                            if (text != null) {
                                DynamoTextView.this.setText(text);
                            }
                            String color = value.get("color");
                            if (color != null) {
                                ColorDrawable background = new ColorDrawable(Color.parseColor(color));
                                DynamoTextView.this.setBackground(background);
                            }
                            String fontColor = value.get("font_color");
                            if (fontColor != null) {
                                DynamoTextView.this.setTextColor(Color.parseColor(fontColor));
                            }
                            String fontSize = value.get("font_size");
                            if(fontSize != null) {
                                DynamoTextView.this.setTextSize(Float.valueOf(fontSize));
                            }
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        // NOP
                    }
                });
    }
}
