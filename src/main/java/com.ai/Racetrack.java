package com.ai;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.ai.model.Position;

public class Racetrack {
    private final boolean[][] isSafe;
    private final int width, height;
    
    private final Set<Position> startingLine;
    private final Set<Position> finishLine;

    private Racetrack(boolean[][] isSafe, Set<Position> startingLine, Set<Position> finishLine) {
        this.isSafe = isSafe;
        width = isSafe.length;
        height = isSafe[0].length;

        this.startingLine = Collections.unmodifiableSet(startingLine);
        this.finishLine = Collections.unmodifiableSet(finishLine);
    }

    public static Racetrack fromFile(String filename) throws FileNotFoundException, IOException {
        return fromStream(ClassLoader.getSystemClassLoader()
                          .getResourceAsStream(filename));
    }

    public static Racetrack fromStream(InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));

        String[] size = in.readLine().split(",");
        int height = Integer.parseInt(size[0]);
        int width = Integer.parseInt(size[1]);

        boolean[][] isSafe = new boolean[width][height];
        Set<Position> startingLine = new HashSet<>();
        Set<Position> finishLine = new HashSet<>();

        for (int y = 0; y < height; y++) {
            char[] row = in.readLine().toCharArray();
            for (int x = 0; x < width; x++) {
                isSafe[x][y] = row[x] != '#';

                if (row[x] == 'S') {
                    startingLine.add(new Position(x, y));
                } else if (row[x] == 'F') {
                    finishLine.add(new Position(x, y));
                }
            }
        }

        return new Racetrack(isSafe, startingLine, finishLine);
    }

    public boolean isSafe(Position position) {
        if (position.getX() < 0 || position.getX() >= width ||
            position.getY() < 0 || position.getY() >= height)
            return false;
        return isSafe[position.getX()][position.getY()];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Position> startingLine() {
        return startingLine;
    }

    public Position randomStartingPosition() {
        Iterator<Position> iter = startingLine.iterator();
        int randomIndex = (int)(Math.random() * startingLine.size());

        for(; randomIndex != 0; randomIndex--) {
            iter.next();
        }
        return iter.next();
    }

    public Set<Position> finishLine() {
        return finishLine;
    }
}
