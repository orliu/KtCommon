package com.orliu.kotlin.common.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DimenTool {

    private static final String BASE_MODULE_NAME = "lib_common_kt";

    public static void gen() {

        File file = new File("./" + BASE_MODULE_NAME + "/src/main/res/values/dimens.xml");
        BufferedReader reader = null;
        StringBuilder sw320 = new StringBuilder();
        StringBuilder sw360 = new StringBuilder();
        StringBuilder sw480 = new StringBuilder();
        StringBuilder sw600 = new StringBuilder();
        StringBuilder sw720 = new StringBuilder();
        StringBuilder sw800 = new StringBuilder();
        StringBuilder w820 = new StringBuilder();

        // nexus 7
        //StringBuilder large = new StringBuilder();


        try {
            System.out.println("生成不同分辨率：");
            reader = new BufferedReader(new FileReader(file));
            String tempString;

            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {

                if (tempString.contains("</dimen>")) {
                    String start = tempString.substring(0, tempString.indexOf(">") + 1);
                    String end = tempString.substring(tempString.lastIndexOf("<") - 2);
                    int num = Integer.valueOf(tempString.substring(tempString.indexOf(">") + 1, tempString.indexOf("</dimen>") - 2));

                    sw320.append(start).append((int) Math.round(num * 0.8)).append(end).append("\n");
                    sw360.append(start).append((int) Math.round(num * 1.0)).append(end).append("\n");
                    sw480.append(start).append((int) Math.round(num * 1.3)).append(end).append("\n");
                    sw600.append(start).append((int) Math.round(num * 1.6)).append(end).append("\n");
                    sw720.append(start).append((int) Math.round(num * 2.0)).append(end).append("\n");
                    sw800.append(start).append((int) Math.round(num * 2.2)).append(end).append("\n");
                    w820.append(start).append((int) Math.round(num * 2.3)).append(end).append("\n");

                } else {
                    sw320.append(tempString).append("\n");
                    sw360.append(tempString).append("\n");
                    sw480.append(tempString).append("\n");
                    sw600.append(tempString).append("\n");
                    sw720.append(tempString).append("\n");
                    sw800.append(tempString).append("\n");
                    w820.append(tempString).append("\n");
                    //large.append(tempString).append("\n");
                }
            }
            reader.close();
            System.out.println("<!--  sw320 -->");
            System.out.println(sw320);
            System.out.println("<!--  sw360 -->");
            System.out.println(sw360);
            System.out.println("<!--  sw480 -->");
            System.out.println(sw480);
            System.out.println("<!--  sw600 -->");
            System.out.println(sw600);
            System.out.println("<!--  sw720 -->");
            System.out.println(sw720);
            System.out.println("<!--  sw800 -->");
            System.out.println(sw800);
            System.out.println("<!--  w820 -->");
            System.out.println(w820);


            String sw320file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw320dp/dimens.xml";
            String sw360file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw360dp/dimens.xml";
            String sw480file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw480dp/dimens.xml";
            String sw600file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw600dp/dimens.xml";
            String sw720file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw720dp/dimens.xml";
            String sw800file = "./" + BASE_MODULE_NAME + "/src/main/res/values-sw800dp/dimens.xml";
            String w820file = "./" + BASE_MODULE_NAME + "/src/main/res/values-w820dp/dimens.xml";
            writeFile(sw320file, sw320.toString());
            writeFile(sw360file, sw360.toString());
            writeFile(sw480file, sw480.toString());
            writeFile(sw600file, sw600.toString());
            writeFile(sw720file, sw720.toString());
            writeFile(sw800file, sw800.toString());
            writeFile(w820file, w820.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(String file, String text) {
        String dir = file.substring(0, file.lastIndexOf("/"));
        File dirFile = new File(dir);
        if (!dirFile.exists())
            dirFile.mkdirs();

        PrintWriter out = null;
        try {
            out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.println(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.close();
    }

    public static void main(String[] args) {
        gen();
    }
}