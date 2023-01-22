package org.example;

import org.apache.commons.lang3.StringUtils;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        AtomicInteger lengthThree = new AtomicInteger(0);
        AtomicInteger lengthFour = new AtomicInteger(0);
        AtomicInteger lengthFive = new AtomicInteger(0);

        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread thread = new Thread(() -> {
            for (String text : texts) {
                if (isPalindromeUsingIntStream(text)) {
                    if (text.length() == 3) lengthThree.incrementAndGet();
                    if (text.length() == 4) lengthFour.incrementAndGet();
                    if (text.length() == 5) lengthFive.incrementAndGet();
                }
            }
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            for (String text : texts) {
                if (oneLetter(text)) {
                    if (text.length() == 3) lengthThree.incrementAndGet();
                    if (text.length() == 4) lengthFour.incrementAndGet();
                    if (text.length() == 5) lengthFive.incrementAndGet();
                }
            }
        });
        thread1.start();
        Thread thread2 = new Thread(() -> {
            for (String text : texts) {
                if (increasingCharacters(text)) {
                    if (text.length() == 3) lengthThree.incrementAndGet();
                    if (text.length() == 4) lengthFour.incrementAndGet();
                    if (text.length() == 5) lengthFive.incrementAndGet();
                }
            }
        });
        thread2.start();

        thread.join();
        thread1.join();
        thread2.join();
        System.out.println("Красивых слов с длиной 3: " + lengthThree + " шт.");
        System.out.println("Красивых слов с длиной 4: " + lengthFour + " шт.");
        System.out.println("Красивых слов с длиной 5: " + lengthFive + " шт.");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static boolean isPalindromeUsingIntStream(String text) {
        String temp = text.replaceAll("\\s+", "").toLowerCase();
        return IntStream.range(0, temp.length() / 2)
                .noneMatch(i -> temp.charAt(i) != temp.charAt(temp.length() - i - 1));
    }

    public static boolean oneLetter(String text) {
        char letter = text.charAt(0);
        int length = text.length();
        String result = StringUtils.repeat(letter, length);
        return result.equals(text);
    }

    public static boolean increasingCharacters(String text) {
        String sort = text.chars()
                .sorted()
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return sort.equals(text);
    }
}
