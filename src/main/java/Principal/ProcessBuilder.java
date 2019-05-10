package Principal;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;

class ProcessBuilder {

    public void executeCommand(Output out, String command, Container contentPane) throws Exception {
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("BUILD FAILURE")) {
                throw new Exception();
            }
            System.out.println(line);
            out.getConsole().append(line+"\n");
            out.getConsole().setCaretPosition(out.getConsole().getDocument().getLength());
        }
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
}