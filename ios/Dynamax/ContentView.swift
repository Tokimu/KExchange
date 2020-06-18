import SwiftUI
import DynamaxMobileIos

struct ContentView: View {
  @EnvironmentObject private var viewModel: HomeViewModel
  var body: some View {
    return NavigationView {
      ScrollView {
        VStack(spacing: 10) {
          HStack { Spacer()}
          if(viewModel.state is HomeViewState.Loading) {
            VStack(alignment: .center) {
              Text("Loading").font(.system(size: 16))
            }
          }
          else if(viewModel.state is HomeViewState.Content) {
            ForEach((self.viewModel.state as! HomeViewState.Content).rates.indices, id: \.self) { i in
              RateView(rate: (self.viewModel.state as! HomeViewState.Content).rates[i], rateValue: (self.viewModel.state as! HomeViewState.Content).rates[i].value)
            }.id(UUID())
          } else {
            HStack { Spacer()}
          }
        }
      }.navigationBarTitle(Text("Rates"))
    }.onAppear {
      UITableView.appearance().separatorColor = .clear
      self.viewModel.onAppear()
    }.onDisappear {
      self.viewModel.onCleared()
    }
  }
}

struct ContentView_Previews: PreviewProvider {
  static var previews: some View {
    ContentView()
  }
}
