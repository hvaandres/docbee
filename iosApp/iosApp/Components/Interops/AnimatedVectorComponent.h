//
//  AnimatedVectorComponent.h
//  iosApp
//
//  Created by Jorge Torres on 06/11/25.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface AnimatedVectorUIViewControllerFactory : NSObject
+ (UIViewController *)createWithContent:(NSString *)content
    __attribute__((swift_name("create(content:)")));
+ (void)updateWithController:(UIViewController *)controller content:(NSString *)content
    __attribute__((swift_name("update(controller:content:)")));
@end
NS_ASSUME_NONNULL_END
