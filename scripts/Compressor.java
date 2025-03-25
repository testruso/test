package org.aoclient.scripts;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class Compressor {

    public static int compressResource(String resourceName, String aoName) {

        if (resourceName == null || resourceName.isEmpty() || aoName == null || aoName.isEmpty()) {
            System.err.println("resourceName or aoName cannot be null or empty");
            return -1;
        }

        Path path = Paths.get(resourceName);

        if (!Files.exists(path)) {
            System.err.println("'" + resourceName + "' does not exist");
            return -1;
        }

        if (!Files.isDirectory(path)) {
            System.err.println("'" + resourceName + "' is not a directory");
            return -1;
        }

        final AtomicInteger fileCount = new AtomicInteger(0);
        try (FileOutputStream fos = new FileOutputStream(aoName); ZipOutputStream zos = new ZipOutputStream(fos, StandardCharsets.UTF_8)) {
            try (Stream<Path> pathStream = Files.walk(path)) {
                pathStream.filter(Files::isRegularFile).forEach(file -> {
                    if (compressFile(path, file, zos)) fileCount.incrementAndGet();
                });
            } catch (IOException e) {
                System.out.println("Error walking directory structure: " + e.getMessage());
                return -1;
            } catch (Exception e) {
                System.out.println("Unexpected error during directory traversal: " + e.getClass().getName() + ": " + e.getMessage());
                return -1;
            }
            return fileCount.get();
        } catch (IOException e) {
            System.out.println("Error creating ao file: " + e.getMessage());
            return -1;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            return -1;
        }
    }

    public static byte[] readResource(String aoName, String resourceName) {

        if (aoName == null || resourceName == null) {
            System.err.println("aoName and resourceName cannot be null");
            return null;
        }

        if (aoName.isEmpty() || resourceName.isEmpty()) {
            System.err.println("aoName and resourceName cannot be empty");
            return null;
        }

        try (ZipFile zipFile = new ZipFile(aoName, StandardCharsets.UTF_8)) {

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = entries.nextElement();

                // Ignora directorios
                if (entry.isDirectory()) continue;

                String fileName = getFileName(entry.getName());
                String baseName = getBaseName(fileName);

                if (baseName.equalsIgnoreCase(resourceName)) {
                    // Lee los datos del recurso encontrado desde un try-with-resources garantizando que el stream se cierre automaticamente al finalizar
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        return is.readAllBytes();
                    } catch (IOException e) {
                        System.err.println("Error reading resource data for '" + resourceName + "': " + e.getMessage());
                        return null;
                    }
                }

            }

            System.err.println("No files were found matching '" + resourceName + "' in " + aoName);

        } catch (IOException e) {
            System.err.println("Error accessing ao file '" + aoName + "'");
        }

        return null;
    }

    private static String getFileName(String path) {
        int lastSeparatorIndex = Math.max(path.lastIndexOf('/'), path.lastIndexOf('\\'));
        return lastSeparatorIndex >= 0 ? path.substring(lastSeparatorIndex + 1) : path;
    }

    private static String getBaseName(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
    }

    private static boolean compressFile(Path path, Path file, ZipOutputStream zos) {

        final int defaultBufferSize = 8192; // 8KB

        try {
            zos.putNextEntry(new ZipEntry(path.relativize(file).toString()));
            byte[] buffer = new byte[defaultBufferSize];
            int bytesRead;
            try (InputStream in = Files.newInputStream(file)) {
                while ((bytesRead = in.read(buffer)) != -1)
                    zos.write(buffer, 0, bytesRead);
            }
            zos.closeEntry();
            return true;
        } catch (IOException e) {
            System.out.println("Error compressing " + file + " file - I/O error: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Error compressing " + file + " file - Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {

        String resourceName = "resources/sounds";
        String aoName = "resources/sounds.ao";

        long start = System.nanoTime();
        int filesCompressed = compressResource(resourceName, aoName);
        long time = (System.nanoTime() - start) / 1_000_000;

        if (filesCompressed > 0)
            System.out.println("Compressed " + filesCompressed + " file" + (filesCompressed > 1 ? "s" : "") + " in " + time + " ms from '" + resourceName + "' in '" + aoName + "'");
        else if (filesCompressed == 0) System.out.println("The folder has no files!");
        else System.err.println("Compression failed!");

        String resourceNameToRead = "2"; // 2.ogg
        byte[] bytes = readResource(aoName, resourceNameToRead);
        if (bytes != null) {
            System.out.println(bytes.length + " bytes were read from resource '" + resourceNameToRead + "'");
            // Opcional: guarda el recurso leido para verificar
            /* try {
                Files.write(Paths.get("output_" + Paths.get(resourceNameToRead).getFileName() + ".map"), bytes);
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            } */
        }

    }

}
