package com.jacques.savedata;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class SaveDataTests {
    BoardData board;
    String fileRoot;

    @Before
    public void setup() throws CloneNotSupportedException {
        board = new BoardData();
        board = board.defaultBoard();
        fileRoot = Paths.get("").toAbsolutePath() + "\\currentboard";
    }

    @Test
    public void testSerialisation() {
        BoardData.serialize(board,fileRoot + "\\board.dat");
        BoardData board2 = BoardData.deserialize(fileRoot + "\\board.dat");

        assertEquals(board, board2);
        assertEquals(board.getClass(), board2.getClass());
    }

    @Test
    public void testDeepClone() {
        BoardData.serialize(board,fileRoot + "\\board.dat");
        BoardData board2 = BoardData.deserialize(fileRoot + "\\board.dat");

        board.boardName = "Different Board";
        assertNotEquals(board, board2);
    }

    @Test
    public void TestStudentJson() throws IOException {
        File jsonFile = new File(fileRoot + "\\board.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        BufferedWriter writer = Files.newBufferedWriter(jsonFile.toPath());
        gson.toJson(board,writer);
        writer.close();

        System.out.println(jsonFile.toPath());
        Runtime.getRuntime().exec("explorer.exe /select," + jsonFile.toPath());

        BufferedReader reader = Files.newBufferedReader(jsonFile.toPath());
        Type t = new TypeToken<BoardData>(){}.getType();
        BoardData boardFromFile = gson.fromJson(reader, t);
        reader.close();
        assertEquals(board,boardFromFile);
    }
}
