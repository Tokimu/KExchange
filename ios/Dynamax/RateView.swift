//
//  RateView.swift
//  Dynamax
//
//  Created by sebastian.zalewski on 2020/06/17.
//  Copyright © 2020 Dynamax. All rights reserved.
//

import SwiftUI
import DynamaxMobileIos

struct RateView: View {
  
  @EnvironmentObject private var viewModel: HomeViewModel
  @State var rate: CurrencyRate
  
  @State var rateValue: Double = 0.0
  
  var body: some View {
    let valueProxy = Binding<String>(
      get: { String(format: "%.02f", Double(self.rateValue)) },
      set: {
        if let value = NumberFormatter().number(from: $0) {
          self.viewModel.calculateExchange(value: value.doubleValue)
          self.rateValue = value.doubleValue
        }
      }
    )
    
    return HStack (alignment: .center) {
      Text(rate.name.emojiCode).font(.system(size: 50))
      
      VStack(alignment: .leading) {
        Text(rate.name.name).font(.system(size: 16))
        Text(rate.name.fullName)
          .font(.system(size: 14))
          .foregroundColor(Color.gray)
          .lineLimit(1)
      }
      
      TextField("0.0", text: valueProxy)
        .multilineTextAlignment(.trailing)
        .lineLimit(1)
        .keyboardType(.numberPad)
    }
    .padding(.leading)
    .padding(.trailing)
    .onTapGesture {
      self.viewModel.changeBase(newBase: self.rate)
    }
  }
}

struct RateView_Previews: PreviewProvider {
  static var previews: some View {
    RateView(rate: CurrencyRate(name: Currency.eur, value: 10.0, rate: 1.0), rateValue: 1.9)
  }
}
