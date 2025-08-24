import Dependencies
import Foundation
import Model
import Observation

@Observable
public final class ProfileProvider {
    @ObservationIgnored
    @Dependency(\.profileUseCase) private var profileUseCase

    public var profile: Profile?

    public init() {}

    @MainActor
    public func fetchProfile() {
        Task {
            profile = await profileUseCase.fetch()
        }
    }

    @MainActor
    public func saveProfile(_ profile: Profile) {
        Task {
            await profileUseCase.save(profile)
        }
    }
}
