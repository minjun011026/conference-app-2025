import SwiftUI
import Model

public struct OGPProfileShareImage: View {
    let profile: Model.Profile
    
    public var body: some View {
        ZStack(alignment: .topLeading) {
            backgroundImage(profile.cardVariant)
            FrontCard(userRole: profile.occupation, userName: profile.name, cardType: profile.cardVariant.type, image: profile.image, normal: (0, 0, 0.5))
                .offset(x: 280, y: 60)
            BackCard(cardType: profile.cardVariant.type, url: profile.url, normal: (0, 0, 0))
                .offset(x: 620, y: 190)
            Image(.ogpTopStar)
        }
        .frame(width: 1200, height: 630)
    }
    
    private func backgroundImage(_ cardVariant: ProfileCardVariant) -> some View {
        let resource: ImageResource
        switch cardVariant.type {
        case .day:
            resource = .nightOgpBase
        default:
            resource = .dayOgpBase
        }
        
        return Image(resource)
            .resizable()
    }
}

#Preview("OGPProfileShareImage(day)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 131, height: 131).background(Color.white)).uiImage!.pngData()!,
            cardVariant: .dayPill
        )
    )
}

#Preview("OGPProfileShareImage(night)", traits: .sizeThatFitsLayout) {
    OGPProfileShareImage(
        profile: Model.Profile(
            name: "Test Test",
            occupation: "Android App Engineer",
            url: URL(string: "https://2025.droidkaigi.jp/")!,
            image: ImageRenderer(content: Text("TEST Icon").frame(width: 131, height: 131).background(Color.white)).uiImage!.pngData()!,
            cardVariant: .nightPill
        )
    )
}
