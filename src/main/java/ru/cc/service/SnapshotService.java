package ru.cc.service;

import org.springframework.stereotype.Service;
import ru.cc.config.AgentConfig;
import ru.cc.config.SetOfWorkParameters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class SnapshotService {

    private static final String PLUGINS_PATH = "C:\\AgentJson\\Plugins";
    private static final String SNAPSHOT_DIRECTORY = "C:\\AgentJson\\SNAPSHOTS";
    private AgentConfig agentConfig;

    public void handleSnapshot(SetOfWorkParameters workParameters) {
            System.out.println("Starting snapshot process");
            String snapshotNumber = workParameters.getParamVal();
            agentConfig.setSnapshotNum(snapshotNumber);
            Path snapshotDir = Paths.get(SNAPSHOT_DIRECTORY, "SNAPSHOT_" + snapshotNumber);
            Path tempDir = Paths.get(SNAPSHOT_DIRECTORY, "temp");
            try {
                Files.createDirectories(snapshotDir);
                Files.createDirectories(tempDir);
                List<File> plugins = getPlugins();
                for (File plugin : plugins) {
                    runPlugin(plugin, tempDir);
                    moveTempFilesToSnapshot(snapshotDir, tempDir);
                    clearTempDirectory(tempDir);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Здесь логика проверки изменений и отправка на storage
    }
    private List<File> getPlugins() {
        System.out.println("Starting getPlugins process");
        File pluginDir = new File(PLUGINS_PATH);
        File[] files = pluginDir.listFiles((dir, name) -> name.endsWith(".jar"));
        return files != null ? List.of(files) : List.of();
    }

    private void runPlugin(File plugin, Path tempDir) {
        System.out.println("Starting pluginRun process");
        String command = "java -jar " + plugin.getAbsolutePath() + " " + tempDir.toString();
        try {
            System.out.println("Plugin work has begun");
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveTempFilesToSnapshot(Path snapshotDir, Path tempDir) throws IOException {
        System.out.println("Starting moveTempFilesToSnapshot process");
        Files.walk(tempDir)
                .filter(Files::isRegularFile)
                .forEach(sourcePath -> {
                    Path targetPath = snapshotDir.resolve(tempDir.relativize(sourcePath));
                    try {
                        Files.move(sourcePath, targetPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void clearTempDirectory(Path tempDir) throws IOException {
        System.out.println("Starting clearTempDirectory process");
        Files.walk(tempDir)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}

