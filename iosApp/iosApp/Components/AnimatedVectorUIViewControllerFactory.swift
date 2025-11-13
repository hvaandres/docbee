//
//  AnimatedVectorUIViewControllerFactory.swift
//  iosApp
//
//  Created by Jorge Torres on 06/11/25.
//
import UIKit
import SwiftUI

@objc(AnimatedVectorUIViewControllerFactory)
public class AnimatedVectorUIViewControllerFactory: NSObject {
    @objc public static func create(content: String) -> UIViewController {
        let hostingController = UIHostingController(rootView: AnimatedVectorUIView(content: content))
        hostingController.view.backgroundColor = .clear
        return hostingController
    }
    
    @objc public static func update(controller: UIViewController, content: String) {
        if let hostingController = controller as? UIHostingController<AnimatedVectorUIView> {
            hostingController.rootView = AnimatedVectorUIView(content: content)
        }
    }
}
