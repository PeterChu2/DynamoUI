package com.dynamoui.android.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dynamoui.android.core.Dynamo;
import com.dynamoui.android.core.DynamoContextImpl;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by peter on 28/11/15.
 */
public class DynamoWebView extends WebView {
    private DynamoContextImpl mContext;
    private Firebase mRef;
    private String mDynamoId;

    public DynamoWebView(Context context) {
        super(context);
        initializeListener(null, null);
    }

    public DynamoWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeListener(context, attrs);
    }

    public DynamoWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeListener(context, attrs);
    }

    private void initializeListener(final Context context, AttributeSet attrs) {
        if (context == null) {
            throw new RuntimeException("dynamoId must be specified in an attribute in the xml.");
        }
        mContext = (DynamoContextImpl) Dynamo.getContext();
        mDynamoId = mContext.getDynamoId(context, attrs);
        DynamoWebView.this.setWebViewClient(new WebViewClient());
        if (mContext == null || mContext.getFirebaseRef() == null) {
            return;
        }
        mRef = mContext.getFirebaseRef().child("web_views").child(mDynamoId);
        mRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> value = null;
                        if (dataSnapshot.getValue() != null) {
                            value = (HashMap<String, String>) dataSnapshot.getValue();
                        }
                        if (value != null) {
                            String pageUrl = value.get("page_url");
                            if (pageUrl != null && !pageUrl.isEmpty()) {
                                DynamoWebView.this.loadUrl(pageUrl);
                            }

                            String color = value.get("color");
                            if (color != null && !color.isEmpty()) {
                                try {
                                    ColorDrawable background = new ColorDrawable(Color.parseColor(color));
                                    DynamoWebView.this.setBackground(background);
                                } catch (IllegalArgumentException e) {
                                    // NOP
                                }
                            }

                            String width = value.get("width");
                            String height = value.get("height");
                            if (width != null && !width.isEmpty()) {
                                try {
                                    DynamoWebView.this.getLayoutParams().width = Integer.valueOf(width);
                                } catch (NumberFormatException e) {
                                    // NOP
                                }
                            }
                            if (height != null && !height.isEmpty()) {
                                try {
                                    DynamoWebView.this.getLayoutParams().height = Integer.valueOf(height);
                                } catch (NumberFormatException e) {
                                    // NOP
                                }
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
