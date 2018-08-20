# MediaMonkey Plugin skeleton
As title denotes. No-op implementation of MediaMonkeyPluginSdk.

## How to build
1. Checkout git submodule `MediaMonkeyPluginSdk`
2. Implement abstract class `com.mediamonkey.android.lib.MediaMonkeyPlugin`
3. Update plugin description file `pkg-info.xml` in `config` directory -
   contents are must be well-formed.
4. Invoke `build-plugin.sh` and deploy your plugin package built in 
   `build` directory. The plugin package must contains `pkg-info.xml` and
   `classes.dex` in a single file.

## Caveats
### 1. What you can
  1. Access to Android API which are not relying on `android.app.Context`
  2. Unsafe access to some Android API depend on `android.app.Context`
     such as `Toast`, `Spannable`, etc.
  3. Access to MediaMonkey API via
     `com.mediamonkey.android.lib.MediaMonkeyPlugin`

### 2. What you can, but not recommended
  1. **Fine-grain control of android View and Widget**
     Because you can't rely on Android's automatic resources resolution(
     locale, configuration, display, etc.), therefore you must write your
     own logic to cope various Android runtime configuration. Reinventing
     a wheel would be god damn tedious and difficult, so do not dare.

  2. **Accessing internal resources located in JAR via Classloader**
     Whenever you attempt to load resource file located in your classpath
     which would be packed into plugin archive on Android runtime,
     Android will read archive file's entry for resource loading and
     loads specified file via `ZipUrlConnection`. Unfortunately, this
     stream is a target of garbage collection, so there will be additional
     file deflation overhead.
  
     Therefore, if you want to distribute your plugin package with
     resource files, it is better to unzip your files first to
     MediaMonkey's internal cache directory rather than directly accessing
     it via `Classloader` to avoid performance degradation.
     The cache directory path  can be obtained via
     `com.mediamonkey.android.lib.MediaMonkeyLib`.

### 3. What you cannot
  1. Referencing android resources via R.class
     Read the very first description of this documentation.