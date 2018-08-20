# MediaMonkey Plugin SDK
Glue interface for MediaMonkey and MediaMonkey Plugins.

You can include this plugin as submodule of your Android studio project, or
simply build as `aar` format via `gradlew assembleRelease` command and
include it directly under `libs` directory of your project structure.

More information about building MediaMonkey Plugin, please refer to
[Plugin implementation demo](https://github.com/FrancescoJo/MediaMonkeyPlugin)
documentation.

# Package description
1. `com.mediamonkey.android.app`:
Provides MediaMonkey APIs to plugin implementation. Plugins can utilise
required features via interfaces in this package.

2. `com.mediamonkey.android.lib`:
Collections of DTOs and libraries both used by MediaMonkey frontend and
plugins. Beware that, classes of `com.mediamonkey.android.lib.internal` are
subject to unnoticed change, so don't use those in your logic.

3. `com.mediamonkey.android.plugin`:
Classes that, all plugins must implement for providing function
to MediaMonkey Frontend. The Frontend will communicate to plugins via
`MediaMonkeyPlugin` class, so its implementation is the most crucial.
Your plugin is cannot be loaded without the implementation.