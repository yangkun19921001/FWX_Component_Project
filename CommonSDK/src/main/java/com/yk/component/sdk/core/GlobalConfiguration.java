/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yk.component.sdk.core;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.ALog;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.hyphenate.easeui.helper.HXHelper;
import com.jess.arms.base.delegate.AppLifecycles;
import com.jess.arms.di.module.ClientModule;
import com.jess.arms.di.module.GlobalConfigModule;
import com.jess.arms.http.log.RequestInterceptor;
import com.jess.arms.integration.ConfigModule;
import com.yk.component.sdk.BuildConfig;
import com.yk.component.sdk.http.Api;
import com.yk.component.sdk.http.SSLSocketClient;
import com.yk.component.sdk.imgaEngine.Strategy.CommonGlideImageLoaderStrategy;
import com.yk.component.sdk.service.InitializeService;
import com.yk.component.sdk.utils.CrashHandler;

import java.util.List;

import butterknife.ButterKnife;
import me.jessyan.autosize.AutoSizeConfig;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.helper.ExceptionHandler;
import okhttp3.OkHttpClient;
import timber.log.Timber;


/**
 * ================================================
 * CommonSDK 的 GlobalConfiguration 含有有每个组件都可公用的配置信息, 每个组件的 AndroidManifest 都应该声明此 ConfigModule
 *
 * @see <a href="https://github.com/JessYanCoding/ArmsComponent/wiki#3.3">ConfigModule wiki 官方文档</a>
 * Created by JessYan on 30/03/2018 17:16
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class GlobalConfiguration implements ConfigModule {

    private String TAG = this.getClass().getSimpleName();

    @Override
    public void applyOptions(Context context, GlobalConfigModule.Builder builder) {
        if (!BuildConfig.LOG_DEBUG) //Release 时,让框架不再打印 Http 请求和响应的信息
            builder.printHttpLogLevel(RequestInterceptor.Level.NONE);
        builder.baseurl(Api.APP_DOMAIN)
                .imageLoaderStrategy(new CommonGlideImageLoaderStrategy())
                .globalHttpHandler(new GlobalHttpHandlerImpl(context))
                .responseErrorListener(new ResponseErrorListenerImpl())
                .gsonConfiguration((context1, gsonBuilder) -> {//这里可以自己自定义配置Gson的参数
                    gsonBuilder
                            .serializeNulls()//支持序列化null的参数
                            .enableComplexMapKeySerialization();//支持将序列化key为object的map,默认只能序列化key为string的map
                })
                .okhttpConfiguration(new ClientModule.OkhttpConfiguration() {
                    @Override
                    public void configOkhttp(Context context, OkHttpClient.Builder builder) {
                        builder.sslSocketFactory(SSLSocketClient.getSSLSocketFactory(), SSLSocketClient.getTrustManager());
                        builder.hostnameVerifier(SSLSocketClient.getHostnameVerifier());
                        //让 Retrofit 同时支持多个 BaseUrl 以及动态改变 BaseUrl. 详细使用请方法查看 https://github.com/JessYanCoding/RetrofitUrlManager
                        RetrofitUrlManager.getInstance().with(builder);
                    }
                })
                .rxCacheConfiguration((context1, rxCacheBuilder) -> {//这里可以自己自定义配置RxCache的参数
                    rxCacheBuilder.useExpiredDataIfLoaderNotAvailable(true);
                    return null;
                });

    }

    @Override
    public void injectAppLifecycle(Context context, List<AppLifecycles> lifecycles) {
        // AppDelegate.Lifecycle 的所有方法都会在基类Application对应的生命周期中被调用,所以在对应的方法中可以扩展一些自己需要的逻辑
        lifecycles.add(new AppLifecycles() {

            @Override
            public void attachBaseContext(@NonNull Context base) {
                initLog(base);
            }

            @Override
            public void onCreate(@NonNull Application application) {
                if (BuildConfig.LOG_DEBUG) {//Timber日志打印
                    Timber.plant(new Timber.DebugTree());
                    ButterKnife.setDebug(true);
                    ARouter.openLog();     // 打印日志
                    ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                    RetrofitUrlManager.getInstance().setDebug(true);
                }
                ARouter.init(application); // 尽可能早,推荐在Application中初始化
                //根据头条适配
//                Density.setDensity(application);
                //Util 初始化、
                Utils.init(application);
                //初始化崩溃日志捕获
                CrashHandler.getInstance().init(application);
                //初始化一些配置信息
                InitializeService.start(application);
                //单 Activity + 多 Fragment 配置
                Fragmentation.builder()
                        // 设置 栈视图 模式为 （默认）悬浮球模式   SHAKE: 摇一摇唤出  NONE：隐藏， 仅在Debug环境生效
                        .stackViewMode(Fragmentation.BUBBLE)
                        .debug(BuildConfig.DEBUG) // 实际场景建议.debug(BuildConfig.DEBUG)
                        /**
                         * 可以获取到{@link me.yokeyword.fragmentation.exception.AfterSaveStateTransactionWarning}
                         * 在遇到After onSaveInstanceState时，不会抛出异常，会回调到下面的ExceptionHandler
                         */
                        .handleException(new ExceptionHandler() {
                            @Override
                            public void onException(Exception e) {
                                // 以Bugtags为例子: 把捕获到的 Exception 传到 Bugtags 后台。
                                // Bugtags.sendException(e);
                                ALog.i(TAG, e.getMessage());
                            }
                        })
                        .install();

                //初始化短信验证功能
