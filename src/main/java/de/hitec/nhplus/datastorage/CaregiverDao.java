package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Caregiver;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * Implements the Interface <code>DaoImp</code>. Overrides methods to generate specific <code>PreparedStatements</code>,
 * to execute the specific SQL Statements.
 */
public class CaregiverDao extends DaoImp<Caregiver> {

    /**
     * The constructor initializes an object of <code>CaregiverDao</code> and passes the connection to its super class.
     *
     * @param connection Object of <code>Connection</code> to execute the SQL-statements.
     */
    public CaregiverDao(Connection connection) {
        super(connection);
    }

    /**
     * Generates a <code>PreparedStatement</code> to query a caregiver by a given caregiver id (cid).
     *
     * @param cid Caregiver id to query.
     * @return <code>PreparedStatement</code> to query the caregiver.
     */
    @Override
    protected PreparedStatement getReadByIDStatement(long cid) {
        try {
            final String SQL = "SELECT * FROM caregiver WHERE cid = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cid);
            return preparedStatement;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Maps a <code>ResultSet</code> of one caregiver to an object of <code>Caregiver</code>.
     *
     * @param result ResultSet with a single row. Columns will be mapped to an object of class <code>Caregiver</code>.
     * @return Object of class <code>Caregiver</code> with the data from the resultSet.
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet result) throws SQLException {
        long cid = result.getLong("cid");
        String firstName = result.getString("firstname");
        String surname = result.getString("surname");
        String phoneNumber = result.getString("phonenumber");

        long millis = result.getLong("inactiveSince");
        LocalDate inactiveSince = (millis > 0)
                ? Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                : null;

        return new Caregiver(cid, firstName, surname, phoneNumber, inactiveSince);
    }

    /**
     * Maps a <code>ResultSet</code> of all caregivers to an <code>ArrayList</code> of <code>Caregiver</code> objects.
     *
     * @param result ResultSet with all rows. The columns will be mapped to objects of class <code>Caregiver</code>.
     * @return <code>ArrayList</code> with objects of class <code>Caregiver</code> for all rows in the <code>ResultSet</code>.
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        while (result.next()) {
            list.add(getInstanceFromResultSet(result));
        }
        return list;
    }

    /**
     * Generates a <code>PreparedStatement</code> to persist the given object of <code>Caregiver</code>.
     *
     * @param caregiver Object of <code>Caregiver</code> to persist.
     * @return <code>PreparedStatement</code> to insert the given caregiver.
     */
    @Override
    protected PreparedStatement getCreateStatement(Caregiver caregiver) {
        try {
            final String SQL = "INSERT INTO caregiver(firstname, surname, phonenumber, inactiveSince) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getPhoneNumber());
            if (caregiver.getInactiveSince() != null) {
                preparedStatement.setDate(4, Date.valueOf(caregiver.getInactiveSince()));
            } else {
                preparedStatement.setNull(4, Types.DATE);
            }
            return preparedStatement;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a <code>PreparedStatement</code> to query all caregivers.
     *
     * @return <code>PreparedStatement</code> to query all caregivers.
     */
    @Override
    protected PreparedStatement getReadAllStatement() {
        try {
            final String SQL = "SELECT * FROM caregiver";
            return this.connection.prepareStatement(SQL);
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a <code>PreparedStatement</code> to update the given caregiver, identified
     * by the id of the caregiver (cid).
     *
     * @param caregiver Caregiver object to update.
     * @return <code>PreparedStatement</code> to update the given caregiver.
     */
    @Override
    protected PreparedStatement getUpdateStatement(Caregiver caregiver) {
        try {
            final String SQL =
                    "UPDATE caregiver SET " +
                            "firstname = ?, " +
                            "surname = ?, " +
                            "phonenumber = ?, " +
                            "inactiveSince = ? " +
                            "WHERE cid = ?";

            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setString(1, caregiver.getFirstName());
            preparedStatement.setString(2, caregiver.getSurname());
            preparedStatement.setString(3, caregiver.getPhoneNumber());
            if (caregiver.getInactiveSince() != null) {
                preparedStatement.setDate(4, Date.valueOf(caregiver.getInactiveSince()));
            } else {
                preparedStatement.setNull(4, Types.DATE);
            }
            preparedStatement.setLong(5, caregiver.getCid());

            return preparedStatement;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }

    /**
     * Generates a <code>PreparedStatement</code> to delete a caregiver with the given id.
     *
     * @param cid Id of the caregiver to delete.
     * @return <code>PreparedStatement</code> to delete caregiver with the given id.
     */
    @Override
    protected PreparedStatement getDeleteStatement(long cid) {
        try {
            final String SQL = "DELETE FROM caregiver WHERE cid = ?";
            PreparedStatement preparedStatement = this.connection.prepareStatement(SQL);
            preparedStatement.setLong(1, cid);
            return preparedStatement;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
