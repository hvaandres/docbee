//
//  AnimatedVectorUIViewController.swift
//  iosApp
//
//  Created by Jorge Torres on 06/11/25.
//
import UIKit
import SwiftUI

class AnimatedVectorUIViewController: UIViewController {
    private var content: String
    
    init(content: String) {
        self.content = content
        super.init(nibName: nil, bundle: nil)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        let hostingController = UIHostingController(rootView: AnimatedVectorUIView(content: content))
        
        addChild(hostingController)
        view.addSubview(hostingController.view)
        hostingController.view.frame = view.bounds
        hostingController.view.autoresizingMask = [.flexibleWidth, .flexibleHeight]
        hostingController.didMove(toParent: self)
    }
}
