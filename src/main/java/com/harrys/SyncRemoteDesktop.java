package com.harrys;

import java.io.Console;

public class SyncRemoteDesktop {
    public SyncRemoteDesktop() {
        Console console = System.console();

        System.out.print("Remote address: ");
        String address = console.readLine();
        console.flush();

        System.out.print("Remote username: ");
        String username = console.readLine();
        console.flush();

        System.out.print("Remote password: ");
        char[] password = console.readPassword();
        console.flush();

        System.out.print("Remote directory: ");
        String remoteDir = console.readLine();
        console.flush();

        System.out.print("Local directory: ");
        String localDir = console.readLine();
        console.flush();

        System.out.println("\nSyncing . . . \n");

        SshConfig config = new SshConfig(address, username, password);

        ConnectionManager manager = new ConnectionManager(config);
        manager.connect();
        manager.sync(remoteDir, localDir);
        manager.disconnect();
    }

    public static void main(String[] args) {
        new SyncRemoteDesktop();
    }
}
