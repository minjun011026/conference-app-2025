package io.github.confsched.profile.components

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import io.github.droidkaigi.confsched.model.profile.ProfileCardTheme

private val LightBaseColor = Color(0xFFF2F4FB)
private val DarkBaseColor = Color(0xFF280F83)

val ProfileCardTheme.baseColor
    get() = if (isDark) DarkBaseColor else LightBaseColor

val ProfileCardTheme.isDark: Boolean
    get() {
        return when (this) {
            ProfileCardTheme.DarkPill,
            ProfileCardTheme.DarkDiamond,
            ProfileCardTheme.DarkFlower -> true

            ProfileCardTheme.LightPill,
            ProfileCardTheme.LightDiamond,
            ProfileCardTheme.LightFlower -> false
        }
    }

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
val ProfileCardTheme.shape: Shape
    @Composable
    get() {
        return when (this) {
            ProfileCardTheme.DarkPill,
            ProfileCardTheme.LightPill
                -> MaterialShapes.Pill.toShape()

            ProfileCardTheme.DarkDiamond,
            ProfileCardTheme.LightDiamond
                -> MaterialShapes.Diamond.toShape()

            ProfileCardTheme.DarkFlower,
            ProfileCardTheme.LightFlower
                -> MaterialShapes.Flower.toShape()
        }
    }
