import kotlinx.kover.api.KoverTaskExtension

kotlin {
    jvm {
        tasks.jvmTest {
            extensions.configure<KoverTaskExtension> {
                isDisabled.set(false)
            }
        }
    }
}
