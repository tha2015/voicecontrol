package org.freejava.voicecontrol.impl;

import java.io.ByteArrayOutputStream;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;

public class ProcessManager {
    private Executor exec;

    public void start(String exe, String[] args) throws Exception {
        kill();

        System.out.print("Executing >" + exe);
        for (String arg : args) System.out.println(" " + arg);
        System.out.println();
        exec = new DefaultExecutor();

        exec.setStreamHandler( new PumpStreamHandler( new ByteArrayOutputStream() ) );
        exec.setWatchdog( new ExecuteWatchdog( ExecuteWatchdog.INFINITE_TIMEOUT ) );

        CommandLine cmd = new CommandLine(exe);
        cmd.addArguments(args);

        exec.execute(cmd, new DefaultExecuteResultHandler());



    }

    public void kill() {
        if (exec != null) {
            exec.getWatchdog().destroyProcess();
            exec = null;
        }
    }
}
