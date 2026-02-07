//
//  GoogleAuthProvider.h
//  iosApp
//
//  Created by Jorge Torres on 04/02/26.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface GoogleAuthRemoteProvider : NSObject
- (void)getGoogleAuthDataWithCompletion:(void (^)(NSObject * _Nullable, NSError * _Nullable))completion;
@end

NS_ASSUME_NONNULL_END
