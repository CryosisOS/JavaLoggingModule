/**
 * Author: CryosisOS
 * Date Created: 2021-01-07
 **/

package cryodev.modules;

//Java SDK Imports
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Logger {
    /// CLASS CONSTANTS
    private static final HashMap<Integer, String> SEVERITY_LEVELS = new HashMap<>(){{
       put(SEVERITY.INFO, "INFO");
       put(SEVERITY.WARNING, "WARNING");
       put(SEVERITY.ERROR, "ERROR");
       put(SEVERITY.CRITICAL, "CRITICAL");
       put(SEVERITY.EVENT, "EVENT");
    }};

    /// CLASS FIELDS
    private String logFileLocation_;

    public Logger(String outputFileLocation) throws IOException {
        setLogFileLocation(outputFileLocation);
    }

    public void setLogFileLocation(String new_filename) throws IOException {
        if (validFileLocation(new_filename)) logFileLocation_ = new_filename;
        else throw new IOException("You cannot write to the output log file.");
        initFileLocation(logFileLocation_);
    }

    public String getLogFileLocation() {
        return logFileLocation_;
    }

    //Writes to file and prints to console.
    public void log(int severity, String message) throws IOException{
        writeToConsole(SEVERITY_LEVELS.get(severity), message);
        writeToFile(SEVERITY_LEVELS.get(severity), message);
    }

    public void log(int severity, String message, boolean writeToConsole, boolean writeToFile) throws IOException{
        if(writeToConsole && writeToFile) log(severity, message);
        if(writeToConsole) writeToConsole(SEVERITY_LEVELS.get(severity), message);
        if(writeToFile) writeToFile(SEVERITY_LEVELS.get(severity), message);
    }

    private boolean validFileLocation(String filePath) throws IOException {
        if (!validDirectory(filePath)) throw new IOException("The directory containing the file does not exist");
        return validWritePerm(filePath);
    }

    private void initFileLocation(String filePath) throws IOException {
        if (!fileExists(filePath)) createFile(filePath);
    }

    private boolean validDirectory(String directory) {
        File file = new File(directory);
        if (!file.isDirectory()) file = file.getParentFile();
        return file.exists();
    }

    private boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    private boolean validWritePerm(String filename) {
        File file = new File(filename);
        return file.canWrite();
    }

    private void createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
    }

    private String getDateTimeString(){
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    private void writeToConsole(String severity, String message){
        String output = String.format("%s: %s\n", severity, message);
        System.out.print(output);
    }

    private void writeToFile(String severity, String message) throws IOException{
        String timeStamp = getDateTimeString();
        String output = String.format("%s:%s: %s\n", timeStamp, severity, message);
        String file = new File(logFileLocation_).getCanonicalPath();
        Files.write(Paths.get(file), output.getBytes(), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
    }
}