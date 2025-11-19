//
//  AlertRemoteStorage.swift
//  iosApp
//
//  Created by Jorge Torres on 15/10/25.
//

import Foundation
import FirebaseFirestore
import ComposeApp

@objc(AlertRemoteStorage)
public class AlertRemoteStorage: NSObject {
    
    let db = Firestore.firestore()
    
    @objc public func saveAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        alert: NSDictionary,
        completion: @escaping (_ result: AlertModel?, _ error: NSError?) -> Void
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
            if (error as NSError? != nil) {
                completion(nil, error as NSError?)
            } else {
                do {
                    let alertDocument = try self.decode(dictionary: data, into: AlertDocument.self)
                    completion(alertDocument.toObject(), nil)
                } catch {
                    completion(nil, error as NSError?)
                }
            }
        }
    }
    
    @objc public func fetchAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        completion: @escaping (_ result: [AlertModel]?, _ error: NSError?) -> Void
    ) {
        Task {
            do {
                let querySnapshot = try await db.collection(collection)
                    .document(uid)
                    .collection(collectionAlert)
                    .getDocuments()
                
                var alertModel = [AlertModel]()
                for document in querySnapshot.documents {
                    do {
                        let swiftDocument = try document.data(as: AlertDocument.self)
                        alertModel.append(swiftDocument.toObject())
                    } catch let decodingError as NSError {
                        completion(nil, decodingError)
                    }
                }
                completion(alertModel, nil)
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
    
    @objc public func modifyAlert(
        collection: String,
        collectionAlert: String,
        uid: String,
        alert: NSDictionary,
        completion: @escaping (_ result: AlertModel?, _ error: NSError?) -> Void
    ) {
        Task {
            var data: [String: Any] = [:]
            for (key, value) in alert {
                if let keyString = key as? String {
                    data[keyString] = value
                }
            }
            
            let reference = db
                .collection(collection)
                .document(uid)
                .collection(collectionAlert)
                .document(data["uid"] as? String ?? "")
            
            do {
                try await reference.updateData([
                    "name": (data["name"] ?? "") as Any,
                    "message": (data["message"] ?? "") as Any
                ])
                let alertDocument = try self.decode(dictionary: data, into: AlertDocument.self)
                completion(alertDocument.toObject(), nil)
            } catch {
                completion(nil, error as NSError?)
            }
        }
    }
    
    func decode<T: Decodable>(dictionary: [String: Any], into type: T.Type) throws -> T {
        let jsonData = try JSONSerialization.data(withJSONObject: dictionary, options: [])
        let decoder = JSONDecoder()
        return try decoder.decode(T.self, from: jsonData)
    }
}

struct AlertDocument: Codable {
    let name: String
    let message: String
    let icon: String
    let uid: String
   
    enum CodingKeys: String, CodingKey {
        case name
        case message
        case icon
        case uid
    }
}

extension AlertDocument {
    func toObject() -> AlertModel {
        return AlertModel(
            uid: self.uid,
            name: self.name,
            message: self.message,
            icon: self.icon
        )
    }
}
