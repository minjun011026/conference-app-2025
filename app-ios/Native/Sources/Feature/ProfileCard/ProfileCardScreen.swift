import Component
import SwiftUI
import Theme

public struct ProfileCardScreen: View {
    @State private var presenter = ProfileCardPresenter()
    @State private var cardType: ProfileCardType = .night

    public init() {}

    public var body: some View {
        NavigationStack {
            profileCardScrollView
                .background(AssetColors.surface.swiftUIColor)
                .navigationTitle("Profile Card")
                #if os(iOS)
                    .navigationBarTitleDisplayMode(.large)
                #endif
        }
    }

    private var profileCardScrollView: some View {
        ScrollView {
            Group {
                cardView
            }
            .padding(.bottom, 80)  // Tab bar padding
        }
    }
    
    private var editView: some View {
        // TODO:
        Text("Edit")
    }
    
    private var cardView: some View {
        VStack(spacing: 0) {
            profileCard
            actionButtons
        }
        .padding(.vertical, 20)
    }

    private var profileCard: some View {
        TiltFlipCard(
            front: { normal in
                FrontCard(
                    userRole: presenter.userRole,
                    userName: presenter.userName,
                    cardType: cardType,
                    normal: (normal.x, normal.y, normal.z),
                )
            },
            back: { normal in
                BackCard(
                    cardType: cardType,
                    normal: (normal.x, normal.y, normal.z),
                )
            }
        )
        .padding(.horizontal, 56)
        .padding(.vertical, 32)
    }

    private var actionButtons: some View {
        VStack(spacing: 8) {
            shareButton
            editButton
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 12)
    }

    private var shareButton: some View {
        Button {
            presenter.shareProfileCard()
        } label: {
            HStack {
                AssetImages.icShare.swiftUIImage
                    .resizable()
                    .frame(width: 18, height: 18)
                Text(String(localized: "Share", bundle: .module))

            }
            .frame(maxWidth: .infinity)
        }
        .filledButtonStyle()
    }

    private var editButton: some View {
        Button {
            presenter.editProfile()
            if cardType == .night {
                cardType = .day
            } else {
                cardType = .night
            }
        } label: {
            Text(String(localized: "Edit", bundle: .module))
                .frame(maxWidth: .infinity)
        }
        .textButtonStyle()
    }
}

#Preview {
    ProfileCardScreen()
}
