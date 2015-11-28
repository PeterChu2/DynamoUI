package com.example.dynamoui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    public DynamoWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeListener(context, attrs);
    }

    private void initializeListener(final Context context, AttributeSet attrs) {
        if (context == null) {
            throw new RuntimeException("dynamoId must be specified in an attribute in the xml.");
        }
        mContext = (DynamoContextImpl) Dynamo.getContext();
        mDynamoId = mContext.getDynamoId(context, attrs);
        DynamoWebView.this.setWebViewClient(new WebViewClient());
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
                            if (pageUrl != null) {
                                DynamoWebView.this.loadUrl(pageUrl);
                            }

                            String color = value.get("color");
                            if (color != null) {
                                ColorDrawable background = new ColorDrawable(Color.parseColor(color));
                                DynamoWebView.this.setBackground(background);
                            }

                            String width = value.get("width");
                            String height = value.get("height");
                            if(width != null) {
                                DynamoWebView.this.getLayoutParams().width = Integer.valueOf(width);
                            }
                            if(height != null) {
                                DynamoWebView.this.getLayoutParams().height = Integer.valueOf(height);
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
