package com.imooc.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;

import java.io.IOException;

public class BinlogServiceTest {

/*    WriteRowsEventData{tableId=70, includedColumns={0, 1, 2}, rows=[[13, 10, 丰田]]}
    UpdateRowsEventData{tableId=70, includedColumnsBeforeUpdate={0, 1, 2}, includedColumns={0, 1, 2}, rows=[{before=[13, 10, 丰田], after=[13, 10, 本田]}]}
    DeleteRowsEventData{tableId=70, includedColumns={0, 1, 2}, rows=[[13, 10, 本田]]}
*/
    public static void main(String[] args) throws IOException {
        BinaryLogClient client = new BinaryLogClient(
                "127.0.0.1",
                3306,
                "root",
                "root"
        );
        //client.setBinlogFilename();
        //client.setBinlogFilename("mysql-bin.000001");
        //client.setBinlogPosition();
        client.registerEventListener((Event event) -> {
            EventData data = event.getData();

            if(data instanceof UpdateRowsEventData){
                System.out.println("update.......");
                System.out.println(data.toString());
            }else if(data instanceof WriteRowsEventData){
                System.out.println("write..........");
                System.out.println(data.toString());
            }else if(data instanceof DeleteRowsEventData){
                System.out.println("Delete.........");
                System.out.println(data.toString());
            }
        });

        client.connect();
    }
}
