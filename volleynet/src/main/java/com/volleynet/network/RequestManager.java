package com.volleynet.network;

/**
 * Volley wrapper
 */

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.volleynet.application.BaseApplication;

public class RequestManager {
    private static RequestManager singleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private RequestManager() {
        // getApplicationContext() is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        // can cause problem if getApplication returns null, not handling that because
        // it should be non null
        requestQueue = Volley.newRequestQueue(BaseApplication.getApplication());

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            final int cacheSize = 4 * 1024 * 1024;
            private final LruCache<String, Bitmap> cache = new LruCache<>(cacheSize);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    /**
     * @return the singleton object of the class
     */
    public static synchronized RequestManager getInstance() {
        if (singleton == null) {
            singleton = new RequestManager();
        }
        return singleton;
    }

    /**
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    /**
     * Adds the request to the request queue
     *
     * @param req the request object
     * @return The passed-in request
     */
    public <T> Request<T> addToRequestQueue(Request<T> req) {
        return getRequestQueue().add(req);
    }

    /**
     * @return image loader object
     */
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
