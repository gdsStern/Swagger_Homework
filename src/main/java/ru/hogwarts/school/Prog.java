package ru.hogwarts.school;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Prog {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        getInt1();
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        getInt2();
        System.out.println(System.currentTimeMillis() - start);
    }

    public static int getInt1() {
        return Stream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
    }

    public static int getInt2() {
        return IntStream
                .iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);
    }
}
