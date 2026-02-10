package com.austria.logistics;

import com.austria.logistics.core.EngineImpl;
import com.austria.logistics.core.contracts.Engine;

public class Startup {
    public static void main(String[] args) {
        Engine engine = new EngineImpl();
        engine.start();
    }
}
