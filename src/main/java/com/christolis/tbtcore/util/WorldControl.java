package com.christolis.tbtcore.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class WorldControl {

    /**
     * Removes a world from the storage disk (recursive).
     *
     * @param path The path of the folder to delete
     */
    public static void removeWorld(File path) {
        if (path.exists()) {
            File files[] = path.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    removeWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        path.delete();
    }

    /**
     * Unloads a world.
     *
     * @param world The world's name to unload.
     */
    public static void unloadWorld(World world) {
        if(world != null) {
            Bukkit.getServer().unloadWorld(world, true);
        }
    }

    /**
     * Copies a new world.
     *
     * @param source The source world.
     * @param target The copied world's save location.
     */
    public static void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList(
                "uid.dat", "session.dat"
            ));
            if (!ignore.contains(source.getName())) {
                if (source.isDirectory()) {
                    if (!target.exists())
                        target.mkdirs();

                    String files[] = source.list();

                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;

                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);

                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
