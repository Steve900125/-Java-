
import java.sql.*;
import java.util.Random;
import java.time.LocalDateTime;   
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter; // Import the DateTimeFormatter class
import math.mathModel;
import math.ParetoRNG;
import java.math.BigDecimal;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

class Run{
    static int runTimes;
    static int sleep;
    Run(){
        runTimes = 30000;
        sleep = 3000;
    }
    Run(int runTimes, int sleep){
        this.runTimes = runTimes;
        this.sleep = sleep;
    }    
}

/**
 * 
 * @author USER
 */
class SqlSever{
    //============================================================
    static public String hostname = "140.128.109.115";
    //所要連線主機的ip位置
    static public String database = "test";
    //所選用的資料庫名稱JavaRandomDB
    static public String userName = "sa";
    //使用者名稱
    static public String password ="thuim109project";
    //使用者密碼
    static public String table = "[Test 1 of 7]" ;
    //Machine1
    //============================================================
    //SQL sever 資料連線設定
    SqlSever(){
        hostname = "140.128.109.115";
        database ="JavaRandomDB";
        userName = "sa";
        password = "s08490043";
        table = "Machine1";
    }
    //建構子會給予初始值預設的資料
    
    public static void showInfor(){
        System.out.println("1.hostname(IP):" + hostname); 
        System.out.println("2.database name:" + database);
        System.out.println("3.table name:" + table);
        System.out.println("4.user name:" + userName);
        System.out.println("5.password:" + password);
        
    }
    //顯示出當前sql Sever 資料
    
    public static String getUrl(){
        String urlSet;
        urlSet = String.format("jdbc:sqlserver://%s:1433;database=%s;",hostname,database);
        return urlSet;
        //為了讓JDBC連接至指定資料庫的字串
    }
    
    //能夠建立一個資料表
    public static void creatTable(String name){      
        String tableName = "Machine" + name;
        String column1 ="ID INT identity(1,1)  ,";
        String column2 ="DeviceId nvarchar(50) ,";
        String column3 ="DeviceValue nvarchar(MAX) ,";
        String column4 ="UpdateTime datetime ,";
        String column5 ="PRIMARY KEY (ID)";             
        try (Connection conn = DriverManager.getConnection(SqlSever.getUrl(), SqlSever.userName, SqlSever.password);) 
        {
            //Statement stmt = conn.createStatement();
            String query = "CREATE TABLE "+ tableName +"("
                    + column1
                    + column2
                    + column3
                    + column4
                    + column5 + ")";          
            PreparedStatement pstmt = conn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS);          
            pstmt.execute();
            //stmt.executeUpdate(query);
            System.out.println("Create sucesse Table name is:"+ tableName);
            table = tableName;
            showInfor();        
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }       
    }
    

}

class Machine{
    int id;
    //資料的id自動編號，如果  
    String deviceId;
    //機器的名稱    
    Map<String, Object> deviceValues= new HashMap<>();
    //機器的詳細資料，有數字跟字串資料所以採用多型的value    
    Timestamp time;
    //資料所產生的時間
    
    Machine(){
        this.deviceValues.put("DeviceTypeId",0);
        this.deviceValues.put("StatusId",0);
        this.deviceValues.put("ActStatus","");
        this.deviceValues.put("ActAlarmMsg","");
        this.deviceValues.put("ActMainProgramName","");
        this.deviceValues.put("ActMainProgramNote","");
        this.deviceValues.put("ActPartCount",0);
        this.deviceValues.put("Profit",0);
        this.deviceValues.put("ProductionRequiredQty",0);
        this.deviceValues.put("ProductionRequiredQtyTotal",0);
        this.time = new Timestamp(System.currentTimeMillis());  
        this.deviceId = "VirtulMachine1";
    } 
    
    //回傳DeviceValues 字串的sql欄位單格資料格式
    //為了完全符合格式所以手動刻模板，事實上我們DeviceValues這個map 能夠用entrySet() 返回map內的所有key和value值。
    String getDeviceValuesString(){
        String dvSql = ""; 
        dvSql += String.format(
                "{\"DeviceTypeId\":%s,"
                +"\"StatusId\":%s,"
                +"\"ActStatus\":\"%s\","
                +"\"ActAlarmMsg\":\"%s\","
                +"\"ActMainProgramName\":\"%s\","
                +"\"ActMainProgramNote\":\"%s\","
                +"\"ActPartCount\":%s,"
                +"\'Profit\': %s,"//老子是單引號歐
                +"\"ProductionRequiredQty\":%s,"
                +"\"ProductionRequiredQtyTotal\":%s}",
                this.deviceValues.get("DeviceTypeId").toString(),
                this.deviceValues.get("StatusId").toString(),
                this.deviceValues.get("ActStatus"),
                this.deviceValues.get("ActAlarmMsg"),
                this.deviceValues.get("ActMainProgramName"),
                this.deviceValues.get("ActMainProgramNote").toString(),
                this.deviceValues.get("ActPartCount").toString(),
                this.deviceValues.get("Profit").toString(),
                this.deviceValues.get("ProductionRequiredQty").toString(),
                this.deviceValues.get("ProductionRequiredQtyTotal").toString()
        );
        return dvSql;
    }
    
}

