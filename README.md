# AndroidCommonLib
Android快速开发公共库

## 文档
文档不断更新中...

地址：https://github.com/changshuai7/AndroidCommonLib/wiki

## 集成
Step 1：Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	         implementation 'com.github.changshuai7:AndroidCommonLib:1.0.3'
	}
Step 3.创建Applicaiton，必须继承BaseApplication,并在Manifest中申明

       public class MyApp extends BaseApplication {
                //code
       }

Step4.
       <application
              ...
              android:name=".MyApp"
              ...

Step5:创建主题，必须继承BaseAppTheme或者BaseAppThemeTranslucentBars（如需要全状态栏沉浸式）

    <style name="MyAppTheme" parent="BaseAppTheme">    
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
    </style>

Step6：创建Activity和Fragment的基类。Activity必须继承BaseActivity；Framgent必须继承BaseFragment 
      public class MyBaseActivity extends BaseActivity {
             //code...
      }

      public class MyBaseFragment extends BaseFragment {
             //code...
      }





## 混淆
如果你的项目需要加入混淆 ， 请加入如下配置

```java

#CommonLib
-keep class com.shuai.android.common_lib.** { *; }
-dontwarn com.shuai.android.common_lib.**

#OkHttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}


#Okio
-dontwarn okio.**
-keep class okio.**{*;}


#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#AgentWeb
-keep class com.just.agentweb.** {
    *;
}
-dontwarn com.just.agentweb.**


#Rxjava RxAndroid
-dontwarn rx.*
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQuene*Field*{
long producerIndex;
long consumerIndex;
}

-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
rx.internal.util.atomic.LinkedQueueNode producerNode;
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
rx.internal.util.atomic.LinkedQueueNode consumerNode;
}


# Gson
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson   你自己的javabean需要混淆
-keep class com.google.gson.examples.android.model.** { *; }




```
