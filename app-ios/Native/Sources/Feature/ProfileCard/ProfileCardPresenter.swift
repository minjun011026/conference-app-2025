import Foundation
import Model
import Observation
import Presentation
import _PhotosUI_SwiftUI

class FormState {
    var name: String
    var occupation: String
    var urlString: String
    var image: PhotosPickerItem?
    var cardVariants: ProfileCardVariants

    var nameError: String?
    var occupationError: String?
    var urlError: String?
    var imageError: String?

    init(
        name: String, occupation: String, urlString: String, image: PhotosPickerItem?, cardVariants: ProfileCardVariants
    ) {
        self.name = name
        self.occupation = occupation
        self.urlString = urlString
        self.image = image
        self.cardVariants = cardVariants
    }

    func validate() -> Bool {
        nameError = name.isEmpty ? "Name is required" : nil
        occupationError = occupation.isEmpty ? "Occupation is required" : nil
        urlError = urlString.isEmpty ? "URL is required" : nil
        imageError = image == nil ? "Image is required" : nil

        return nameError == nil && occupationError == nil && urlError == nil && imageError == nil
    }

    @MainActor
    func createProfile() async throws -> Profile {
        let imageData = try await image?.loadTransferable(type: Data.self)
        guard let imageData else {
            fatalError("Failed to load image data")
        }

        return Profile(
            name: name,
            occupation: occupation,
            url: URL(string: urlString)!,
            image: imageData,
            cardVariants: cardVariants
        )
    }
}

@MainActor
@Observable
final class ProfileCardPresenter {
    let profile = ProfileProvider()

    var formState: FormState
    var isEditing: Bool

    var shouldEditing: Bool {
        isEditing || profile.profile == nil
    }

    init() {
        formState = FormState(name: "", occupation: "", urlString: "", image: nil, cardVariants: .nightPill)
        isEditing = false
    }

    func loadInitial() {
        profile.fetchProfile()
    }

    func shareProfileCard() {
        // print("Share profile card tapped")
        // TODO: Implement sharing functionality
    }

    func editProfile() {
        formState = FormState(
            name: profile.profile?.name ?? "",
            occupation: profile.profile?.occupation ?? "",
            urlString: profile.profile?.url.absoluteString ?? "",
            // TODO: enable to set loaded image
            image: nil,
            cardVariants: profile.profile?.cardVariants ?? .nightPill
        )
        isEditing = true
    }

    @MainActor
    func createCard() {
        if !formState.validate() { return }

        Task {
            let profileData = try await formState.createProfile()
            profile.saveProfile(profileData)
            profile.fetchProfile()
            self.isEditing = false
        }
    }

    func setName(_ name: String) {
        formState.name = name
    }

    func setOccupation(_ occupation: String) {
        formState.occupation = occupation
    }

    func setLink(_ linkString: String) {
        formState.urlString = linkString
    }

    func setImage(_ image: PhotosPickerItem?) {
        formState.image = image
    }
}
