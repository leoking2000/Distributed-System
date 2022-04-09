package com.company.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class FileChunk implements Serializable
{
    public static final int MAX_CHUNK_SIZE = 512; // 0.5 kb

    public static ArrayList<FileChunk> Load(String filepath) throws IOException
    {
        File file = new File(filepath);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        ArrayList<FileChunk> chunks = new ArrayList<>();

        int bytes_left = fileContent.length;
        int index = 0;

        while (bytes_left != 0)
        {
            if(bytes_left >= MAX_CHUNK_SIZE)
            {
                byte[] chunk = new byte[MAX_CHUNK_SIZE];

                System.arraycopy(fileContent, index * MAX_CHUNK_SIZE, chunk, 0, MAX_CHUNK_SIZE);

                chunks.add(new FileChunk(index, chunk));
                index++;
                bytes_left -= MAX_CHUNK_SIZE;
            }
            else
            {
                byte[] chunk = new byte[bytes_left];

                System.arraycopy(fileContent, index * MAX_CHUNK_SIZE, chunk, 0, bytes_left);

                chunks.add(new FileChunk(index, chunk));
                index++;
                bytes_left = 0;
            }

        }

        return  chunks;
    }

    public static void Store(String path , ArrayList<FileChunk> chunks) throws IOException
    {
        chunks.sort((o1, o2) -> {
            if(o1.index == o2.index) return  0;
            return o1.index < o2.index ? 1 : -1;
        });

        int size = 0;

        for (FileChunk chunk: chunks)
        {
            size += chunk.data.length;
        }

        byte[] bytesToWrite = new byte[size];

        for (FileChunk chunk: chunks)
        {
            System.arraycopy(
                    chunk.data,
                    0,
                    bytesToWrite,
                    chunk.index * MAX_CHUNK_SIZE,
                    chunk.data.length);
        }

        Files.write( (new File(path)).toPath(), bytesToWrite, StandardOpenOption.CREATE_NEW);
    }

    private final int index;
    private final byte[] data;

    private FileChunk(int index, byte[] data)
    {
        this.index = index;
        this.data = data;
    }

    public int getIndex()
    {
        return index;
    }

    public byte[] getData()
    {
        return data;
    }
}
