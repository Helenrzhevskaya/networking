package homework8;

import java.io.IOException;
import java.sql.*;

public class PostgresExamples {

    public static void main(String[] args) {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            String usernane = "postgres";
            String password = "1234567";

            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5433/javacore", usernane, password);

            Statement statement = connection.createStatement();

            statement.executeUpdate("update students set name1 = 'Student1' where id = 1");

          ResultSet resultSet = statement.executeQuery("select * from students");

             while(resultSet.next()) {
             System.out.print(resultSet.getInt("id") + " ");
             System.out.print(resultSet.getString("name1")+ " ");
             System.out.print(resultSet.getInt("faculty_id") + "\n");
             }

            Long time = System.currentTimeMillis();
             connection.setAutoCommit(false);
             for (int i = 0; i < 10000; i++) {
                 statement.executeUpdate(String.format("insert into students (name1, score, faculty_id) values ('%s', %d, %d)",
                                                      "students" + i, i, i));
             }
             connection.commit();
            System.out.println("время вставки 1000 строк = " + (System.currentTimeMillis() - time));


//разкоментит
/**


            PreparedStatement preparedStatement = connection.prepareStatement
                    ("insert into students (name1, score, faculty_id) values (?, ?, ?)");
                    connection.setAutoCommit(false);
                    preparedStatement.setString(1,"SashaPrep");
                    preparedStatement.setInt(2, 100);
                    preparedStatement.setInt(3, 3);
                    preparedStatement.addBatch();


        /**ResultSet resultSet2 = statement.executeQuery("select * from students");

            while(resultSet2.next()) {
                System.out.println(resultSet2.getInt("id") + " ");
                System.out.println(resultSet2.getString("name1")+ " ");
                //System.out.print(resultSet2.getInt(("faculty_id") + " "));courses
            }
**/

//раскоментить

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();

        } finally {
            try {
                connection.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        }
    }


