//
//  UserRemoteAuthentication.swift
//  iosApp
//
//  Created by Jorge Torres on 31/07/25.
//

import Foundation
import FirebaseAuth

@objc(UserRemoteAuthentication)
public class UserRemoteAuthentication: NSObject {
    
    @objc public func signIn(withEmail: String, password: String, completion: @escaping (NSDictionary?, NSError?) -> Void) {
        Auth.auth().signIn(withEmail: withEmail, password: password) { [weak self] authResult, error in
            guard self != nil else { return }
            if let user = authResult?.user {
                let userInfo: [String: Any] = [
                    "uid": user.uid,
                    "email": user.email ?? ""
                ]
                completion(userInfo as NSDictionary, nil)
            } else {
                completion(nil, error as NSError?)
            }
        }
    }
    
    @objc public func signIn(withIdToken: String, completion: @escaping (NSDictionary?, NSError?) -> Void) {
        let credential = GoogleAuthProvider.credential(withIDToken: withIdToken, accessToken: "")
        Auth.auth().signIn(with: credential) { [weak self] authResult, error in
            guard self != nil else { return }
            if let user = authResult?.user {
                let userInfo: [String: Any] = [
                    "uid": user.uid,
                    "isNewUser": authResult?.additionalUserInfo?.isNewUser ?? false,
                ]
                completion(userInfo as NSDictionary, nil)
            } else {
                completion(nil, error as NSError?)
            }
        }
    }
    
    @objc public func signup(withEmail: String, password: String, completion: @escaping (NSDictionary?, NSError?) -> Void) {
        Auth.auth().createUser(withEmail: withEmail, password: password) { [weak self] authResult, error in
            guard self != nil else { return }
            if let user = authResult?.user {
                let userInfo: [String: Any] = [
                    "uid": user.uid,
                    "email": user.email ?? ""
                ]
                completion(userInfo as NSDictionary, nil)
            } else {
                completion(nil, error as NSError?)
            }
        }
    }
    
    @objc public func logout(completion: @escaping (NSError?) -> Void) {
        do {
            try Auth.auth().signOut()
            completion(nil)
        } catch let signOutError as NSError {
            completion(signOutError)
        }
    }
    
}
