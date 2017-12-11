# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#Toothpick
-keep class com.package.** { *; }

#requery
-keep class * extends java.lang.Enum {
}
-dontwarn rx.internal.**
-dontwarn android.support.**

#< Stetho
-keep class com.facebook.stetho.** { *; }
# Stetho>

#< com.squareup.okhttp3
-dontwarn okhttp3.**
-dontwarn okio.**
# com.squareup.okhttp3>

#< Retrolambda
-dontwarn java.lang.invoke.*
# Retrolambda>

#< Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
# Retrofit 2.X>
