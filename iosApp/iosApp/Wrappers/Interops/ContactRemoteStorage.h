//
//  ContactRemoteStorage.h
//  iosApp
//
//  Created by Jorge Torres on 27/08/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface ContactRemoteStorage : NSObject

- (void)saveContactWithCollection:(NSString *)collection
                collectionContact:(NSString *)collectionContact
                              uid:(NSString *)uid
                          contact:(NSDictionary *)contact
                       completion:(void (^)(NSError * _Nullable error))completion;

- (void)fetchContactWithCollection:(NSString *)collection
                 collectionContact:(NSString *)collectionContact
                               uid:(NSString *)uid
                        completion:(void (^)(NSArray<NSDictionary *> * _Nullable result, NSError * _Nullable error))completion;

@end

NS_ASSUME_NONNULL_END
