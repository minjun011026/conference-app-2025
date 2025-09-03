import SwiftUI
import Theme

struct BackCard: View {
    let cardType: ProfileCardType
    let url: URL
    let normal: (Float, Float, Float)
    let effectEnabled: Bool

    init(cardType: ProfileCardType, url: URL, normal: (Float, Float, Float) = (0, 0, 0), effectEnabled: Bool = true) {
        self.cardType = cardType
        self.url = url
        self.normal = normal
        self.effectEnabled = effectEnabled
    }

    let shaderFunction = ShaderFunction(library: .bundle(.module), name: "kiraEffect")

    var body: some View {
        ZStack {
            if cardType == .night {
                Color(uiColor: UIColor(red: 40.0 / 255.0, green: 15.0 / 255.0, blue: 131.0 / 255.0, alpha: 1.0))
            } else {
                Color(uiColor: UIColor(red: 242.0 / 255.0, green: 244.0 / 255.0, blue: 251.0 / 255.0, alpha: 1.0))
            }
            Image("\(cardType.rawValue)_base_sticker", bundle: .module)
                .resizable()
                .scaledToFill()
            VStack(alignment: .center, spacing: 20) {
                Image("\(cardType.rawValue)_card_title", bundle: .module)
                QrCodeView(data: url.absoluteString)
                    .frame(width: 160, height: 160)
                Spacer()
            }
            .padding(.horizontal, 30)
            .padding(.vertical, 40)
            Image("\(cardType.rawValue)_top_sticker", bundle: .module)
                .resizable()
                .scaledToFill()
        }
        .frame(width: 300, height: 380)
        .cornerRadius(12)
        .kiraEffect(
            function: shaderFunction,
            normal: normal,
            monochromeImage: Image("back_effect", bundle: .module),
            isEnabled: effectEnabled
        )
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
}
