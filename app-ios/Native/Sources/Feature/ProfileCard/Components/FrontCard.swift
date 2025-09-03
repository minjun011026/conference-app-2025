import SwiftUI
import Theme

struct FrontCard: View {
    let userRole: String
    let userName: String
    let cardType: ProfileCardType
    let image: Data
    let normal: (Float, Float, Float)
    let effectEnabled: Bool

    init(
        userRole: String, userName: String, cardType: ProfileCardType, image: Data,
        normal: (Float, Float, Float) = (0, 0, 0), effectEnabled: Bool = true
    ) {
        self.userRole = userRole
        self.userName = userName
        self.cardType = cardType
        self.image = image
        self.normal = normal
        self.effectEnabled = effectEnabled
    }

    let shaderFunction = ShaderFunction(library: .bundle(.module), name: "kiraEffect")

    var body: some View {
        let lightContentColor = Color(
            uiColor: UIColor(red: 52.0 / 255, green: 39.0 / 255, blue: 94.0 / 255, alpha: 1.0))
        ZStack(alignment: .top) {
            Image("\(cardType.rawValue)_background", bundle: .module)
                .resizable()
                .scaledToFill()
                .kiraEffect(
                    function: shaderFunction,
                    normal: normal,
                    monochromeImage: Image("front_effect", bundle: .module),
                    isEnabled: effectEnabled
                )
            VStack(alignment: .center, spacing: 20) {
                Image("\(cardType.rawValue)_card_title", bundle: .module)
                VStack(alignment: .center, spacing: 12) {
                    avatarImage
                    VStack(alignment: .center, spacing: 0) {
                        Text(userRole)
                            .foregroundStyle(
                                cardType == .night ? AssetColors.onSurface.swiftUIColor : lightContentColor
                            )
                            .typographyStyle(.bodyMedium)
                        Text(userName)
                            .foregroundStyle(cardType == .night ? .white : lightContentColor)
                            .typographyStyle(
                                .init(
                                    font: .custom(AssetFonts.Chango.regular, size: 24), lineHeight: 32, letterSpacing: 0
                                )
                            )
                            .multilineTextAlignment(.center)
                            .frame(maxWidth: 252)
                    }
                }
                Spacer()
            }
            .padding(.horizontal, 30)
            .padding(.vertical, 40)
            Image("\(cardType.rawValue)_front_wave", bundle: .module)
                .resizable()
                .frame(width: 300, height: 380)
        }
        .frame(width: 300, height: 380)
        .cornerRadius(12)
        // Figma shadow: may be replaced by Metal shader
        .shadow(color: .black.opacity(0.12), radius: 10, x: 3, y: 3)
        .shadow(color: .black.opacity(0.11), radius: 17, x: 11, y: 14)
        .shadow(color: .black.opacity(0.06), radius: 24, x: 24, y: 31)
        .shadow(color: .black.opacity(0.02), radius: 28, x: 42, y: 55)
        .shadow(color: .black.opacity(0.00), radius: 31, x: 66, y: 87)
        .overlay {
            RoundedRectangle(cornerRadius: 12)
                .fill(.black.shadow(.inner(color: .white.opacity(0.32), radius: 2, x: -2, y: -2)))
                .rotationEffect(.degrees(180))
                .blendMode(.lighten)
        }
        .clipped(antialiased: true)
    }

    private var avatarImage: some View {
        Image(uiImage: UIImage(data: image)!)
            .resizable()
            .frame(width: 131, height: 131)
            .foregroundColor(.accentColor)
    }
}
