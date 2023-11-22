//
//  DJRefreshDefaultHeader.m
//  DjrefreshLibraryExample
//
//  Created by Hay Huang on 2023/8/25.
//

#import "DJRefreshDefaultHeader.h"

@implementation DJRefreshDefaultHeader
@synthesize onRefresh;


-(void)prepare{
    [super prepare];
    self.stateLabel.hidden = YES;
    self.lastUpdatedTimeLabel.hidden = YES;
    self.loadingView.color = [UIColor colorWithRed:0x20/255.f green:0x22/255.f blue:0x23/255.f alpha:1];
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

- (void)setLocale:(NSString *)locale{
    _locale = locale;
    [self textConfiguration];
}

- (void)textConfiguration {
    // 初始化文字
    if([self.locale containsString:@"en"]){
        [self setTitle:@"Pull to refresh" forState:MJRefreshStateIdle];
        [self setTitle:@"Release to refresh" forState:MJRefreshStatePulling];
        [self setTitle:@"Refreshing..." forState:MJRefreshStateRefreshing];
    }else{
        [self setTitle:@"下拉可以刷新" forState:MJRefreshStateIdle];
        [self setTitle:@"松开立即刷新" forState:MJRefreshStatePulling];
        [self setTitle:@"正在刷新..." forState:MJRefreshStateRefreshing];
    }
    [self setLastUpdatedTimeKey:MJRefreshHeaderLastUpdatedTimeKey];
}

- (void)setLastUpdatedTimeKey:(NSString *)lastUpdatedTimeKey
{
    [super setLastUpdatedTimeKey:lastUpdatedTimeKey];
    
    // 如果label隐藏了，就不用再处理
    if (self.lastUpdatedTimeLabel.hidden) return;
    
    NSDate *lastUpdatedTime = [[NSUserDefaults standardUserDefaults] objectForKey:lastUpdatedTimeKey];
    
    // 如果有block
    if (self.lastUpdatedTimeText) {
        self.lastUpdatedTimeLabel.text = self.lastUpdatedTimeText(lastUpdatedTime);
        return;
    }
    
    NSString *MJRefreshHeaderLastTimeText= @"最后更新：";
    NSString *MJRefreshHeaderDateTodayText= @"今天";
    NSString *MJRefreshHeaderNoneLastDateText= @"无记录";

    if([self.locale containsString:@"en"]){
        
        MJRefreshHeaderLastTimeText= @"Last update: ";
        MJRefreshHeaderDateTodayText= @"Toady";
        MJRefreshHeaderNoneLastDateText= @"No record";
    }
    
    if (lastUpdatedTime) {
        // 1.获得年月日
        NSCalendar *calendar = [NSCalendar calendarWithIdentifier:NSCalendarIdentifierGregorian];
        NSUInteger unitFlags = NSCalendarUnitYear| NSCalendarUnitMonth | NSCalendarUnitDay | NSCalendarUnitHour | NSCalendarUnitMinute;
        NSDateComponents *cmp1 = [calendar components:unitFlags fromDate:lastUpdatedTime];
        NSDateComponents *cmp2 = [calendar components:unitFlags fromDate:[NSDate date]];
        
        // 2.格式化日期
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        BOOL isToday = NO;
        if ([cmp1 day] == [cmp2 day]) { // 今天
            formatter.dateFormat = @" HH:mm";
            isToday = YES;
        } else if ([cmp1 year] == [cmp2 year]) { // 今年
            formatter.dateFormat = @"MM-dd HH:mm";
        } else {
            formatter.dateFormat = @"yyyy-MM-dd HH:mm";
        }
        NSString *time = [formatter stringFromDate:lastUpdatedTime];
        
        // 3.显示日期
        self.lastUpdatedTimeLabel.text = [NSString stringWithFormat:@"%@%@%@",
                                          MJRefreshHeaderLastTimeText,
                                          isToday ?MJRefreshHeaderDateTodayText : @"",
                                          time];
    } else {
        self.lastUpdatedTimeLabel.text = [NSString stringWithFormat:@"%@%@",
                                          MJRefreshHeaderLastTimeText,
                                          MJRefreshHeaderNoneLastDateText];
    }
}



@end
