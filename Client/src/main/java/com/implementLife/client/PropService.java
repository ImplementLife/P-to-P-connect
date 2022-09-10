package com.implementLife.client;

import com.implementLife.client.UI.UIProvider;

import java.io.*;
import java.util.Properties;

public class PropService {
    //region Singleton
    private static PropService propService;
    public static PropService getPropService() {
        if (propService == null) {
            propService = new PropService();
        }
        return propService;
    }
    private PropService() {}
    //endregion

    private static final String PATH_PROP = System.getProperty("user.dir") + "/resources/config.prop";

    private Properties load() {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(PATH_PROP)) {
            prop.load(input);
        } catch (IOException ignore) {}
        return prop;
    }

    private void save(Properties prop) {
        try (OutputStream output = new FileOutputStream(PATH_PROP)) {
            prop.store(output, null);
        } catch (IOException e) {
            UIProvider.showErrDialog(new RuntimeException(new File(PATH_PROP).getAbsolutePath()));
        }
    }

    public void saveId(String uuid) {
        Properties prop = load();
        prop.setProperty("uuid", uuid);
        save(prop);
    }

    public String getId() {
        Properties prop = load();
        return (String) prop.get("uuid");
    }
}
