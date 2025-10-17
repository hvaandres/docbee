//
//  AlertRemoteStorage.h
//  iosApp
//
//  Created by Jorge Torres on 15/10/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface AlertRemoteStorage : NSObject

- (void)saveAlertWithCollection:(NSString *)collection
                collectionAlert:(NSString *)collectionAlert
                              uid:(NSString *)uid
                          alert:(NSDictionary *)alert
                       completion:(void (^)(NSDictionary * _Nullable result,  NSError * _Nullable error))completion;

- (void)fetchAlertWithCollection:(NSString *)collection
                 collectionAlert:(NSString *)collectionAlert
                               uid:(NSString *)uid
                        completion:(void (^)(NSArray<NSDictionary *> * _Nullable result, NSError * _Nullable error))completion;

- (void)deleteAlertWithCollection:(NSString *)collection
                  collectionAlert:(NSString *)collectionAlert
                               uid:(NSString *)uid
                         alertUid:(NSString *)alertUid
                         completion:(void (^)(BOOL success, NSError * _Nullable error))completion;

@end

NS_ASSUME_NONNULL_END
