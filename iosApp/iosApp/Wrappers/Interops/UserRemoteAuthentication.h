//
//  UserRemoteAuthentication.h
//  iosApp
//
//  Created by Jorge Torres on 31/07/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface UserRemoteAuthentication : NSObject

- (void)signInWithEmail:(NSString *)email
               password:(NSString *)password
             completion:(void (^)(NSDictionary * _Nullable userInfo, NSError * _Nullable error))completion;

- (void)signupWithEmail:(NSString *)email
               password:(NSString *)password
             completion:(void (^)(NSDictionary * _Nullable userInfo, NSError * _Nullable error))completion;

@end

NS_ASSUME_NONNULL_END
