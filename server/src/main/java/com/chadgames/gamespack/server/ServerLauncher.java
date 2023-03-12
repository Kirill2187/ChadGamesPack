package com.chadgames.gamespack.server;

import java.io.IOException;

/** Launches the server application. */
public class ServerLauncher {

    private static GameServer server;

    public static void main(String[] args) throws IOException {
        server = new GameServer();
        System.out.println("Server started successfully");
    }
}
