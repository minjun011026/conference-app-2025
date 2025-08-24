import Foundation
import Model
import UseCase

// TODO: Replace KMP connection
struct ProfileUseCaseImpl {
    private let nameKey = "name"
    private let occupationKey = "occupation"
    private let urlKey = "url"
    private let imageKey = "image"
    private let cardVariantsKey = "cardVariants"

    func fetch() async -> Model.Profile? {
        let userDefaults = UserDefaults.standard
        guard let name = userDefaults.string(forKey: nameKey),
            let occupation = userDefaults.string(forKey: occupationKey),
            let urlString = userDefaults.string(forKey: urlKey),
            let url = URL(string: urlString),
            let imageData = userDefaults.data(forKey: imageKey),
            let cardVariantsString = userDefaults.string(forKey: cardVariantsKey),
            let cardVariants = Model.ProfileCardVariants(rawValue: cardVariantsString)
        else {
            return nil
        }
        return Profile(
            name: name,
            occupation: occupation,
            url: url,
            image: imageData,
            cardVariants: cardVariants,
        )
    }

    func save(_ profile: Model.Profile) async {
        let userDefaults = UserDefaults.standard
        userDefaults.set(profile.name, forKey: nameKey)
        userDefaults.set(profile.occupation, forKey: occupationKey)
        userDefaults.set(profile.url.absoluteString, forKey: urlKey)
        userDefaults.set(profile.image, forKey: imageKey)
        userDefaults.set(profile.cardVariants.rawValue, forKey: cardVariantsKey)
    }
}
