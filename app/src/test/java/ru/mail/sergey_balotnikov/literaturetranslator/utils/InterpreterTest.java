package ru.mail.sergey_balotnikov.literaturetranslator.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class InterpreterTest {

    @Test
    public void translatedText() {
        assertEquals("hello", "привет");
    }
}