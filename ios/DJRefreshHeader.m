//
//  DJRefreshDefaultHeader.m
//  DjrefreshLibraryExample
//
//  Created by Hay Huang on 2023/8/25.
//

#import "DJRefreshHeader.h"

@implementation DJRefreshHeader
@synthesize onRefresh;

-(void)prepare{
    [super prepare];
    self.autoresizingMask = UIViewAutoresizingNone;
}

- (void)setState:(MJRefreshState)state {
    [super setState:state];
    if (self.onChangeState) {
        if (state == MJRefreshStateIdle && (_preState == MJRefreshStateRefreshing || _preState == MJRefreshStateWillRefresh)) {
            self.onChangeState(@{@"state":@(4)}); // 结束刷新
        } else if (state == MJRefreshStateWillRefresh){
            self.onChangeState(@{@"state":@(3)}); // 正在刷新
          if(self.onRefresh){
            self.onRefresh(@{});
          }
        } else {
            self.onChangeState(@{@"state":@(state)});
        }
    }
    _preState = state;
}

- (void)setRefreshing:(BOOL)refreshing {
    if (refreshing && self.state == MJRefreshStateIdle) {
        MJRefreshDispatchAsyncOnMainQueue({
            [self beginRefreshing];
        })
    } else if (!refreshing && (self.state == MJRefreshStateRefreshing || self.state == MJRefreshStateWillRefresh)) {
        __weak typeof(self) weakSelf = self;
        [self endRefreshingWithCompletionBlock:^{
            typeof(weakSelf) self = weakSelf;
            if (self.onChangeState) {
                self.onChangeState(@{@"state":@(MJRefreshStateIdle)});
            }
        }];
    }
}

@end
