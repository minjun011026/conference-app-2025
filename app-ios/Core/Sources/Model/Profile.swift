import Foundation

public enum ProfileCardVariants: String, Sendable {
    case nightPill
    case nightDiamond
    case nightFlower
    case dayPill
    case dayDiamond
    case dayFlower
}

public struct Profile: Sendable, Equatable {
    public let name: String
    public let occupation: String
    public let url: URL
    public let image: Data
    public let cardVariants: ProfileCardVariants

    public init(
        name: String,
        occupation: String,
        url: URL,
        image: Data,
        cardVariants: ProfileCardVariants
    ) {
        self.name = name
        self.occupation = occupation
        self.url = url
        self.image = image
        self.cardVariants = cardVariants
    }
}
