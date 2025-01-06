package edu.hsog.db;

import edu.hsog.db.DTOs.GadgetDTO;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class DBQueries {

    private static Connection con;
    private static Statement stm;
    private static ResultSet rs;

    private static PreparedStatement loginStm;
    private static ResultSet loginRs;

    private static PreparedStatement registerUserStm;

    private static Statement countStm;
    private static ResultSet countRs;

    private static ResultSet commentsRs;
    private static PreparedStatement commentsStm;

    private static ResultSet gadgetRs;
    private static PreparedStatement gadgetStm;

    private static ResultSet ratingRs;
    private static PreparedStatement ratingStm;

    private static PreparedStatement rateGadgetStm;

    private static PreparedStatement editRatingStm;

    private static PreparedStatement checkRatingStm;
    private static ResultSet checkRatingRs;

    private static ResultSet mostRatedGadgetRs;
    private static Statement mostRatedGadgetStm;

    private static PreparedStatement deletionStatement;

    private static PreparedStatement insertGadgetStm;


    static int count(){
        con = Globals.getPoolConnection();

        try {

            countStm = con.createStatement();
            String q = "SELECT COUNT(*) FROM GADGETS";

            countRs = countStm.executeQuery(q);
            countRs.next();

            return countRs.getInt(1);


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(countRs != null) countRs.close();
                if(countStm != null) countStm.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    static boolean userLoginData(String givenEmail, String givenPassword){
        con = Globals.getPoolConnection();

        try {

            String q = "SELECT * FROM USERS WHERE EMAIL=? and PASSWD =?";
            loginStm = con.prepareStatement(q);
            loginStm.setString(1, givenEmail);
            loginStm.setString(2, givenPassword);

            loginRs = loginStm.executeQuery();

            if(!loginRs.next()) return false;
            else return true;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(loginRs != null) loginRs.close();
                if(loginStm != null) loginStm.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean registerUser(String email, String password) {
        con = Globals.getPoolConnection();

        try {

            String q = "INSERT INTO USERS VALUES (?, ?)";
            registerUserStm = con.prepareStatement(q);
            registerUserStm.setString(1, email);
            registerUserStm.setString(2, password);

            int rowsAffected = registerUserStm.executeUpdate();


            if(rowsAffected>0) return true;
            else return false;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(registerUserStm != null) registerUserStm.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void loadGadgets() {
        try {
            con = Globals.getPoolConnection();
            stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String q = "SELECT * FROM GADGETS ORDER BY URL";
            rs = stm.executeQuery(q);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading gadgets: " + e.getMessage());
        }
    }

    public static GadgetDTO loadGadgetByUrl(String url){
        try {
            con = Globals.getPoolConnection();

            String q = "SELECT * FROM GADGETS WHERE URL = ?";
            gadgetStm = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            gadgetStm.setString(1, url);

            gadgetRs = gadgetStm.executeQuery();
            gadgetRs.next();


            String email = gadgetRs.getString("EMAIL");
            String keywords = gadgetRs.getString("KEYWORDS");
            String description = gadgetRs.getString("DESCRIPTION");
            byte[] image = gadgetRs.getBytes("COVER");

            return new GadgetDTO(url, email, keywords, description, image);


        } catch (SQLException e) {
            throw new RuntimeException("Error loading gadget by URL: " + e.getMessage());
        }
        finally {
            try {
                if (gadgetRs != null) gadgetRs.close();
                if (gadgetStm != null) gadgetStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static GadgetDTO loadRecord() {
        try {
            rs.next();
            String url = rs.getString("URL");
            String email = rs.getString("EMAIL");
            String keywords = rs.getString("KEYWORDS");
            String description = rs.getString("DESCRIPTION");
            byte[] image = rs.getBytes("COVER");

            return new GadgetDTO(url, email, keywords, description, image);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading record: " + e.getMessage());
        }
    }

    public static GadgetDTO moveFirst() {
        try {
            rs.first();


            String url = rs.getString("URL");
            String email = rs.getString("EMAIL");
            String keywords = rs.getString("KEYWORDS");
            String description = rs.getString("DESCRIPTION");
            byte[] image = rs.getBytes("COVER");


            return new GadgetDTO(url, email, keywords, description, image);
        } catch (SQLException e) {
            throw new RuntimeException("Error moving to first record: " + e.getMessage());
        }
    }

    // Move to the last record
    public static GadgetDTO moveLast() {
        try {
            rs.last();

            String url = rs.getString("URL");
            String email = rs.getString("EMAIL");
            String keywords = rs.getString("KEYWORDS");
            String description = rs.getString("DESCRIPTION");
            byte[] image = rs.getBytes("COVER");


            return new GadgetDTO(url, email, keywords, description, image);

        } catch (SQLException e) {
            throw new RuntimeException("Error moving to last record: " + e.getMessage());
        }
    }

    public static boolean isFirst(){
        try {
            return rs.isFirst();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static boolean isLast(){
        try {
            return rs.isLast();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static GadgetDTO nextGadget() {
        try {
            rs.next();

            String url = rs.getString("URL");
            String email = rs.getString("EMAIL");
            String keywords = rs.getString("KEYWORDS");
            String description = rs.getString("DESCRIPTION");
            byte[] image = rs.getBytes("COVER");


            return new GadgetDTO(url, email, keywords, description, image);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading record: " + e.getMessage());
        }
    }

    public static GadgetDTO previousGadget() {
        try {
            rs.previous();


            String url = rs.getString("URL");
            String email = rs.getString("EMAIL");
            String keywords = rs.getString("KEYWORDS");
            String description = rs.getString("DESCRIPTION");
            byte[] image = rs.getBytes("COVER");


            return new GadgetDTO(url, email, keywords, description, image);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading record: " + e.getMessage());
        }
    }


    public static ArrayList<String> getComments(String url){
        ArrayList<String> list = new ArrayList<>();

        try {
            con = Globals.getPoolConnection();


            String q = "SELECT KOMMENTAR FROM BEWERTUNG WHERE URL = ? ORDER BY EMAIL";
            commentsStm = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            commentsStm.setString(1, url);

            commentsRs = commentsStm.executeQuery();

            while(commentsRs.next()){
                list.add(commentsRs.getString(1));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Error loading commentsss: " + e.getMessage());
        }
        finally {
            try {
                if (commentsRs != null) commentsRs.close();
                if (commentsStm != null) commentsStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static double getRating(String url){

        try {
            con = Globals.getPoolConnection();


            String q = "SELECT AVG(GEFALLEN) FROM BEWERTUNG WHERE URL = ?";
            ratingStm = con.prepareStatement(q, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ratingStm.setString(1, url);

            ratingRs = ratingStm.executeQuery();

            ratingRs.next();
            return ratingRs.getDouble(1);

        } catch (SQLException e) {
            throw new RuntimeException("Error loading rating: " + e.getMessage());
        }
        finally {
            try {
                if (ratingRs != null) ratingRs.close();
                if (ratingStm != null) ratingStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static String mostRatedGadget() {
        try {
            con = Globals.getPoolConnection();

            String q = "WITH AveragedData AS (\n" +
                    "    SELECT URL, AVG(GEFALLEN) AS Average FROM\n" +
                    "    BEWERTUNG\n" +
                    "    GROUP BY URL\n" +
                    ")\n" +
                    "\n" +
                    "SELECT URL, Average FROM AveragedData\n" +
                    "WHERE Average=(SELECT MAX(Average) FROM AveragedData)";

            mostRatedGadgetStm = con.createStatement();

            mostRatedGadgetRs = mostRatedGadgetStm.executeQuery(q);
            mostRatedGadgetRs.next();

            return mostRatedGadgetRs.getString(1);



        } catch (SQLException e) {
            throw new RuntimeException("Error loading mostRatedGadget: " + e.getMessage());
        }
        finally {
            try {
                if (mostRatedGadgetRs != null) mostRatedGadgetRs.close();
                if (mostRatedGadgetStm != null) mostRatedGadgetStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }



    public static int deleteGadget(String url){
        try {
            con = Globals.getPoolConnection();

            deletionStatement = con.prepareStatement("DELETE FROM GADGETS WHERE URL = ?");
            deletionStatement.setString(1, url);

            return deletionStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting gadget: " + e.getMessage());
        }
        finally {
            try {
                if (rs != null) rs.close();
                if (deletionStatement != null) deletionStatement.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static int rateGadget(String email, String url, int rating, String comment){
        try {
            con = Globals.getPoolConnection();

            rateGadgetStm = con.prepareStatement("INSERT INTO BEWERTUNG VALUES (?, ?, ?, ?)");
            rateGadgetStm.setString(1, email);
            rateGadgetStm.setString(2, url);
            rateGadgetStm.setInt(3, rating);
            rateGadgetStm.setString(4, comment);

            return rateGadgetStm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while rating the gadget: " + e.getMessage());
        }
        finally {
            try {
                //TODO: ta mshelsh ose mo e mshelsh commentRs
                if (rateGadgetStm != null) rateGadgetStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static boolean checkRating(String email, String url){
        try {
            con = Globals.getPoolConnection();

            checkRatingStm = con.prepareStatement("SELECT * FROM BEWERTUNG WHERE EMAIL=? AND URL=?");
            checkRatingStm.setString(1, email);
            checkRatingStm.setString(2, url);

            checkRatingRs = checkRatingStm.executeQuery();
            if(checkRatingRs.next()){
                return true;
            }else {
                return false;
            }


        } catch (SQLException e) {
            throw new RuntimeException("Error while checking if the rating exists " + e.getMessage());
        }
        finally {
            try {
                if (checkRatingRs != null) checkRatingRs.close();
                if (checkRatingStm != null) checkRatingStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static int editRating(String email, String url, int rating, String comment){
        try {
            con = Globals.getPoolConnection();

            editRatingStm = con.prepareStatement("UPDATE BEWERTUNG SET GEFALLEN=?, KOMMENTAR=? WHERE EMAIL=? AND URL=?");
            editRatingStm.setInt(1, rating);
            editRatingStm.setString(2, comment);
            editRatingStm.setString(3, email);
            editRatingStm.setString(4, url);

            return editRatingStm.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while rating the gadget: " + e.getMessage());
        }
        finally {
            try {
                if (editRatingStm != null) editRatingStm.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException("Error closing resources: " + e.getMessage());
            }
        }
    }

    public static void insertGadget(String url, String email, String keywords, String description, Icon icon){
        con = Globals.getPoolConnection();

        try {

            String q = "INSERT INTO GADGETS VALUES (?, ?, ?, ?, ?)";
            insertGadgetStm = con.prepareStatement(q);
            insertGadgetStm.setString(1, url);
            insertGadgetStm.setString(2, email);
            insertGadgetStm.setString(3, keywords);
            insertGadgetStm.setString(4, description);

            Blob myImage = Converter.icon2Blob(icon, con);

            insertGadgetStm.setBlob(5, myImage);



            insertGadgetStm.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if(insertGadgetStm != null) insertGadgetStm.close();
                if(con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}



