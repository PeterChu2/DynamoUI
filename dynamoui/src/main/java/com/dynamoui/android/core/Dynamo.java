package com.dynamoui.android.core;

/**
 * Created by peter on 28/11/15.
 */
public class Dynamo {
    public static String END_POINT = "https://hackwestern.firebaseio.com/";
    private static DynamoContext sContext;
    public static synchronized DynamoContext getContext() {
        if(sContext == null) {
            sContext = new DynamoContextImpl();
            return sContext;
        } else {
            return sContext;
        }
    }
}
