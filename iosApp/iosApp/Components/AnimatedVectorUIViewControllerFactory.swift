//
//  AnimatedVectorUIViewControllerFactory.swift
//  iosApp
//
//  Created by Jorge Torres on 06/11/25.
//
import UIKit

@objc(AnimatedVectorUIViewControllerFactory)
public class AnimatedVectorUIViewControllerFactory: NSObject {
    @objc public static func create(content: String) -> UIViewController {
        return AnimatedVectorUIViewController(content: content)
    }
}
