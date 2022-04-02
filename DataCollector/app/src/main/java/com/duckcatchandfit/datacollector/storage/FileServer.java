package com.duckcatchandfit.datacollector.storage;

import com.jcraft.jsch.*;

public class FileServer {

    //#region Field

    private String hostAddress;
    private String username;
    private String password;
    private int port = 22;
    private String remoteDirectory;

    //#endregion

    //#region Properties

    public void setHostAddress(String hostAddress) { this.hostAddress = hostAddress; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setPort(int port) { this.port = port; }

    public void setRemoteDirectory(String remoteDirectory) { this.remoteDirectory = remoteDirectory; }

    //#endregion

    //#region Public Methods

    // Src: https://ourcodeworld.com/articles/read/30/how-to-upload-a-file-to-a-server-using-jsch-sftp-in-android
    public boolean uploadFile(String filePath) {
        try {
            Session session = createSession();
            session.connect();

            Channel channel = session.openChannel("sftp");
            channel.connect();

            ChannelSftp sftp = (ChannelSftp) channel;
            sftp.cd(remoteDirectory);

            sftp.put(filePath, remoteDirectory + "/" + getFileName(filePath));

            channel.disconnect();
            session.disconnect();

            return true;
        }
        catch (IllegalArgumentException | JSchException | SftpException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    //#endregion

    //#region Private Methods

    private Session createSession() throws JSchException {
        JSch ssh = new JSch();

        /* Remember that this is just for testing and we need a quick access,
         * you can add an identity and known_hosts file to prevent Man In the Middle attacks */
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");

        Session session = ssh.getSession(username, hostAddress, port);
        session.setConfig(config);
        session.setPassword(password);

        return session;
    }

    private String getFileName(String filePath) throws IllegalArgumentException {
        final String[] parts = filePath.split("/");
        return parts[parts.length - 1];
    }

    //#endregion
}
