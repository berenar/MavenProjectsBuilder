package Principal;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class ProcessBuilder {

    public void executeCommand(Output out, String command, Container contentPane, int id) throws Exception {
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            out.getConsole().append(" " + line + "\n");
            out.getConsole().setCaretPosition(out.getConsole().getDocument().getLength());
            if (line.contains("BUILD FAILURE")) {
                out.getConsole().append(" \n");
                out.getConsole().append(" \n");
                out.getConsole().append(" ****************************************" + "\n");
                out.getConsole().append(" *  Error compiling project number  " + id + " *" + "\n");
                out.getConsole().append(" ****************************************" + "\n");
                out.getConsole().append(" \n");
                out.getConsole().append(" \n");
                throw new Exception();
            }
        }
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}