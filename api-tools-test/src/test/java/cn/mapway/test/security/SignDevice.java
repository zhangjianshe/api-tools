package cn.mapway.test.security;

import java.text.MessageFormat;

public class SignDevice {
    public static void main(String[] args) {
       String text="I am {0} ,{1}";
        System.out.println(MessageFormat.format(text,1));
    }
}
