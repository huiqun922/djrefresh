//
//  DJRefreshDefaultHeader.h
//  DjrefreshLibraryExample
//
//  Created by Hay Huang on 2023/8/25.
//

#import <Foundation/Foundation.h>
#import <MJRefresh/MJRefresh.h>
#import <React/RCTScrollableProtocol.h>

NS_ASSUME_NONNULL_BEGIN

@interface DJRefreshDefaultHeader : MJRefreshNormalHeader<RCTCustomRefreshControlProtocol>

@property (nonatomic, assign) MJRefreshState preState;
@property (nonatomic, copy) RCTDirectEventBlock onChangeState;
@property (nonatomic, strong) NSString *locale;

@end

NS_ASSUME_NONNULL_END
