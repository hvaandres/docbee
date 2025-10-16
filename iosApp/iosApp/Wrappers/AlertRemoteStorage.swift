//
//  AlertRemoteStorage.swift
//  iosApp
//
//  Created by Jorge Torres on 15/10/25.
//

import Foundation
import FirebaseFirestore

@objc(AlertRemoteStorage)
public class AlertRemoteStorage: NSObject {
    
    let db = Firestore.firestore()
    
    @objc public func saveAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        alert: NSDictionary,
        completion: @escaping (NSError?) -> Void
    ) {
        let reference = db
            .collection(collection)
            .document(uid)
            .collection(collectionAlert)
            .document()
        
        var data: [String: Any] = [:]
        for (key, value) in alert {
            if let keyString = key as? String {
                data[keyString] = value
            }
        }
        data["uid"] = reference.documentID
        
        reference.setData(data) { error in
            completion(error as NSError?)
        }
    }
    
    @objc public func fetchAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        completion: @escaping (_ result: [[String: Any]]?, _ error: NSError?) -> Void
    ) {
        Task {
            do {
                let querySnapshot = try await db.collection(collection)
                    .document(uid)
                    .collection(collectionAlert)
                    .getDocuments()
                
                completion(querySnapshot.documents.map { $0.data() }, nil)
            } catch {
                completion(nil, error as NSError?)
            }
        }
    }
    
    @objc public func deleteAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        alertUid: String,
        completion: @escaping (Bool, NSError?) -> Void
    ) {
        Task {
            do {
              try await db.collection(collection)
                    .document(uid)
                    .collection(collectionAlert)
                    .document(alertUid)
                    .delete()
                completion(true, nil)
            } catch {
                completion(false, error as NSError?)
            }
        }
    }
    
}
