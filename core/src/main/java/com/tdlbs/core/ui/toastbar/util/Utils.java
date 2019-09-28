package com.tdlbs.core.ui.toastbar.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ================================================
 * 对类的描述
 *
 * @author: markgu
 * @e-mail: <a href="mailto:guhao561@gmail.com">Contact me</a>
 * @time: 2019-08-06 16:39
 * ================================================
 */
public class Utils {
    private static Boolean MIUI8orLater;

    public static boolean isMIUI8orLater() {
        if (MIUI8orLater != null) {
            return MIUI8orLater;
        }
        String propName = "ro.miui.ui.version.name";
        String line = null;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            MIUI8orLater = false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ignored) {
                }
            }
        }
        if (line != null) {
            try {
                int num = Integer.parseInt(line.replaceAll("[\\D]", ""));
                if (num >= 8) {
                    MIUI8orLater = true;
                } else {
                    MIUI8orLater = false;
                }
            } catch (Exception e) {
                MIUI8orLater = false;
            }
        } else {
            MIUI8orLater = false;
        }
        return MIUI8orLater;
    }
}
