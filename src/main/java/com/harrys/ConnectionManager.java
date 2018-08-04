package com.harrys;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConnectionManager {
    private static final int PORT = 22;
    private final Logger log = LoggerFactory.getLogger(ConnectionManager.class);
    private final SshConfig config;
    private Session session;
    private ChannelSftp sftpChannel;

    ConnectionManager(SshConfig config) {
        this.config = config;
    }

    void sync(String remoteDir, String localDir) {
        String remoteFileName = localDir;
        try {
            Vector<ChannelSftp.LsEntry> ls = sftpChannel.ls(remoteDir);
            for (ChannelSftp.LsEntry entry : ls) {
                if (entry.getFilename().equals(".") || entry.getFilename().equals("..")) continue;
                remoteFileName = remoteFileName.concat("/").concat(entry.getFilename());
                String localFileName = localDir.concat("/").concat(entry.getFilename());
                if (entry.getAttrs().isDir()) {
                    new File(localFileName).mkdirs();
                    sync(remoteFileName, localFileName);
                } else {
                    log.debug(String.format("Copying %s to %s", remoteFileName, localFileName));
                    sftpChannel.get(remoteFileName, localFileName);
                }
            }
        } catch (SftpException e) {
            log.error(String.format("Could not copy file %s: %s", remoteFileName,  e.getMessage()));
        }
    }

    void disconnect() {
        session.disconnect();
        log.info("Sync completed");
    }

    void connect() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(config.getRemoteUsername(), config.getRemoteAddress(), PORT);
            session.setPassword(String.valueOf(config.getRemotePassword()));
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            sftpChannel = (ChannelSftp) channel;
        } catch (JSchException e) {
            log.error(String.format("Could not connect to host: %s", e.getMessage()));
        }
    }
}
