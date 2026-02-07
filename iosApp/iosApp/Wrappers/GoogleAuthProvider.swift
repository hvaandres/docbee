//
//  GoogleAuthProvider.swift
//  iosApp
//
//  Created by Jorge Torres on 04/02/26.
//

import Foundation
import UIKit
import GoogleSignIn
import ComposeApp


@objc(GoogleAuthRemoteProvider)
class GoogleAuthRemoteProvider: NSObject {
    
    @objc func getGoogleAuthData(completion: @escaping (_ result: AuthenticationModel?, _ error: Error?) -> Void) {
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
    
    private func getGoogleIdToken() async throws -> AuthenticationModel {
        guard let topViewController = await activeViewController else {
            throw NSError(domain: "UI not configured", code: 0, userInfo: nil)
        }

        return try await withCheckedThrowingContinuation { continuation in
            GIDSignIn.sharedInstance.signIn(withPresenting: topViewController) { result, error in
                if let error = error {
                    continuation.resume(throwing: error)
                    return
                }
                let user = result?.user
                let profile = user?.profile
                let userModel = AuthenticationModel(
                    idToken: user?.idToken?.tokenString ?? "",
                    name: profile?.givenName ?? "",
                    lastname: profile?.familyName ?? "",
                    email: profile?.email ?? "",
                    phoneNumber: ""
                )
                continuation.resume(returning: userModel)
            }
        }
    }
}