//主程式
public class ConnectDB
{   
    public static void main(String[] args){
        try{                                             
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            //DriverManager：負責加載各種不同驅動程序（Driver），並根據不同的請求，向調用者返回相應的數據庫連接（Connection）。
            //Driver：驅動程序，會將自身加載到DriverManager中去，並處理相應的請求並返回相應的數據庫連接（Connection）。            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            //利用Class.forName()方法來加載JDBC驅動程序（Driver）至DriverManager
            Run r = new Run();
            while(true){
                for(int i = 0 ; i < Run.runTimes; i++)
                {
                    insertData();
                    Thread.sleep(r.sleep/10);                     
                }
                System.out.println("Success ! ");
                deleteTest();
                //結束後刪除具現在時間三天前的資料
            }   
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
static public Connection connect() throws SQLException {
    //SqlSever sql = new SqlSever();         
    //建構SQL資料物件        
    return DriverManager.getConnection(SqlSever.getUrl(), SqlSever.userName, SqlSever.password);
    //Connection conn = DriverManager.getConnection(sql.getUrl(), sql.userName, sql.password);
    //從DriverManager中，通過JDBC URL，用戶名，密碼來獲取相應的資料庫連接（Connection)
}
    
static public void deleteTest(){
        //刪除距離現在三天前的資料
        String sql ="DELETE FROM "+ SqlSever.table +" WHERE "
                + "UpdateTime < ?";
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {  
            LocalDateTime myDateObj = LocalDateTime.now();
            myDateObj = myDateObj.minusDays(3);
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);              
            pstmt.setString(1, formattedDate);
            pstmt.execute(); 
            conn.close();
            pstmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
}
static public void insertData(){
    //產生資料庫物件讀取資料    
    String sql ="INSERT INTO "+ SqlSever.table +"(DeviceId , DeviceValue , UpdateTime) "
                + "VALUES(?,?,?)";
    //插入的SQL語法   
    Machine ma = new Machine();
    //建立Machine物件  
    ma = machineOuputData();
    //將機器的資料傳入物件中
    try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {           
            Timestamp time= new Timestamp(System.currentTimeMillis());                     
             System.out.printf("INSERT INTO "+ SqlSever.table +"(DeviceId , DeviceValue , UpdateTime) "
                + "VALUES(%s,%s,%s)\n",ma.deviceId ,ma.getDeviceValuesString() ,ma.time.toString());     
            pstmt.setString(1, ma.deviceId);
            pstmt.setString(2, ma.getDeviceValuesString());
            pstmt.setString(3, ma.time.toString());
            pstmt.execute();           
            conn.close();
            pstmt.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
}


static public Machine machineOuputData(){
        Random rand = new Random();
        //產生亂數      
        Machine machine = new Machine();        
        ParetoRNG pr = new ParetoRNG(rand,2,0.01,2.5);
        double temp =  pr.getDouble()*1000;
        BigDecimal bg=new BigDecimal(temp+"");
        //==============================================================================================
        machine.deviceValues.replace("DeviceTypeId", 1);
        machine.deviceValues.replace("StatusId", 1);
        machine.deviceValues.replace("ActStatus", "ON");
        machine.deviceValues.replace("ActAlarmMsg", "[15][1001] EMERGENCY STOP");
        machine.deviceValues.replace("ActMainProgramName", "O" + String.valueOf(rand.nextInt(1000)));
        machine.deviceValues.replace("ActMainProgramNote", "");
        machine.deviceValues.replace("ActPartCount",rand.nextInt(10));
        machine.deviceValues.replace("Profit", bg) ;
        machine.deviceValues.replace("ProductionRequiredQty",rand.nextInt(10));
        machine.deviceValues.replace("ProductionRequiredQtyTotal",rand.nextInt(100));
        //==============================================================================================
        //這裡負責產生亂數或機器要的資料
        return machine;
    }
}
