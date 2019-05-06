package Principal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProcessBuilder {


    public void executeCommand(String command) throws Exception {
        java.lang.ProcessBuilder processBuilder = new java.lang.ProcessBuilder();
        processBuilder.command("cmd.exe", "/c", command);
        Process process = processBuilder.start();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains("BUILD FAILURE")){
                throw new Exception();
            }
            System.out.println(line);
        }
    }
}