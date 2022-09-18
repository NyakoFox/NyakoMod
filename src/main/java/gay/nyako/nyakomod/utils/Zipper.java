package gay.nyako.nyakomod.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
    File source;
    File output;
    List<File> files;

    public Zipper(File source, File output) {
        this.source = source;
        this.output = output;

        this.files = new ArrayList<>();
    }

    public void zip() throws IOException {
        generateFileList(source);

        FileOutputStream fos = new FileOutputStream(output);
        var zos = new ZipOutputStream(fos);
        for (var file : files) {
            writeFileToZip(file, zos);
        }

        zos.close();
        fos.close();
    }

    void writeFileToZip(File input, ZipOutputStream zos) {
        try (FileInputStream fis = new FileInputStream(input)) {
            byte[] buf = new byte[1024];
            int len;

            zos.putNextEntry(getZipEntry(input));
            while ((len = fis.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }

            zos.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ZipEntry getZipEntry(File file) {
        var name = file.getPath().replace('\\', '/');
        var sourcePath = source.getPath().replace('\\', '/');
        String newName;
        if (sourcePath.endsWith("/")) {
            newName = name.replace(sourcePath, "");
        } else {
            newName = name.replace(sourcePath + "/", "");
        }

        newName = newName.replace("./", "");

        return new ZipEntry(newName);
    }

    void generateFileList(File node) {
        if (node.isFile()) {
            files.add(node);
        }

        if (node.isDirectory()) {
            for (var file : node.list()) {
                generateFileList(new File(node, file));
            }
        }
    }
}