//                MobSDK.init(application);
                //初始化环信
                initHXDemo(application);
                /**
                 * 以下是 AndroidAutoSize 可以自定义的参数, {@link AutoSizeConfig} 的每个方法的注释都写的很详细
                 * 使用前请一定记得跳进源码，查看方法的注释, 下面的注释只是简单描述!!!
                 */
//        AutoSizeConfig.getInstance()

                //是否打印 AutoSize 的内部日志, 默认为 true, 如果您不想 AutoSize 打印日志, 则请设置为 false
//                .setLog(false)

                //是否使用设备的实际尺寸做适配, 默认为 false, 如果设置为 false, 在以屏幕高度为基准进行适配时
                //AutoSize 会将屏幕总高度减去状态栏高度来做适配, 如果设备上有导航栏还会减去导航栏的高度
                //设置为 true 则使用设备的实际屏幕高度, 不会减去状态栏以及导航栏高度
//                .setUseDeviceSize(true)

                //是否全局按照宽度进行等比例适配, 默认为 true, 如果设置为 false, AutoSize 会全局按照高度进行适配
//                .setBaseOnWidth(false)

                //设置屏幕适配逻辑策略类, 一般不用设置, 使用框架默认的就好
//                .setAutoAdaptStrategy(new AutoAdaptStrategy())
//        ;
//                customAdaptForExternal();
            }

            @Override
            public void onTerminate(@NonNull Application application) {

            }
        });
    }

    @Override
    public void injectActivityLifecycle(Context context, List<Application.ActivityLifecycleCallbacks> lifecycles) {
        lifecycles.add(new ActivityLifecycleCallbacksImpl());
    }

    @Override
    public void injectFragmentLifecycle(Context context, List<FragmentManager.FragmentLifecycleCallbacks> lifecycles) {
        lifecycles.add(new FragmentManager.FragmentLifecycleCallbacks() {

            @Override
            public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建是重复利用已经创建的Fragment。
                // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
                // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
                f.setRetainInstance(true);
            }


        });
    }


    /**
     * 给外部的三方库 {@link Activity} 自定义适配参数, 因为三方库的 {@link Activity} 并不能通过实现
     * {@link CustomAdapt} 接口的方式来提供自定义适配参数 (因为远程依赖改不了源码)
     * 所以使用 {@link ExternalAdaptManager} 来替代实现接口的方式, 来提供自定义适配参数
     */
