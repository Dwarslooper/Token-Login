package com.dwarslooper.loginwithtoken;

import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing Token Login Addon");
    }

    @Override
    public void onRegisterCategories() {
        // Do nothing cuz no hacks in here lol
    }

    @Override
    public String getPackage() {
        return "com.dwarslooper.loginwithtoken";
    }
}
