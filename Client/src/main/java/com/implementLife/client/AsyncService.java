package com.implementLife.client;

public class AsyncService {
    //region Singleton
    private static AsyncService asyncService;
    public static AsyncService getAsyncService() {
        if (asyncService == null) {
            asyncService = new AsyncService();
        }
        return asyncService;
    }
    private AsyncService() {}
    //endregion

    public void runAsync(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        thread.start();
    }
}
