package Tests;

import com.company.EventDeliverySystem.ValueTypes.FileChunk;
import com.company.EventDeliverySystem.ValueTypes.Text;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.*;

class FileChunkTest
{

    @Test
    void Test1()
    {
        String name = "leonidas";
        ArrayList<FileChunk> chunks = FileChunk.Generate(name.getBytes(StandardCharsets.UTF_8));

        Text t = new Text(chunks, null);

        Assertions.assertEquals(name, t.getTheText());

    }

}