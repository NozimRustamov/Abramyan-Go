import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
    
    init() {
        // Initialize Koin
        KoinHelperKt.doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea(.all)
        }
    }
}
