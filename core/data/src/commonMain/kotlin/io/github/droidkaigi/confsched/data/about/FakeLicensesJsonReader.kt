package io.github.droidkaigi.confsched.data.about

public class FakeLicensesJsonReader : LicensesJsonReader {
    override suspend fun readLicensesJson(): String = ""
}
