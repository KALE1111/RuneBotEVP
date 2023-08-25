package com.example;


import com.example.EthanApiPlugin.EthanApiPlugin;

import com.example.PacketUtils.PacketUtilsPlugin;

import com.example.testerplugin.testerplugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(EthanApiPlugin.class, PacketUtilsPlugin.class, testerplugin.class);
        RuneLite.main(args);
    }
}