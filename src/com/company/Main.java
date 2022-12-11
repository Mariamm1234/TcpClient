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
        Socket client = new Socket("localhost", 6698);
        for (int i = 0; i < num; i++) {
            System.out.print("Enter files' path you want to send : ");
            String path = sc.next();
            writeToServe = new DataOutputStream(client.getOutputStream());
            writeToServe.writeUTF(path);
//            writeToServe.writeUTF("Start\n");
//            String []type=path.split("\\.");
//            String t = (String) path.subSequence(path.indexOf(".") + 1, path.length());
            long startTime = System.currentTimeMillis();

            buff = new BufferedReader(new InputStreamReader(client.getInputStream()));
            System.out.println("connected successfully");

            input = new DataInputStream(client.getInputStream());
//            writeToServe.writeUTF(t);
            sendFile(path, client);
//            Runnable r = new Runnable() {
//                @Override
//                public void run() {
//                    try {
//
//                        getFromServer(client);
//
//                    } catch (Exception e) {
//
//                    }
//                }
//            };
//            r.run();
            fileTime.put(String.format("file number %s", i + 1), System.currentTimeMillis() - startTime);
        }
//   client.close();
        printTimeDetails(fileTime, num);
        client.close();
        writeToServe.close();
//        input.close();
//        writeToServe.close();
    }
    static void sendFile(String path, Socket socket) throws Exception {

//        File file = new File(path);
//        FileInputStream fis = new FileInputStream(file);
//        BufferedInputStream bis = new BufferedInputStream(fis);
//        //Get socket's output stream
//        OutputStream os = socket.getOutputStream();
////        writeToServe.writeUTF(path+"\n");
//        //Read File Contents into contents array
//        byte[] contents;
//        long fileLength = file.length();
//        long current = 0;
//        long start = System.nanoTime();
//        while (current != fileLength) {
//            int size = 10000;
//            if (fileLength - current >= size)
//                current += size;
//            else {
//                size = (int) (fileLength - current);
//                current = fileLength;
//            }
//            contents = new byte[size];
//            bis.read(contents, 0, size);
//            os.write(contents);
//            writeToServe.writeBytes("\nExit\n");
//            System.out.print("Sending file ... "+(current*100));
//        }
//        os.flush();
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
//            System.out.println("Sending " + filename + "(" + mybytearray.length + " bytes)");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            System.out.println("File Sent.");

        }
        finally {
            if (bis != null) bis.close();
            if (os != null) os.close();
            if (fis != null) fis.close();
            if (socket!=null) socket.close();
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
