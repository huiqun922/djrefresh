//
//  DJRefreshDefaultHeader.m
//  react-native-djrefresh-library
//
//  Created by Hay Huang on 2023/8/25.
//

#import <React/RCTViewManager.h>
#import "DJRefreshHeader.h"

@interface DJRefreshHeaderManager : RCTViewManager
@end

@implementation DJRefreshHeaderManager

RCT_EXPORT_MODULE(DJRefreshHeader)

- (UIView *)view
{
  return [[DJRefreshHeader alloc] init];
}

RCT_EXPORT_VIEW_PROPERTY(refreshing, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onRefresh, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeState, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onChangeOffset, RCTDirectEventBlock)


@end
