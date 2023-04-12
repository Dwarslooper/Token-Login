package com.dwarslooper.loginwithtoken.mixins;

import com.dwarslooper.loginwithtoken.screen.LoginWithTokenScreen;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.screens.AccountsScreen;
import meteordevelopment.meteorclient.gui.widgets.WWidget;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccountsScreen.class)
public abstract class AccountScreenMixin extends WindowScreen {
    public AccountScreenMixin(GuiTheme theme, WWidget icon, String title) {
        super(theme, icon, title);
    }

    @Shadow
    protected abstract void addButton(WContainer c, String text, Runnable action);

    @Inject(
        method = {"initWidgets"},
        at = {@At("TAIL")},
        remap = false
    )
    public void onInitWidgets(CallbackInfo ci) {
        WHorizontalList l1 = this.add(this.theme.horizontalList()).expandCellX().expandX().widget();
        this.addButton(l1, "Login with token", () -> {
            this.client.setScreen(new LoginWithTokenScreen(GuiThemes.get(), this));
        });
    }
}
