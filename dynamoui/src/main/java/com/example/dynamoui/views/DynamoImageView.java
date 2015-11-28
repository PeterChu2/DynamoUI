package com.example.dynamoui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.dynamoui.R;
import com.example.dynamoui.core.Dynamo;
import com.example.dynamoui.core.DynamoContextImpl;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.koushikdutta.ion.Ion;

import java.util.HashMap;

/**
 * Created by peter on 28/11/15.
 */
public class DynamoImageView extends ImageView {
    private DynamoContextImpl mContext;
    private Firebase mRef;
    private String mDynamoId;

    public DynamoImageView(Context context) {
        super(context);
        initializeListener(null, null);
    }

    public DynamoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeListener(context, attrs);
    }

    public DynamoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeListener(context, attrs);
    }

    public DynamoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeListener(context, attrs);
    }

    private void initializeListener(final Context context, AttributeSet attrs) {
        if (context == null) {
            throw new RuntimeException("dynamoId must be specified in an attribute in the xml.");
        }
        mContext = (DynamoContextImpl) Dynamo.getContext();
        mDynamoId = mContext.getDynamoId(context, attrs);
        mRef = mContext.getFirebaseRef().child("image_views").child(mDynamoId);
        mRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> value = null;
                        if (dataSnapshot.getValue() != null) {
                            value = (HashMap<String, String>) dataSnapshot.getValue();
                        }
                        if (value != null) {
                            String imgUrl = value.get("img_url");
                            if (imgUrl != null) {
                                Ion.with(DynamoImageView.this)
                                        .load(imgUrl);
                            }
                            String color = value.get("color");
                            if (color != null) {
                                ColorDrawable background = new ColorDrawable(Color.parseColor(color));
                                DynamoImageView.this.setBackground(background);
                            }

                            String width = value.get("width");
                            String height = value.get("height");
                            if(width != null) {
                                DynamoImageView.this.getLayoutParams().width = Integer.valueOf(width);
                            }
                            if(height != null) {
                                DynamoImageView.this.getLayoutParams().height = Integer.valueOf(height);
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
