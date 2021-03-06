package org.presentation4you.resource_controller.server.Repository;

import java.sql.*;
import java.util.NoSuchElementException;

public class IntegrationTestsRepository implements IResourceRepo, IUserRepo {
    private Connection con = null;
    private Statement st = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    private String url = "jdbc:mysql://localhost:3306/test";
    final private String user = "pfu";
    final private String password = "pass";

    public IntegrationTestsRepository() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean has(int id) {
        boolean ret = false;
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("SELECT COUNT(resId) FROM resources WHERE" +
                    " resId=?;");
            pst.setInt(1, id);
            rs = pst.executeQuery();

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    ret = true;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return ret;
    }

    @Override
    public void add(int id, int typeId) {
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("INSERT INTO `resources`(resId, typeId) " +
                    "VALUES(?, ?);");

            pst.setInt(1, id);
            pst.setInt(2, typeId);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public void remove(int id) {
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("DELETE FROM `resources` WHERE resId=?;");

            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public int getType(String type) throws NoSuchElementException {
        int typeId = 0;
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("SELECT typeId FROM resource_types WHERE resource_type=?;");
            pst.setString(1, type);

            rs = pst.executeQuery();

            if (rs.next()) {
                typeId = rs.getInt(1);
            } else {
                throw new NoSuchElementException();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return typeId;
    }

    public void deleteAll() {
        try {
            con = DriverManager.getConnection(url, user, password);

            con.setAutoCommit(false);
            st = con.createStatement();

            st.addBatch("DELETE FROM resources");
            st.addBatch("DELETE FROM requests");

            st.executeBatch();
            con.commit();
        } catch (SQLException ex) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex1) {
                    System.out.println(ex1.getMessage());
                }
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public String getEmail(String login) {
        String email = null;
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("SELECT email FROM users WHERE login=?;");
            pst.setString(1, login);

            rs = pst.executeQuery();

            if (rs.next()) {
                email = rs.getString(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return email;
    }

    @Override
    public String authorize(String login, String pass) {
        String group = null;
        try {
            con = DriverManager.getConnection(url, user, password);

            pst = con.prepareStatement("SELECT g.`group` FROM `groups` g, `users` u " +
                    "WHERE u.`groupId`=g.`gId` AND u.`login`=? AND u.`pass`=?;");
            pst.setString(1, login);
            pst.setString(2, pass);

            rs = pst.executeQuery();

            if (rs.next()) {
                group = rs.getString(1);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return group;
    }
}