/*    private void customAdaptForExternal() {
        *//**
     * {@link ExternalAdaptManager} 是一个管理外部三方库的适配信息和状态的管理类, 详细介绍请看 {@link ExternalAdaptManager} 的类注释
     *//*
        AutoSizeConfig.getInstance().getExternalAdaptManager()

                //加入的 Activity 将会放弃屏幕适配, 一般用于三方库的 Activity, 详情请看方法注释
                //如果不想放弃三方库页面的适配, 请用 addExternalAdaptInfoOfActivity 方法, 建议对三方库页面进行适配, 让自己的 App 更完美一点
//                .addCancelAdaptOfActivity(DefaultErrorActivity.class)

                //为指定的 Activity 提供自定义适配参数, AndroidAutoSize 将会按照提供的适配参数进行适配, 详情请看方法注释
                //一般用于三方库的 Activity, 因为三方库的设计图尺寸可能和项目自身的设计图尺寸不一致, 所以要想完美适配三方库的页面
                //就需要提供三方库的设计图尺寸, 以及适配的方向 (以宽为基准还是高为基准?)
                //三方库页面的设计图尺寸可能无法获知, 所以如果想让三方库的适配效果达到最好, 只有靠不断的尝试
                //由于 AndroidAutoSize 可以让布局在所有设备上都等比例缩放, 所以只要你在一个设备上测试出了一个最完美的设计图尺寸
                //那这个三方库页面在其他设备上也会呈现出同样的适配效果, 等比例缩放, 所以也就完成了三方库页面的屏幕适配
                //即使在不改三方库源码的情况下也可以完美适配三方库的页面, 这就是 AndroidAutoSize 的优势
                //但前提是三方库页面的布局使用的是 dp 和 sp, 如果布局全部使用的 px, 那 AndroidAutoSize 也将无能为力
                //经过测试 DefaultErrorActivity 的设计图宽度在 380dp - 400dp 显示效果都是比较舒服的
                .addExternalAdaptInfoOfActivity(DefaultErrorActivity.class, new ExternalAdaptInfo(true, 400));
    }*/


    /**
     * 初始化 Log
     */
    private void initLog(Context context) {
        String logPath = Environment.getExternalStorageDirectory() + Constants.ILogPath.DefPath;
        if (!FileUtils.isDir(logPath)) {
            FileUtils.createOrExistsDir(logPath);

        }
        ALog.init(context)
                .setLogSwitch(true)// 设置log总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(true)// 设置是否输出到控制台开关，默认开                .
// setLogSwitch(BuildConfig.DEBUG)// 设置log总开关，包括输出到控制台和文件，默认开
                .setConsoleSwitch(com.jess.arms.BuildConfig.DEBUG)// 设置是否输出到控制台开关，默认开
                .setGlobalTag(null)// 设置log全局标签，默认为空
                // 当全局标签不为空时，我们输出的log全部为该tag，
                // 为空时，如果传入的tag为空那就显示类名，否则显示tag
                .setLogHeadSwitch(true)// 设置log头信息开关，默认为开
                .setLog2FileSwitch(true)// 打印log时是否存到文件的开关，默认关
                .setDir(logPath)// 当自定义路径为空时，写入应用的/cache/log/目录中
                .setFilePrefix(context.getString(com.jess.arms.R.string.app_name) + "----")// 当文件前缀为空时，默认为"alog"，即写入文件为"alog-MM-dd.txt"
                .setBorderSwitch(true)// 输出日志是否带边框开关，默认开
        ;// log栈深度，默认为1
    }

    /**
     * 初始化环信
     */
    public void initHXDemo(Application applicationContext) {
        HXHelper.getInstance().initSDK(applicationContext);
    }
}
