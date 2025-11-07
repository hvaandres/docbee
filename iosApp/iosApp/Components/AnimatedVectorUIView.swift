//
//  AnimatedVectorUIView.swift
//  iosApp
//
//  Created by Jorge Torres on 06/11/25.
//
import DotLottie
import SwiftUI

struct AnimatedVectorUIView: View {
    var content: String
    
    var body: some View {
        DotLottieAnimation(
            animationData: content,
            config: AnimationConfig(autoplay: true, loop: true, backgroundColor: .black)
        ).view()
    }
}
