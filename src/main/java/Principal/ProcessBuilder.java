package Principal;

import java.awt.Container;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

class ProcessBuilder {

    /**
     * Executes a command in a terminal and outputs results
     *
     * @param out         Console where results will be displayed
     * @param command     the command to execute
     * @param contentPane JFrame
     * @param id          project id
     * @throws Exception thrown if something went wrong
     */
    public void executeCommand(Output out, String command, Container contentPane, int id) throws Exception {
        //Set the cursor to a wait cursor
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
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
        //sets the cursor to it's default value
        contentPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Executes a simple command in background
     *
     * @param command command to be executed
     * @throws IOException exception
     * @throws InterruptedException exception
     */
    @SuppressWarnings("SameReturnValue")
    public void executeCommand(String command) throws IOException, InterruptedException {
        java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        process.waitFor();
    }
}