//
//  ContactRemoteStorage.swift
//  iosApp
//
//  Created by Jorge Torres on 27/08/25.
//

import Foundation
import FirebaseFirestore
import ComposeApp

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
        completion: @escaping (_ result: [ContactModel]?, _ error: NSError?) -> Void
    ) {
        Task {
            do {
                let querySnapshot = try await db.collection(collection)
                    .document(uid)
                    .collection(collectionContact)
                    .getDocuments()
                var contactModel = [ContactModel]()
                for document in querySnapshot.documents {
                    do {
                        let swiftDocument = try document.data(as: ContactDocument.self)
                        contactModel.append(swiftDocument.toObject())
                    } catch let decodingError as NSError {
                        completion(nil, decodingError)
                    }
                }
                
                completion(contactModel, nil)
            } catch {
                completion(nil, error as NSError?)
            }
        }
    }
    
    @objc public func deleteContact(
        collection: String,
        collectionContact: String,
        uid: String,
        contactUid: String,
        completion: @escaping (Bool, NSError?) -> Void
    ) {
        Task {
            do {
              try await db.collection(collection)
                    .document(uid)
                    .collection(collectionContact)
                    .document(contactUid)
                    .delete()
                completion(true, nil)
            } catch {
                completion(false, error as NSError?)
            }
        }
    }
}

struct ContactDocument: Codable {
    let firstName: String
    let lastName: String
    let email: String
    let phoneNumber: String
    let dateOfBirth: String
    let address: String
    let gender: String
    let uid: String
    
    enum CodingKeys: String, CodingKey {
        case firstName
        case lastName
        case email
        case phoneNumber
        case dateOfBirth
        case address
        case gender
        case uid
    }
}

extension ContactDocument {
    func toObject() -> ContactModel {
        return ContactModel(
            uid: self.uid,
            firstName: self.firstName,
            lastName: self.lastName,
            email: self.email,
            phoneNumber: self.phoneNumber,
            dateOfBirth: self.dateOfBirth,
            address: self.address,
            gender: self.gender
        )
    }
}
