# ProGuard / R8 rules for Abramyan Go (Android release).
# Library-specific consumer rules ship with their AARs — keep this file focused
# on what the application itself needs.

# ===== Kotlin metadata + serialization annotations =====
-keepattributes *Annotation*, InnerClasses, Signature, Exceptions, EnclosingMethod
-keep class kotlin.Metadata { *; }

# ===== kotlinx.serialization =====
# Companion + serializer factories for every @Serializable class.
-keepclassmembers @kotlinx.serialization.Serializable class ** {
    *** Companion;
    <fields>;
    <methods>;
}
-keepclasseswithmembers class **$$serializer { static ** INSTANCE; }
-dontnote kotlinx.serialization.AnnotationsKt

# ===== Domain models + Navigation routes =====
# Models are deserialized from JSON via reflection-free serializer factories,
# but we keep them anyway to make the surface immune to obfuscation surprises.
-keep class tj.abramyan.go.data.Category { *; }
-keep class tj.abramyan.go.data.CategoryTask { *; }
-keep class tj.abramyan.go.data.CategoryTasksFileJson { *; }
-keep class tj.abramyan.go.ui.Route { *; }
-keep class tj.abramyan.go.ui.Route$* { *; }

# ===== Compose Multiplatform Resources =====
# Keep the generated Res class and its internal objects (font, files, etc.)
-keep class tj.abramyan.go.shared.resources.Res { *; }
-keep class tj.abramyan.go.shared.resources.Res$* { *; }

# ===== Enum support (none today, but cheap to keep) =====
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
