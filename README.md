DynamoUI
====================

Android library to create Android UI elements that have dynamic properties in real-time.

Integrate into your app:
---------------------
1.  Add module to your application's build.gradle.
```
compile 'com.example.dynamoui:dynamoui:1.0@aar'
    compile 'com.android.support:design:23.1.1'
    compile 'com.android.support:appcompat-v7:23.1.1'
    compile 'com.firebase:firebase-client-android:2.4.1+'
    compile 'com.koushikdutta.ion:ion:2.+'
```
Add this to your android object in your app's build.gradle:
```
packagingOptions {
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE-FIREBASE.txt'
        exclude 'META-INF/NOTICE'
    }
```

Add this to your project's build.gradle.
```
allprojects {
    repositories {
        jcenter()
        mavenLocal()
    }
}
```

2.  Initialize the Dynamo Context in your Activity's onCreate before setting the content view.
```
    Dynamo.getContext().init(this, "MyCoolApp");
```
3.  Add Dynamo UI Elements to your activity's layout. Ensure a dynamo_id is assigned that matches the dashboard dynamo_id.
```
    <com.example.dynamoui.views.DynamoTextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:id="@+id/dynamo_text_view"
            android:text="Hello World!"
            app:dynamo_id="text_view_1" />
```
