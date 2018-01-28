/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.xuexiang.xlog;

import android.app.Application;

import com.xuexiang.xlog.annotation.LogLevel;
import com.xuexiang.xlog.annotation.LogSegment;
import com.xuexiang.xlog.crash.CrashHandler;
import com.xuexiang.xlog.crash.SendEmailCrashListener;
import com.xuexiang.xlog.logger.Logger;
import com.xuexiang.xlog.strategy.format.DiskFormatStrategy;
import com.xuexiang.xlog.strategy.format.IFormatStrategy;
import com.xuexiang.xlog.strategy.log.DiskLogStrategy;
import com.xuexiang.xlog.strategy.log.ILogStrategy;
import com.xuexiang.xlog.utils.TimeUtils;

/**
 * @author xuexiang
 * @date 2018/1/26 上午11:35
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XLog.init(this);

        Logger.newBuilder("PrettyLogger").build();

        ILogStrategy diskLogStrategy = DiskLogStrategy.newBuilder()       //日志打印策略
                .setLogDir("xlogDemo")                                    //设置日志文件存储的根目录
                .setLogPrefix("xlog")                                     //设置日志文件名的前缀
                .setLogSegment(LogSegment.FOUR_HOURS)                     //设置日志记录的时间片间隔
                .setLogLevels(LogLevel.ERROR, LogLevel.DEBUG)             //设置日志记录的等级
                .build();
        IFormatStrategy formatStrategy = DiskFormatStrategy.newBuilder()  //日志格式策略
                .setShowThreadInfo(false)                                 //设置是否显示线程信息
                .setTimeFormat(TimeUtils.LOG_LINE_TIME)                   //设置日志记录时间的时间格式
                .setMethodCount(1)                                        //设置打印显示的方法数
                .setLogStrategy(diskLogStrategy)                          //设置日志打印策略
                .build();
        Logger.newBuilder("DiskLogger")
                .setFormatStrategy(formatStrategy)                        //设置日志格式策略
                .build();

        CrashHandler.getInstance().setOnCrashListener(new SendEmailCrashListener());
    }
}
