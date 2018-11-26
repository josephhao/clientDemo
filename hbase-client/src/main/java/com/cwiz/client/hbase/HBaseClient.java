package com.cwiz.client.hbase;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.apache.htrace.fasterxml.jackson.core.JsonProcessingException;
import org.apache.htrace.fasterxml.jackson.databind.ObjectMapper;
import org.apache.htrace.fasterxml.jackson.databind.ObjectWriter;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * knowledge :
 * 1. even if the hbase data is stored in the local place
 *    with the parameter {hbase.rootdir}:"file:///Users/cwiz/hbase/data",
 *    we still need the compatible hadoop jar to perform
 *    the function of storing.
 *
 */

public class HBaseClient implements Closeable {

    protected Connection hbaseConnection;
    protected Configuration hbaseConfig;

    protected String tableName = "javaClient";
    protected String cf_create = "create";
    protected String cf_delete = "delete";



    {
        this.hbaseConfig = HBaseConfiguration.create();

//        hbaseConfig.set("hbase.zookeeper.quorum", "192.168.8.104");
//        hbaseConfig.set("hbase.zookeeper.property.clientPort", "2181");
        if (System.getProperty("HBASE_CONF_DIR") != null) {
            hbaseConfig.addResource(new Path(System.getProperty("HBASE_CONF_DIR"), "hbase-site.xml"));
        } else {
            hbaseConfig.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
//            logger.info("HBASE_CONF_DIR : {}", System.getenv("HBASE_CONF_DIR"));
            File p = new File(System.getenv("HBASE_CONF_DIR")+"/"+ "hbase-site.xml");
//            logger.info("HBASE_CONF_DIR : file {}", p.exists() );
        }

        if (System.getProperty("HDFS_CONF_DIR") != null) {
            hbaseConfig.addResource(new Path(System.getProperty("HDFS_CONF_DIR"), "core-site.xml"));
        } else
            if(System.getenv("HDFS_CONF_DIR")!=null){
            hbaseConfig.addResource(new Path(System.getenv("HDFS_CONF_DIR"), "core-site.xml"));
        }

        try {
            this.hbaseConnection  = ConnectionFactory.createConnection(this.hbaseConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HBaseClient(){

    }

    public void createTable(String tableName,Configuration conf, String... columnFamilyNames) {
        System.out.println("start create table "+tableName);
        try {

            HBaseAdmin hBaseAdmin = new HBaseAdmin(conf);
            if (hBaseAdmin.tableExists(tableName)) {
                System.out.println(tableName + " already exists");
                //hBaseAdmin.disableTable(tableName);
                //hBaseAdmin.deleteTable(tableName);
                return;
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
            for(int i=0; i<columnFamilyNames.length; i++) {
                tableDescriptor.addFamily(new HColumnDescriptor( columnFamilyNames[i]));
            }
            hBaseAdmin.createTable(tableDescriptor);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("end create table "+tableName);
    }

    public TableName getTableName(String tableName){
        return TableName.valueOf(tableName);
    }

    public Table getTable(TableName tableName){
        try {
            return this.hbaseConnection.getTable(tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void doPut(String tableName, String rowkey, Map<String, Object> keyValues) throws IOException {

        Table table = this.getTable(getTableName(tableName));
        Put put = generatePut(rowkey, keyValues);
        table.put(put);
    }


    public Put generatePut(String rowkey, Map<String, Object> keyValues){
        Put put = new Put(Bytes.toBytes(rowkey));
        ObjectMapper mapper = new ObjectMapper();
        for(Map.Entry<String,Object> entry : keyValues.entrySet()){
            String[] cols = entry.getKey().split(":");
            try {
                String value  = mapper.writeValueAsString( entry.getValue());
                put.addColumn(Bytes.toBytes(cols[0]), Bytes.toBytes(cols[1]), Bytes.toBytes( value ));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        }

        //System.out.println(put.toString());
        return put;
    }

    public Put generatePut(String rowkey, String columnFamily, Map<String, String> keyValues){
        Put put = new Put(Bytes.toBytes(rowkey));

        for(Map.Entry<String,String> entry : keyValues.entrySet()){
            put.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(entry.getKey()), Bytes.toBytes(entry.getValue()));
        }

        //System.out.println(put.toString());
        return put;
    }

    public boolean isTableExists(TableName tableName) throws IOException {
        boolean result = false;

        Admin admin = this.hbaseConnection.getAdmin();
        return admin.tableExists(tableName);
    }

    public Result doGet(String tableName , String rowkey){
        Get get = new Get(Bytes.toBytes(rowkey));
        get.addFamily(Bytes.toBytes(cf_create));
        Table table = getTable(getTableName(tableName));
        Result result = null;
        try {
            result = table.get(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
        try {
            System.out.println(writer.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Result doVersionedGet(String tableName , byte[] rowkey, byte[] cf, byte[] qualifier, int versions) throws IOException{
        Get get = new Get(rowkey);
        get.setMaxVersions(versions); // will return last 3 versions of row
        Table table = getTable(getTableName(tableName));
        Result r = table.get(get);
//        byte[] b = r.getValue(cf, qualifier);          // returns current version of value
//        List<Cell> kv = r.getColumnCells(cf, qualifier); // returns all versions of this column

        return r;
    }

    public void doScan(String tableName ,byte[] rowPrefix, byte[] cf, byte[] qualifier) throws IOException{
        Table table = this.getTable(getTableName(tableName));
        Scan scan = new Scan();
        scan.addColumn(cf, qualifier);

        scan.setRowPrefixFilter(rowPrefix);
        ResultScanner rs = table.getScanner(scan);
        try {
            for (Result r = rs.next(); r != null; r = rs.next()) {
                formatResult(r);
            }
        }
        finally {
            rs.close(); // always close the ResultScanner!
        }
    }


    public void formatResult( Result result){
        Cell[] cells = result.rawCells();
        System.out.println();
        System.out.println(result.toString());
        if(result.isEmpty()) {

            for (Cell cell : cells) {
                System.out.println(Bytes.toString(CellUtil.cloneRow(cell)) + " , "
                        + Bytes.toString(CellUtil.cloneFamily(cell)) + " , "
                        + Bytes.toString(CellUtil.cloneQualifier(cell)) + " , "
                        + Bytes.toString(CellUtil.cloneValue(cell)) + " , "
                        + cell.getTimestamp());
            }
            //        byte[] b = r.getValue(cf, qualifier); // returns current version of value
            //        List<Cell> kv = r.getColumnCells(cf, qualifier); // returns all versions of this column

        }
    }


    public void close() {
        try {
            this.hbaseConnection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HBaseClient hBaseClient = new HBaseClient();
        hBaseClient.createTable(hBaseClient.tableName,
                hBaseClient.hbaseConfig ,
                hBaseClient.cf_create,
                hBaseClient.cf_delete);
        Map<String, Object>  keyValue = new HashMap<String, Object>();
        keyValue.put(hBaseClient.cf_create+":c1", "v1");
        keyValue.put(hBaseClient.cf_create+":c2", "v2");
        String rowkey = "row1";
        try {
            hBaseClient.doPut(hBaseClient.tableName , rowkey, keyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Result result= hBaseClient.doGet(hBaseClient.tableName, rowkey);
        System.out.println();
        System.out.println("result :" + result );

        System.out.println(Bytes.toString(result.getRow()));
        System.out.println("rawCells");

        hBaseClient.formatResult(result);
//        CellScanner scanner = CellUtil.createCellScanner(cells);

    }
}
