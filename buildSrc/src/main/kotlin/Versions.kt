object Versions {
    private const val sdkCoreVersion = "0.0.2-SNAPSHOT"
    private const val sdkAppVersion = "0.0.2-SNAPSHOT"
    private const val NONE = "NONE"

    val jarVersion = mapOf(
        "sdk-app" to sdkAppVersion,
        "sdk-core" to sdkCoreVersion,
        NONE to NONE
    )
}