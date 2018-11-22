
<%@page import="java.sql.*" %>

<html>
<body>
<%  class Warehouse {
             String getInformationForAllDatabase() throws SQLException{
               Connection connection = DriverManager
                       .getConnection("jdbc:mysql://localhost:3306/warehouse","root", "kimheehazelhasan11905");
               String getLots = "select * from lots;";
               String getGoods = "select * from goods;";
               String getTransactions = "select * from transactions;";
               String getAvailability = "select * from availability";
               String printSomething = "";

               Statement statement = connection.createStatement();
               printSomething+= "\nLOTS: \nID \t\tSIZE \t\tLOADABILITY\n";
               ResultSet rsLots = statement.executeQuery(getLots) ;{
                   while (rsLots.next()) {
                       printSomething += rsLots.getString("id") + "\t\t"
                               + rsLots.getString("size") + "\t\t\t\t"
                               + rsLots.getString("loadability") + "\t\t\n";
                   }
               }

               printSomething+="\n\nGoods:\nID \t PRICE\t WEIGHT \tSIZE\tNAME\n";
               ResultSet rsGoods = statement.executeQuery(getGoods) ;{
                   while (rsGoods.next()) {
                       printSomething +=rsGoods.getString("id") + "\t\t" +
                               rsGoods.getString("price")+ "\t\t" +
                               rsGoods.getString("weight")+ "\t\t" +
                               rsGoods.getString("size")+ "\t\t" +
                               rsGoods.getString("name")+ "\t\t\t\n";
                   }
               }
               printSomething+="\n\nTransactions:\nID \t  \t  DATE \t\t\t\t\t GOOD_ID \tQUANTITY \tTYPE\n";
               ResultSet rsTransactions = statement.executeQuery(getTransactions) ;{
                   while (rsTransactions.next()) {
                       printSomething +=rsTransactions.getString("id")+ "\t" +
                               rsTransactions.getString("dateOfTransaction")+ "\t" +
                               rsTransactions.getString("good_id")+ "\t" +
                               rsTransactions.getString("quantity")+ "\t" +
                               rsTransactions.getString("typeOfTransaction")+ "\t\n";
                   }
               }
               printSomething+="\nAVAILABILITY: \nID \t\tNUM OF GOODS \t\tGOOD ID\t    LOT ID\n";
               ResultSet rsAvailability = statement.executeQuery(getAvailability) ;{
                   while (rsAvailability.next()) {
                       printSomething +=rsAvailability.getString("id")+ "\t" +
                               rsAvailability.getString("numOfGoods")+ "\t" +
                               rsAvailability.getString("good_id")+ "\t" +
                               rsAvailability.getString("lot_id")+ "\t\n";
                   }
               }
               return printSomething;

           }

}%>
<%Warehouse wrh = new Warehouse();
out.println(wrh.getInformationForAllDatabase())
%>
</body>
</html>
