package io.github.droidkaigi.confsched.data.about

import io.github.droidkaigi.confsched.model.buildconfig.BuildConfigProvider

public class FakeBuildConfigProvider : BuildConfigProvider {
    override val versionName: String
        get() = "1.0.0"
}
