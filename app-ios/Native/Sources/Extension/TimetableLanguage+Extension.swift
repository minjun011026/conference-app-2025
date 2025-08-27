import Model

let languageCodes = ["JA", "EN"]

extension TimetableLanguage {
    public var displayLanguages: [String] {
        if langOfSpeaker.uppercased() == "MIXED" {
            return [langOfSpeaker]
        }

        var components: [String] = []

        let speakerLanguageCode = String(langOfSpeaker.uppercased().prefix(2))

        // Language of speaker
        components.append(speakerLanguageCode)

        // Add interpretation indicator if needed
        if isInterpretationTarget {
            components.append(languageCodes.first(where: { $0 != speakerLanguageCode })!)
        }

        return components
    }
}
