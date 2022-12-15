package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static DataOutputStream writeToServe = null;
    static DataInputStream input = null;
    static BufferedReader buff = null;
    static Calendar now = Calendar.getInstance();
    static long Time = System.currentTimeMillis();
    public static void main(String[] args) throws Exception {
        Map<String, Long> fileTime = new HashMap<String, Long>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of files you want to send : ");
        int num = sc.nextInt();
        Socket client;
        for (int i = 0; i < num; i++) {
             client = new Socket("localhost", 6698);
            System.out.print("Enter files' path you want to send : ");
            String path = sc.next();
            writeToServe = new DataOutputStream(client.getOutputStream());
            writeToServe.writeUTF(path);
            long startTime = System.currentTimeMillis();

            buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("connected successfully");

            input = new DataInputStream(client.getInputStream());
            sendFile(path, client);
            fileTime.put(String.format("file number %s", i + 1), System.currentTimeMillis() - startTime);
            client.close();
            writeToServe.close();
        }
        printTimeDetails(fileTime, num);
        writeToServe.close();
    }
    static void sendFile(String path, Socket socket) throws Exception {
        System.out.println("Ready to send file");
        FileInputStream fis=null;
        BufferedInputStream bis=null;
        OutputStream os = null;
        try {
            // send file
            File myFile = new File (path);

            byte [] mybytearray  = new byte [(int)myFile.length()];

            fis = new FileInputStream(myFile);

            bis = new BufferedInputStream(fis);
            bis.read(mybytearray,0,mybytearray.length);
            os = socket.getOutputStream();
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            System.out.println("File Sent.");

        }
        finally {
        }
    }
    static void printTimeDetails(Map<String, Long> fileTime, int num) {
        for (Map.Entry<String, Long> me :
                fileTime.entrySet()) {
            System.out.print(me.getKey() + ":");
            System.out.println(me.getValue());
        }
        System.out.println("total time for " + num + " is = " + (System.currentTimeMillis() - Time));
        long tim = System.currentTimeMillis() - Time;
        System.out.println("total time for " + num + " is = " + tim);
        System.out.println("average time for " + num + " is = " + tim / num);
    }

}
