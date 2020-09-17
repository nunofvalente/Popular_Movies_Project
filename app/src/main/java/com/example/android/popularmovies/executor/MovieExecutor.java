package com.example.android.popularmovies.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MovieExecutor {

    private static final Object LOCK = new Object();
    private static MovieExecutor eInstance;
    private final Executor databaseExecutor;

    private MovieExecutor(Executor databaseExecutor) {
        this.databaseExecutor = databaseExecutor;
    }

    public static MovieExecutor getInstance() {
        if(eInstance == null) {
            synchronized (LOCK) {
                eInstance = new MovieExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return eInstance;
    }

    public Executor getDatabaseExecutor() {
        return databaseExecutor;
    }
}
