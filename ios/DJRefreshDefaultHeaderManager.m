//
//  DJRefreshDefaultHeader.m
//  react-native-djrefresh-library
//
//  Created by Hay Huang on 2023/8/25.
//

#import <React/RCTViewManager.h>
#import "DJRefreshDefaultHeader.h"

@interface DJRefreshDefaultHeaderManager : RCTViewManager
@end

@implementation DJRefreshDefaultHeaderManager

RCT_EXPORT_MODULE(DJRefreshDefaultHeader)

- (UIView *)view
{
  return [[DJRefreshDefaultHeader alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(refreshing, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onRefresh, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeState, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeOffset, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(locale, NSString)


@end
