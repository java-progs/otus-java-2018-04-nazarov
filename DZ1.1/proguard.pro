-verbose

-optimizationpasses 3
-overloadaggressively
-repackageclasses ''
-allowaccessmodification

-keep class com.google.common.base.** { *; }
-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

-dontwarn com.google.common.hash**
-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**