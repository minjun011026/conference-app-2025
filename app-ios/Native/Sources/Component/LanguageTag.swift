import Extension
import Model
import SwiftUI
import Theme

public struct LanguageTag: View {
    let language: TimetableLanguage
    
    public init(language: TimetableLanguage) {
        self.language = language
    }
    
    public var body: some View {
        Text(language.displayLanguage)
            .font(Typography.labelMedium)
            .foregroundStyle(AssetColors.onSurfaceVariant.swiftUIColor)
            .padding(.horizontal, 6)
            .padding(.vertical, 2)
            .overlay(
                RoundedRectangle(cornerRadius: 6)
                    .stroke(AssetColors.outline.swiftUIColor, lineWidth: 1)
            )
    }
}

#Preview {
    LanguageTag(language: TimetableLanguage(langOfSpeaker: "en", isInterpretationTarget: false))
}
