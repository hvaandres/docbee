//
//  UserRemoteStorage.swift
//  iosApp
//
//  Created by Jorge Torres on 30/07/25.
//

import Foundation
import FirebaseFirestore

@objc(UserRemoteStorage)
public class UserRemoteStorage: NSObject {
    
    @objc public func saveUser(collection: String, profile: NSDictionary, completion: @escaping (NSError?) -> Void) {
        let db = Firestore.firestore()
        
        var data: [String: Any] = [:]
        for (key, value) in profile {
            if let keyString = key as? String {
                data[keyString] = value
            }
        }
        
        db.collection(collection)
            .document(data["uid"] as? String ?? "")
            .setData(data) { error in
                completion(error as NSError?)
            }
    }
}
