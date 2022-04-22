package com.company.EventDeliverySystem.ValueTypes;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/*
a data Chunk used to send data piece by piece
 */
public class FileChunk implements Serializable
{
    public static final int MAX_CHUNK_SIZE = 512; // 0.5 kb

    // all chunks it returns except the last has length MAX_CHUNK_SIZE
    public static  ArrayList<FileChunk> Generate(byte[] Content)
    {

        ArrayList<FileChunk> chunks = new ArrayList<>();

        int bytes_left = Content.length;
        int index = 0;

        while (bytes_left != 0)
        {
            // if we have more bytes left than MAX_CHUNK_SIZE
            if(bytes_left >= MAX_CHUNK_SIZE)
            {
                // create a chunk of length MAX_CHUNK_SIZE
                byte[] chunk = new byte[MAX_CHUNK_SIZE];

                // copy the bytes into the newly created chunk
                System.arraycopy(Content, index * MAX_CHUNK_SIZE, chunk, 0, MAX_CHUNK_SIZE);

                // add the chunk
                chunks.add(new FileChunk(index, chunk));
                index++;
                bytes_left -= MAX_CHUNK_SIZE;
            }
            else // write the rest in a chunk
            {
                // create a chunk of length bytes_left
                byte[] chunk = new byte[bytes_left];
                // copy the bytes into the newly created chunk
                System.arraycopy(Content, index * MAX_CHUNK_SIZE, chunk, 0, bytes_left);

                // add the chunk
                chunks.add(new FileChunk(index, chunk));
                index++;
                bytes_left = 0;
            }

        }

        return  chunks;
    }

    public static byte[] GetData(ArrayList<FileChunk> chunks)
    {
        // sort the chunk array by index
        chunks.sort((o1, o2) -> {
            if(o1.index == o2.index) return  0;
            return o1.index < o2.index ? 1 : -1;
        });

        // find the size in bytes of the ArrayList<FileChunk>
        int size = 0;
        for (FileChunk chunk: chunks)
        {
            size += chunk.data.length;
        }

        // copy the bytes of ArrayList<FileChunk> into a byte[]
        byte[] bytes = new byte[size];
        for (FileChunk chunk: chunks)
        {
            System.arraycopy(
                    chunk.data,
                    0,
                    bytes,
                    chunk.index * MAX_CHUNK_SIZE,
                    chunk.data.length);
        }

        return bytes;
    }

    public static ArrayList<FileChunk> Load(String filepath) throws IOException
    {
        File file = new File(filepath);
        byte[] fileContent = Files.readAllBytes(file.toPath());

        return Generate(fileContent);
    }

    public static void Store(String path , ArrayList<FileChunk> chunks) throws IOException
    {
        byte[] bytesToWrite = GetData(chunks);

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
