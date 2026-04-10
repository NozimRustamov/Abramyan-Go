# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# Keep Kotlin metadata for reflection
-keep class kotlin.Metadata { *; }

# SQLDelight
-keep class app.cash.sqldelight.** { *; }
-keep interface app.cash.sqldelight.** { *; }

# Koin
-keepnames class * implements org.koin.core.KoinComponent

# Kotlin Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    <fields>;
    <methods>;
}

# Keep data classes
-keep class com.abramyango.domain.model.** { *; }
-keep class com.abramyango.data.db.** { *; }

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Ktor (if used in future)
-dontwarn io.ktor.**
-keep class io.ktor.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Enum
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
