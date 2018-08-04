package com.harrys;

class SshConfig {
    private final String remoteAddress;
    private final String remoteUsername;
    private final char[] remotePassword;

    SshConfig(String remoteAddress, String remoteUsername, char[] remotePassword) {
        this.remoteAddress = remoteAddress;
        this.remoteUsername = remoteUsername;
        this.remotePassword = remotePassword;
    }

    String getRemoteAddress() {
        return remoteAddress;
    }

    String getRemoteUsername() {
        return remoteUsername;
    }

    char[] getRemotePassword() {
        return remotePassword;
    }
}
