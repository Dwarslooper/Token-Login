package com.dwarslooper.loginwithtoken.screen;

import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.WLabel;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.mixin.MinecraftClientAccessor;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.gui.Element;
import net.minecraft.client.util.Session;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class LoginWithTokenScreen extends WindowScreen {
    private final WindowScreen parent;
    private WTextBox accessTokenTextBox;
    String name;
    String uuid;
    private WHorizontalList displayBox;
    private WButton login;
    private WLabel displayData;

    public LoginWithTokenScreen(GuiTheme theme, WindowScreen parent) {
        super(theme, "Login with token");
        this.parent = parent;
    }

    public void initWidgets() {

        displayBox = add(theme.horizontalList()).expandX().center().widget();

        this.accessTokenTextBox = this.add(this.theme.textBox("", "Access Token")).expandX().widget();

        WHorizontalList l = add(theme.horizontalList()).expandX().center().widget();

        login = l.add(this.theme.button("Login")).padVertical(20).expandCellX().expandX().widget();
        WButton info = l.add(this.theme.button("Info")).padVertical(20).expandCellX().expandX().widget();
        login.action = () -> get(true);
        info.action = () -> get(false);
    }

    private void get(boolean cs) {
        String token = this.accessTokenTextBox.get();
        if(token.isEmpty()) return;
        login.set("...");

        Thread t = new Thread(() -> {
            try {
                URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer " + token);
                con.setDoOutput(true);

                try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    JSONObject jsonObject = new JSONObject(response.toString());

                    this.name = jsonObject.getString("name");
                    this.uuid = jsonObject.getString("id");

                    login.set("Login");
                    if(cs) {
                        label("Logged in as " + name + "!", Color.GREEN);
                        ((MinecraftClientAccessor) mc).setSession(new Session(name, uuid, token, Optional.empty(), Optional.empty(), Session.AccountType.MSA));
                    } else {
                        label("Name: " + name + "\nID: " + uuid, Color.GREEN);
                    }

                }

            } catch (Exception e) {
                label("Invalid / expired Token!", Color.RED);
                login.set("Login");
            }
        });
        t.start();
    }

    private void label(String text, Color color) {
        if(this.displayData != null){
            if (text.isEmpty()) {
                remove((Element) this.displayData);
            } else {
                this.displayData.color(color);
                this.displayData.set(text);
            }
        } else {
            this.displayData = displayBox.add(theme.label("")).widget();
            label(text, color);
        }
    }

}
