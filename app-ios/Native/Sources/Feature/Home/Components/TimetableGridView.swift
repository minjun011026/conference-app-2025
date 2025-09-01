import Component
import Model
import Presentation
import SwiftUI
import Theme

struct TimetableGridView: View {
    @Binding var selectedDay: DayTab
    let timetableItems: [TimetableTimeGroupItems]
    let rooms: [Room]
    let onItemTap: (TimetableItemWithFavorite) -> Void

    @State private var now: Date = Date()
    @State private var timer: Timer? = nil

    // MARK: - Layout Const
    private let heightOfMinute: CGFloat = 5
    private let roomWidth: CGFloat = 192
    private let roomSpacing: CGFloat = 4
    private let timeAxisWidth: CGFloat = 50

    var body: some View {
        ScrollView(.vertical) {
            VStack(spacing: 0) {
                DayTabBar(selectedDay: $selectedDay)
                    .padding(.horizontal)

                timetableGridView

                // Bottom padding for tab bar
                Color.clear.padding(.bottom, 60)
            }
        }
        .onAppear {
            timer = Timer.scheduledTimer(withTimeInterval: 1, repeats: true) { _ in
                Task { @MainActor in
                    self.now = Date()
                }
            }
        }
        .onDisappear {
            timer?.invalidate()
        }
    }

    private var timetableGridView: some View {
        HStack(alignment: .top, spacing: 0) {
            timeAxis
                .frame(width: timeAxisWidth, height: timetableHeight, alignment: .topTrailing)
                .padding(.trailing, 4)

            ScrollView(.horizontal) {
                VStack(spacing: 0) {
                    roomHeaderRow

                    ZStack(alignment: .topLeading) {
                        roomLines
                        hourLines
                        sessionCards
                        currentTimeLine
                    }
                    .frame(width: timetableWidth, height: timetableHeight, alignment: .topLeading)
                }
            }

            .padding(.trailing)
        }
    }

    private var roomHeaderRow: some View {
        HStack(spacing: roomSpacing) {
            Color.clear
                .gridCellUnsizedAxes([.horizontal, .vertical])

            ForEach(rooms, id: \.id) { room in
                Text(room.displayName)
                    .font(Typography.titleMedium)
                    .foregroundStyle(room.roomTheme.primaryColor)
                    .frame(width: roomWidth)
                    .padding(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 0))
            }
        }
    }

    private var timeAxis: some View {
        VStack(spacing: 0) {
            // Place transparent text to take the room header space.
            Text("Room Name")
                .font(Typography.titleMedium)
                .foregroundStyle(Color.clear)
                .padding(EdgeInsets(top: 8, leading: 0, bottom: 8, trailing: 0))

            if let dayStart = dayStart, let dayEnd = dayEnd {
                let totalMinutes = Int(dayEnd.timeIntervalSince(dayStart) / 60)
                ForEach(0...totalMinutes / 60, id: \.self) { hourIndex in
                    let labelDate = Calendar.current.date(byAdding: .minute, value: hourIndex * 60, to: dayStart)!
                    Text(labelDate.formatted(date: .omitted, time: .shortened))
                        .font(.caption2)
                        .frame(height: heightOfMinute * 60, alignment: .top)
                }
            }
        }
    }

    @ViewBuilder
    private var roomLines: some View {
        ForEach(0...rooms.count, id: \.self) { index in
            let x = CGFloat(index) * (roomWidth + roomSpacing) - (roomSpacing / 2)
            Rectangle()
                .fill(Color.gray.opacity(0.3))
                .frame(width: 1, height: timetableHeight)
                .offset(x: x)
        }
    }

    @ViewBuilder
    private var hourLines: some View {
        if let dayStart = dayStart, let dayEnd = dayEnd {
            let totalMinutes = Int(dayEnd.timeIntervalSince(dayStart) / 60)
            let hourNum = totalMinutes / 60
            ForEach(0...hourNum, id: \.self) { hourIndex in
                Rectangle()
                    .fill(Color.gray.opacity(0.3))
                    .frame(height: 1)
                    .frame(maxWidth: .infinity)
                    .offset(y: CGFloat(hourIndex * 60) * heightOfMinute)
            }
        }
    }

    @ViewBuilder
    private var currentTimeLine: some View {
        if let dayStart = dayStart,
            let dayEnd = dayEnd
        {
            if now >= dayStart && now <= dayEnd {
                Rectangle()
                    .fill(Color.red)
                    .frame(height: 2)
                    .frame(maxWidth: .infinity)
                    .offset(y: offsetY(for: now, base: dayStart))
            }
        }
    }

    @ViewBuilder
    private var sessionCards: some View {
        if let dayStart = dayStart {
            ForEach(timelineItems, id: \.id) { item in
                let left = offsetX(for: item.timetableItem)
                let top = offsetY(for: item.timetableItem.startsAt, base: dayStart)
                let width = sessionWidth(for: item.timetableItem)
                let height = sessionHeight(for: item.timetableItem)

                TimetableGridCard(
                    timetableItem: item.timetableItem,
                    onTap: { _ in onItemTap(item) }
                )
                .frame(width: width, height: height)
                .offset(x: left, y: top)
            }
        }
    }

    // MARK: - Helpers
    private var timelineItems: [TimetableItemWithFavorite] {
        timetableItems.flatMap(\.items)
    }

    private var dayStart: Date? {
        guard let earliest = timelineItems.map({ $0.timetableItem.startsAt }).min() else { return nil }
        // Round to hour unit
        return Calendar.current.date(
            bySettingHour: Calendar.current.component(.hour, from: earliest),
            minute: 0,
            second: 0,
            of: earliest
        )
    }

    private var dayEnd: Date? {
        timelineItems.map({ $0.timetableItem.endsAt }).max()
    }

    private var timetableWidth: CGFloat {
        guard !rooms.isEmpty else { return 0 }
        let columns = CGFloat(rooms.count)
        return columns * roomWidth + (columns - 1) * roomSpacing
    }

    private var timetableHeight: CGFloat {
        if let dayStart = dayStart, let dayEnd = dayEnd {
            return CGFloat(dayEnd.timeIntervalSince(dayStart) / 60) * heightOfMinute
        }
        return 0
    }

    private func offsetX(for item: any TimetableItem) -> CGFloat {
        if (item.isLunchTime()) {
            return 0
        } else {
            guard let idx = rooms.firstIndex(where: { $0.id == item.room.id }) else { return 0 }
            return CGFloat(idx) * (roomWidth + roomSpacing)
        }
    }

    private func offsetY(for date: Date, base: Date) -> CGFloat {
        let seconds = date.timeIntervalSince(base)
        return CGFloat(seconds / 60.0) * heightOfMinute
    }
    
    private func sessionWidth(for item: any TimetableItem) -> CGFloat {
        if (item.isLunchTime()) {
            return timetableWidth
        } else {
            return roomWidth
        }
    }

    private func sessionHeight(for item: any TimetableItem) -> CGFloat {
        let seconds = item.endsAt.timeIntervalSince(item.startsAt)
        return CGFloat(seconds / 60.0) * heightOfMinute
    }
}
