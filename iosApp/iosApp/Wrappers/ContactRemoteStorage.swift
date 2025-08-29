//
//  ContactRemoteStorage.swift
//  iosApp
//
//  Created by Jorge Torres on 27/08/25.
//

import Foundation
import FirebaseFirestore

@objc(ContactRemoteStorage)
public class ContactRemoteStorage: NSObject {
    
    let db = Firestore.firestore()
    
    @objc public func saveContact(
        collection: String,
        collectionContact: String,
        uid: String,
        contact: NSDictionary,
        completion: @escaping (NSError?) -> Void
    ) {
        let reference = db
            .collection(collection)
            .document(uid)
            .collection(collectionContact)
            .document()
        
        var data: [String: Any] = [:]
        for (key, value) in contact {
            if let keyString = key as? String {
                data[keyString] = value
            }
        }
        data["uid"] = reference.documentID
        
        reference.setData(data) { error in
            completion(error as NSError?)
        }
    }
    
    
    @objc public func fetchContact(
        collection: String,
        collectionContact: String,
        uid: String,
        completion: @escaping (_ result: [[String: Any]]?, _ error: NSError?) -> Void
    ) {
        Task {
            do {
                let querySnapshot = try await db.collection(collection)
                    .document(uid)
                    .collection(collectionContact)
                    .getDocuments()
                
                completion(querySnapshot.documents.map { $0.data() }, nil)
            } catch {
                completion(nil, error as NSError?)
            }
        }
    }
}
