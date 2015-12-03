package com.dynamoui.android.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.dynamoui.android.core.Dynamo;
import com.dynamoui.android.core.DynamoContextImpl;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

/**
 * Created by peter on 01/12/15.
 */
public class DynamoEditText extends EditText {
    private DynamoContextImpl mContext;
    private Firebase mRef;
    private String mDynamoId;

    public DynamoEditText(Context context) {
        super(context);
        initializeListener(null, null);
    }

    public DynamoEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeListener(context, attrs);
    }

    public DynamoEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeListener(context, attrs);
    }

    private void initializeListener(Context context, AttributeSet attrs) {
        if (context == null) {
            throw new RuntimeException("dynamoId must be specified in an attribute in the xml.");
        }
        mContext = (DynamoContextImpl) Dynamo.getContext();
        if(attrs == null) {
            return;
        }
        mDynamoId = mContext.getDynamoId(context, attrs);
        if(mContext == null || mContext.getFirebaseRef() == null) {
            return;
        }
        mRef = mContext.getFirebaseRef().child("edit_texts").child(mDynamoId);
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
                            if (text != null && !text.isEmpty()) {
                                DynamoEditText.this.setText(text);
                            }
                            String color = value.get("color");

                            if (color != null && !color.isEmpty()) {
                                try {
                                    ColorDrawable background = new ColorDrawable(Color.parseColor(color));
                                    DynamoEditText.this.setBackground(background);
                                } catch (IllegalArgumentException e) {
                                    // NOP
                                }
                            }

                            String fontColor = value.get("font_color");
                            if (fontColor != null && !fontColor.isEmpty()) {
                                try {
                                    DynamoEditText.this.setTextColor(Color.parseColor(fontColor));
                                } catch (IllegalArgumentException e) {
                                    // NOP
                                }
                            }

                            String fontSize = value.get("font_size");
                            if (fontSize != null && !fontSize.isEmpty()) {
                                try {
                                    DynamoEditText.this.setTextSize(Float.valueOf(fontSize));
                                } catch (NumberFormatException e) {
                                    // NOP
                                }
                            }
                            String width = value.get("width");
                            String height = value.get("height");
                            if (width != null && !width.isEmpty()) {
                                try {
                                    DynamoEditText.this.getLayoutParams().width = Integer.valueOf(width);
                                } catch (NumberFormatException e) {
                                    // NOP
                                }
                            }
                            if (height != null && !height.isEmpty()) {
                                try {
                                    DynamoEditText.this.getLayoutParams().height = Integer.valueOf(height);
                                } catch (NumberFormatException e) {
                                    // NOP
                                }
                            }
                            String placeHolderText = value.get("input_placeholder");
                            if (placeHolderText != null && !placeHolderText.isEmpty()) {
                                DynamoEditText.this.setHint(placeHolderText);
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
