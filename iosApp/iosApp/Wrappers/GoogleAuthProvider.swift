//
//  GoogleAuthProvider.swift
//  iosApp
//
//  Created by Jorge Torres on 04/02/26.
//

import Foundation
import UIKit
import GoogleSignIn


@objc(GoogleAuthRemoteProvider)
class GoogleAuthRemoteProvider: NSObject {
    
    @objc func getGoogleIdToken(completion: @escaping (String?, Error?) -> Void) {
        Task {
            do {
                let token = try await getGoogleIdToken()
                completion(token, nil)
            } catch {
                completion(nil, error)
            }
        }
    }
    
    @MainActor
    private var activeViewController: UIViewController? {
        let scene = UIApplication.shared.connectedScenes
            .first { $0.activationState == .foregroundActive } as? UIWindowScene
        return scene?.windows.first { $0.isKeyWindow }?.rootViewController
    }
    
    private func getGoogleIdToken() async throws -> String {
        guard let topViewController = await activeViewController else {
            return ""
        }

        return try await withCheckedThrowingContinuation { continuation in
            GIDSignIn.sharedInstance.signIn(withPresenting: topViewController) { result, error in
                if let error = error {
                    continuation.resume(throwing: error)
                    return
                }
                
                let idToken = result?.user.idToken?.tokenString ?? ""
                continuation.resume(returning: idToken)
            }
        }
    }
}
