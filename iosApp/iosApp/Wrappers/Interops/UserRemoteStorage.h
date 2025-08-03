//
//  UserRemoteStorage.h
//  iosApp
//
//  Created by Jorge Torres on 03/08/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface UserRemoteStorage : NSObject

- (void)saveUserWithCollection:(NSString *)collection
                       profile:(NSDictionary *)profile
                    completion:(void (^)(NSError * _Nullable error))completion;

@end

NS_ASSUME_NONNULL_END
