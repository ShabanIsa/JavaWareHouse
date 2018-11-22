package model;
import java.sql.*;
import java.util.Scanner;

public class WarehouseDAO {
    public WarehouseDAO() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("something");
        } catch (Exception ex) {
            System.out.println("something there");
        }
    }

    public static void main(String[] args) throws SQLException {
        WarehouseDAO warehouseDAO = new  WarehouseDAO();
        System.out.println(warehouseDAO.getInformationForAllDatabase());
    }
    public Connection connection()throws SQLException {
        System.out.println("-------- MySQL JDBC Connection Testing ------------");

        Connection connection = null;
        System.out.println("connection to database...");
        try {
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/warehouse","root", "iron_boy@2512114");

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
            return connection;
        }

        if (connection != null) {
            System.out.println("CONNECTED");
            return connection;
        } else {
            System.out.println("Can't connect to this database");
            return connection;
        }
    }

    public void getInformation(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. Information for whole database. ");
        System.out.println("2. Free Warehouse Capacity. ");
        System.out.println("3. Total price of all goods in the warehouse. ");
        System.out.println("4. AVG num of transactions per X days back.");
        System.out.println("5. Total price and total weight of all products. ");
        System.out.println("6. Reference for selected lot. ");
        System.out.println("7. Reference for selected good. ");
        System.out.println("8. Update the price of selected good.");
        System.out.println("9. Find good by name or id.");
        int choice = scanner.nextInt();
        switch (choice){
            case 1: getInformationForAllDatabase();
                break;
            case 2: freeWarehouseCapacity(connection);
                break;
            case 3: totalPriceOfAllGoods(connection);
                break;
            case 4:avg(connection);
                break;
            case 5: stockValue(connection);
                break;
            case 6: referenceForSelectedLot(connection);
                break;
            case 7: referenceForSelectedGood(connection);
                break;
            case 8: updateGoodPrice(connection);
                break;
            case 9: findByNameOrId(connection);
                break;
            default:
                System.out.println("illegal choice");
        }

    }

    public void freeWarehouseCapacity(Connection connection) throws SQLException{
        double allLotsSizes, allLotsAvailabilities, allLotsOccupiedSize, allLotsOccupiedAvailability;
        allLotsSizes = allLotsAvailabilities = allLotsOccupiedAvailability = allLotsOccupiedSize = 0;

        String getSumOfAllSizes = "select sum(lots.size)\n" +
                "from lots;";
        String getSumOfAllLoadability = "select sum(lots.loadability)\n" +
                "from lots;";
        String getSumOfAllOccupiedSize = "select sum(availability.numOfGoods*(goods.size))\n" +
                "from availability join goods\n" +
                "where goods.id = availability.good_id;";
        String getSumOfAllOccupiedLoadability = "select sum(availability.numOfGoods*(goods.weight))\n" +
                "from availability join goods\n" +
                "where goods.id = availability.good_id;";
        ResultSet resultSet;
        Statement statement = connection.createStatement();

        resultSet = statement.executeQuery(getSumOfAllSizes);
        if (resultSet.next())
            allLotsSizes=resultSet.getDouble(1);
        resultSet=statement.executeQuery(getSumOfAllLoadability);
        if (resultSet.next())
            allLotsAvailabilities = resultSet.getDouble(1);
        resultSet=statement.executeQuery(getSumOfAllOccupiedSize);
        if (resultSet.next())
            allLotsOccupiedSize = resultSet.getDouble(1);
        resultSet=statement.executeQuery(getSumOfAllOccupiedLoadability);
        if (resultSet.next())
            allLotsOccupiedAvailability = resultSet.getDouble(1);

        System.out.println("total free warehouse capacity: ");
        System.out.println("size = " + (allLotsSizes-allLotsOccupiedSize));
        System.out.println("Loadability = " + (allLotsAvailabilities - allLotsOccupiedAvailability));
        System.out.println("total occupied warehouse capacity: ");
        System.out.println("size = " + (allLotsOccupiedSize));
        System.out.println("Loadability = " + (allLotsOccupiedAvailability));

    }
    public String getInformationForAllDatabase() throws SQLException{
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
    public void totalPriceOfAllGoods (Connection connection)throws SQLException{
        String getTotalPrice = "select sum(availability.numOfGoods*(goods.price))\n" +
                "from availability join goods\n" +
                "where goods.id = availability.good_id;\n";
        double totalPrice = 0;
        ResultSet resultSet;
        Statement statement = connection.createStatement();

        resultSet = statement.executeQuery(getTotalPrice);
        if (resultSet.next())
            totalPrice=resultSet.getDouble(1);
        System.out.println("total price of all products in the warehouse: " + totalPrice);
    }
    public void avg(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter days: ");
        int days = scanner.nextInt();
        String getNumOfTransactions ="select count(transactions.id) \n" +
                "from transactions\n" +
                "where dateOfTransaction between adddate(now(), -" + days + ")\n"+
                "and now();";
        double avgTransactions = 0;
        ResultSet resultSet;
        Statement statement = connection.createStatement();

        resultSet = statement.executeQuery(getNumOfTransactions);
        if (resultSet.next())
            avgTransactions=resultSet.getDouble(1);
        avgTransactions/=days;
        System.out.println("avg transactions for " + days + " back = " + avgTransactions);
    }
    public void stockValue(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first date: <year-month-day> <hours:min:sec>");
        String date1 = "2018-02-02 00:00:00";
        System.out.println("Enter second date: <year-month-day> <hours:min:sec>");
        String date2 = "2018-04-12 00:00:00";

        String query = "select SUM(goods.price) \n" +
                "from goods join transactions\n" +
                "where (transactions.dateOfTransaction between '" + date1 +"' and '" + date2 + "')\n" +
                "and goods.id = transactions.good_id;";
        String query1 = "select SUM(goods.weight) \n" +
                "from goods join transactions\n" +
                "where (transactions.dateOfTransaction between '" + date1 +"' and '" + date2 + "')\n" +
                "and goods.id = transactions.good_id;";
        double allPrice = 0, allWeight=0;
        ResultSet resultSet;
        Statement statement = connection.createStatement();
        try {
            resultSet = statement.executeQuery(query);
            if (resultSet.next())
                allPrice = resultSet.getDouble(1);

            resultSet = statement.executeQuery(query1);
            if (resultSet.next())
                allWeight = resultSet.getDouble(1);

        }catch (SQLException ex){
            System.out.println("SQL Exception.");
        }

        System.out.println("Total price: " + allPrice + "\n Total weight: " + allWeight);
    }
    public void referenceForSelectedLot(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter lot id: ");
        int lot_id = scanner.nextInt();
        String getLotInformation = "select goods.name, availability.numOfGoods\n" +
                "from goods join availability join lots\n" +
                "where goods.id=availability.good_id and lots.id=availability.lot_id and\n" +
                "lots.id =" + lot_id + ";";

        Statement statement = connection.createStatement();
        ResultSet rsLots = statement.executeQuery(getLotInformation) ;{
            while (rsLots.next()) {
                System.out.printf("%s \t\t%s \n",
                        rsLots.getString("name"),
                        rsLots.getString("numOfGoods"));
            }
        }
    }
    public void referenceForSelectedGood(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter good id: ");
        int goodId = scanner.nextInt();
        double sumOfAllGoods=0;
        String getSumOfAllGoods = "select sum(availability.numOfGoods)\n" +
                "from goods join lots join availability\n" +
                "where goods.id=availability.good_id and lots.id=availability.lot_id and\n" +
                "goods.id = " + goodId + ";";
        String getInfoForGoods = "select goods.name, lots.id,availability.numOfGoods\n" +
                "from goods join lots join availability\n" +
                "where goods.id=availability.good_id and lots.id=availability.lot_id and\n" +
                "goods.id = " + goodId + ";";

        ResultSet resultSet;
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(getSumOfAllGoods);
        if (resultSet.next())
            sumOfAllGoods = resultSet.getDouble(1);
        System.out.println("Sum of all goods with id " + goodId + " is: " + sumOfAllGoods);
        System.out.println("The goods are in lots below: ");
        ResultSet rsLots = statement.executeQuery(getInfoForGoods) ;{
            System.out.println("\nNAME: \t\tLOT ID \t\tQUANTITY ");
            while (rsLots.next()) {
                System.out.printf("%s \t\t%s \t\t\t%s \n",
                        rsLots.getString("name"),
                        rsLots.getString("id"),
                        rsLots.getString("numOfGoods"));
            }
        }

    }
    public void updateGoodPrice(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter good id: ");
        int goodId = scanner.nextInt();
        System.out.println("Enter new price: ");
        double price = scanner.nextDouble();
        String query = "update goods \n" +
                "set price=" + price + "\n"+
                "where id=" + goodId + ";";
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public void findByNameOrId (Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("1. Find by id. ");
            System.out.println("2. Find by name. ");
            choice = scanner.nextInt();
        }while (choice>2||choice<1);

        if (choice==1){
            System.out.println("enter id: ");
            int id = scanner.nextInt();
            String getById = "select goods.name , goods.weight , goods.size \n" +
                    "from goods\n" +
                    "where goods.id ='" + id + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getById) ;
            {
                System.out.println("\nNAME: \t\tWEIGHT \t\tSIZE ");
                while (resultSet.next()) {
                    System.out.printf("%s \t\t%s \t\t\t%s \n",
                            resultSet.getString("name"),
                            resultSet.getString("weight"),
                            resultSet.getString("size"));
                }
            }
        }else {
            System.out.print("enter name: ");
            String name = scanner.next();

            String getByName = "select goods.name , goods.weight , goods.size \n" +
                    "from goods\n" +
                    "where goods.name ='" + name + "';";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getByName) ;
            {
                System.out.println("\nNAME: \t\tWEIGHT \t\tSIZE ");
                while (resultSet.next()) {
                    System.out.printf("%s \t\t%s \t\t\t%s \n",
                            resultSet.getString("name"),
                            resultSet.getString("weight"),
                            resultSet.getString("size"));
                }
            }

        }
    }

    public void createLotOrGood(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.print("1. Create Lot\n2. Create Good");
            choice = scanner.nextInt();
            if (choice<=0 || choice>2)
                System.out.println("illegal command!");
        }while (choice<1 || choice>2);

        if (choice==1)
            createLot(connection);
        else createGoods(connection);
    }
    public void createGoods(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Name of good: ");
        String name = scanner.nextLine();
        System.out.println("Enter weight of good: ");
        double weight = scanner.nextDouble();
        System.out.println("Enter size of good: ");
        double size = scanner.nextDouble();
        System.out.println("Enter price of good: ");
        double price = scanner.nextDouble();
        String query = "insert into goods values(null,'"+name+"',"+weight+","+size+","+price+");";
        System.out.println(query);
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
    }
    public void createLot(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserting Lot in warehouse: ");
        System.out.println("enter lot's size: ");
        double size = scanner.nextDouble();
        System.out.println("Enter lot's loadability: ");
        double loadability = scanner.nextDouble();
        Statement statement = connection.createStatement();
        statement.executeUpdate("insert into lots values (NULL,"+size+","+loadability+");");
    }

    public void createTransaction(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.print("1. Insert\n2. Export\n");
            choice = scanner.nextInt();
            if (choice<=0 || choice>2)
                System.out.println("illegal command!");
        }while (choice<1 || choice>2);

        if (choice==1)
            inport(connection);
        else export(connection);
    }
    public void inport(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println("1. Auto inport (the program will decide where to add the goods)");
            System.out.println("2. Manual inport");
            choice = scanner.nextInt();
            if (choice<=0 || choice>2)
                System.out.println("illegal command!");
        }while (choice<1 || choice>2);

        if (choice==1)
            autoInport(connection);
        else manualInport(connection);

    }
    public void export(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        int choice;
        do{
            System.out.println("1. Auto export (the program will decide from where to export the goods)");
            System.out.println("2. Manual export");
            choice = scanner.nextInt();
            if (choice<=0 || choice>2)
                System.out.println("illegal command!");
        }while (choice<1 || choice>2);

        if (choice==1)
            autoExport(connection);
        else manualExport(connection);
    }

    public void autoInport(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        double numOfLots, goodSize, goodWeight;
        numOfLots=goodSize=goodWeight=0;
        System.out.println("Enter the id of good: ");
        int goodId = scanner.nextInt();
        System.out.println("enter the quantity of good: ");
        int goodQuantity = scanner.nextInt();
        String getGoodSize = "select size from goods where goods.id = " + goodId + ";";
        String getGoodWeight = "select weight from goods where goods.id = " + goodId + ";";
        String getNumOfLots ="select count(lots.id) \n" +
                "from lots);";
        String getLotSize = "select size from lots;";


        Statement statement = connection.createStatement();
        ResultSet resultSet;

        resultSet = statement.executeQuery(getGoodSize);
        if (resultSet.next())
            goodSize=resultSet.getDouble(1);

        resultSet = statement.executeQuery(getGoodWeight);
        if (resultSet.next())
            goodWeight=resultSet.getDouble(1);

        resultSet = statement.executeQuery(getNumOfLots);
        if (resultSet.next())
            numOfLots=resultSet.getDouble(1);
        double lotSize=0;
        resultSet = statement.executeQuery(getLotSize);{
            while(resultSet.next());{
                lotSize=resultSet.getDouble(1);

            }
        }
        /*
           ResultSet rsAvailability = statement.executeQuery(getAvailability) ;{
            while (rsAvailability.next()) {
                System.out.printf("%s \t\t%s \t\t\t\t\t%s \t\t\t %s\n",
                        rsAvailability.getString("id"),
                        rsAvailability.getString("numOfGoods"),
                        rsAvailability.getString("good_id"),
                        rsAvailability.getString("lot_id"));
            }
        }*/

    }
    public void manualInport(Connection connection)throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        double lotSize, lotLoadability, lotOccupiedSize,lotOccupedLoadability,goodSize,goodWeight;
        lotSize = lotLoadability = lotOccupiedSize = lotOccupedLoadability = goodSize = goodWeight = 0;
        System.out.println("Enter good id: ");
        int goodId = scanner.nextInt();
        System.out.println("how much from this product do you want to insert?");
        int goodQuantity = scanner.nextInt();
        System.out.println("Enter lot id: ");
        int lotId = scanner.nextInt();
        String getGoodSize = "select size from goods where goods.id = " + goodId + ";";
        String getGoodWeight = "select weight from goods where goods.id = " + goodId + ";";
        String getLotSize = "select size from lots where lots.id = " + lotId + ";";
        String getLotLoadability = "select lots.loadability from lots where lots.id = " + lotId + ";";
        String getLotOccupiedSize = "select sum(g.size*tr.numOfGoods)\n" +
                "from availability tr\n" +
                "join goods g on tr.good_id = g.id\n" +
                "join lots l on tr.lot_id = l.id\n" +
                "where l.id =" + lotId +
                ";";
        String getLotOccupedLoadability = "select sum(g.weight*tr.numOfGoods)\n" +
                "from availability tr\n" +
                "join goods g on tr.good_id = g.id\n" +
                "join lots l on tr.lot_id = l.id\n" +
                "where l.id =" + lotId +
                ";";
        String getQuantityOfProductInTheLot = "select tr.numOfGoods\n" +
                "from availability tr\n" +
                "join goods g on tr.good_id = g.id\n" +
                "join lots l on tr.lot_id = l.id\n" +
                "where l.id = " + lotId + "\n" +
                "and g.id =" + goodId + ";";
        double goodQuantityInTheLot=0;
        ResultSet rsUpdate;
        Statement someStatement = connection.createStatement();
        rsUpdate = someStatement.executeQuery(getQuantityOfProductInTheLot);
        if (rsUpdate.next())
            goodQuantityInTheLot = rsUpdate.getDouble(1);

        String setGoodFromLot= "update availability\n" +
                "set numOfGoods =" +  (goodQuantity +  goodQuantityInTheLot) +
                " where lot_id = " + lotId +
                " and good_id = " + goodId + ";";

        Statement statement1 = connection.createStatement();
        ResultSet rs;

        rs = statement1.executeQuery(getLotSize);
        if (rs.next())
            lotSize=rs.getDouble(1);

        rs=statement1.executeQuery(getLotLoadability);
        if (rs.next())
            lotLoadability=rs.getDouble(1);

        rs=statement1.executeQuery(getLotOccupiedSize);
        if (rs.next())
            lotOccupiedSize=rs.getDouble(1);

        rs = statement1.executeQuery(getGoodSize);
        if (rs.next())
            goodSize=rs.getDouble(1);

        rs = statement1.executeQuery(getGoodWeight);
        if (rs.next())
            goodWeight=rs.getDouble(1);

        rs = statement1.executeQuery(getLotOccupedLoadability);
        if (rs.next())
            lotOccupedLoadability = rs.getDouble(1);

        boolean flag = false;
        double goodTotalSize = goodSize*goodQuantity;
        double goodTotalWeight = goodWeight*goodQuantity;
        System.out.println("LotSize = " + lotSize
                + "\tloadability = " + lotLoadability
                + "\n\toccupiedSize = " + lotOccupiedSize
                + "\toccupiedLoadability = " + lotOccupedLoadability
                + "\n ClassPackage.Good size = " + goodSize
                + "\t ClassPackage.Good weight = "+ goodWeight);
        String query = "insert into transactions values (NULL,now()," + goodId + "," + goodQuantity + ",'Inport');";
        double freeSize, freeLoadability;
        freeSize = lotSize - lotOccupiedSize;
        freeLoadability = lotLoadability - lotOccupedLoadability;
        if (goodQuantityInTheLot!=0 && freeSize>=goodTotalSize && freeLoadability>= goodTotalWeight){
            statement1.executeUpdate(setGoodFromLot);
            statement1.executeUpdate(query);
            flag=true;
        }
        if (freeSize>=goodTotalSize && freeLoadability>= goodTotalWeight && flag==false){

            String query2 = "insert into availability values (NULL, " + goodQuantity + "," + goodId + "," + lotId + ");";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query2);
            statement.executeUpdate(query);
            System.out.println("success\n In lot " + lotId + "were entered" + goodQuantity  + "from good with id: " + goodId);
        }else if (flag==false)System.err.println("there is no free space or loadability");

    }

    public void autoExport(Connection connection) throws SQLException{
        System.out.println("auto Export");
    }
    public void manualExport(Connection connection) throws SQLException{
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter the id of the good which you want to export: ");
        int goodId = scanner.nextInt();
        System.out.println("enter the id of the lot from which you want to export");
        int lotId = scanner.nextInt();
        System.out.println("enter the quantity - you want to export: ");
        double goodQuantityToExport = scanner.nextInt();
        double goodQuantity = 0;
        Statement statement1 = connection.createStatement();
        ResultSet rs;
        String getQuantityOfProductInTheLot = "select tr.numOfGoods\n" +
                "from availability tr\n" +
                "join goods g on tr.good_id = g.id\n" +
                "join lots l on tr.lot_id = l.id\n" +
                "where l.id = " + lotId + "\n" +
                "and g.id =" + goodId + ";";
        Statement statement = connection.createStatement();
        rs = statement1.executeQuery(getQuantityOfProductInTheLot);
        if (rs.next())
            goodQuantity = rs.getDouble(1);

        System.out.println("GQ = "+(int)goodQuantity);
        System.out.println("Export = "+(int)goodQuantityToExport);
        String setGoodFromLot= "update availability\n" +
                "set numOfGoods =" +  (goodQuantity -  goodQuantityToExport) +
                " where lot_id = " + lotId +
                " and good_id = " + goodId + ";";

        String delete = "delete from availability\n" +
                "where numOfGoods=0;";
        if (goodQuantity >= goodQuantityToExport){
            statement.executeUpdate(setGoodFromLot);
            String query = "insert into transactions values (NULL,now()," + goodId + "," + goodQuantity + ",'Export');";
            statement.executeUpdate(query);

            System.out.println("Export Success");
        }else System.out.println("failed... FUCK");

        statement.executeUpdate(delete);
    }

    public void deleteLotGood(Connection connection) throws SQLException{
        System.out.println("DELETE");
    }
    public void menu(){
        System.out.println("\t\t\t\tWAREHOUSE database Menu:");
        System.out.println("1. Get information from table.");
        System.out.println("2. Create transaction.");
        System.out.println("3. Create lot/good");
        System.out.println("4. Delete lot/good");
        System.out.println("0. Exit");

    }
}

