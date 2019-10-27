package com.oracle.hpcm.utils;

import java.io.*;
import org.testng.Reporter;

class StreamGobbler extends Thread {

    InputStream is;
    String type;
    ReturnObject returnObject;

    StreamGobbler(InputStream is, String type, ReturnObject returnObject) {
        this.is = is;
        this.type = type;
        this.returnObject = returnObject;
    }
public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (type.equals("OUTPUT")) {
                    returnObject.appendLineToOutput(line);
                }
            }
            if (type.equals("ERROR")) {
                returnObject.appendLineToError(line);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

public class Executor {

    public static ReturnObject runCommand(String arg) throws IOException, InterruptedException {
        ReturnObject returnObject = new ReturnObject();
        Process proc = null;
        final String OS = System.getProperty("os.name").toLowerCase();
        Reporter.log("OS : " + OS);
        if (OS.indexOf("win") >= 0) {
            String[] cmd = new String[3];

            cmd[0] = "cmd.exe";
            cmd[1] = "/C";
            cmd[2] = arg;

            Runtime rt = Runtime.getRuntime();
            Reporter.log("Executing " + cmd[0] + " " + cmd[1]
                    + " " + cmd[2], true);
            proc = rt.exec(cmd);
        }

        if (OS.indexOf("nux") >= 0) {
            String path = System.getProperty("path_to_epmautomate");
            String cmd = arg.replace("epmautomate", path);

            Runtime rt = Runtime.getRuntime();
            Reporter.log("Executing " + cmd, true);
            proc = rt.exec(cmd);

        }

        StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ERROR", returnObject);

        StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "OUTPUT", returnObject);

        errorGobbler.start();
        outputGobbler.start();

        int exitVal = proc.waitFor();

        returnObject.setReturnValue(exitVal);
        return returnObject;

    }
}
