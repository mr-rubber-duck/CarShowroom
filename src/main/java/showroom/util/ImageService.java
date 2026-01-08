package showroom.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageService {

    private static final String STORAGE_DIR = System.getProperty("user.home") + File.separator + "showroom_images";

    static {
        File dir = new File(STORAGE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Copies a list of files to the local storage directory and returns the new
     * paths.
     */
    public static List<String> storeImages(List<String> sourcePaths) {
        List<String> storedPaths = new ArrayList<>();
        if (sourcePaths == null)
            return storedPaths;

        for (String source : sourcePaths) {
            try {
                File sourceFile = new File(source);
                if (!sourceFile.exists())
                    continue;

                // Don't copy if it's already in the storage directory
                if (source.startsWith(STORAGE_DIR)) {
                    storedPaths.add(source);
                    continue;
                }

                String extension = "";
                int i = source.lastIndexOf('.');
                if (i > 0) {
                    extension = source.substring(i);
                }

                String newName = UUID.randomUUID().toString() + extension;
                Path destination = Paths.get(STORAGE_DIR, newName);
                Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

                storedPaths.add(destination.toAbsolutePath().toString());
            } catch (IOException e) {
                System.err.println("Failed to copy image: " + source);
                e.printStackTrace();
            }
        }
        return storedPaths;
    }

    public static String getStorageDir() {
        return STORAGE_DIR;
    }
}
