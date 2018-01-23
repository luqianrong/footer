package com.example.footer.application;


import com.example.footer.config.Config;

/**
 * Created by android on 9/10/15.
 */
public class GlobalApplication extends BaseApplication {


    public static GlobalApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;







        if (Config.DEBUG) {
//            LeakCanary.install(this);
        }
    }

    /**
     * init
     */
    private void init() {

    }

}
