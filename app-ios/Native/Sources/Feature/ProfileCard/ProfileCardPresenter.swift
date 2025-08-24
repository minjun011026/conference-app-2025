import Foundation
import Model
import Observation
import Presentation

@MainActor
@Observable
final class ProfileCardPresenter {
    let profile = ProfileProvider()
    var editingProfile: Profile?

    var isEditing: Bool {
        editingProfile != nil || profile.profile == nil
    }

    init() {}

    func loadInitial() {
        profile.fetchProfile()
    }

    func shareProfileCard() {
        // print("Share profile card tapped")
        // TODO: Implement sharing functionality
    }

    func editProfile() {
        editingProfile = profile.profile
    }
}
