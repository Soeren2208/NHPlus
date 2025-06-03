package de.hitec.nhplus.datastorage;

import de.hitec.nhplus.model.Caregiver;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class CaregiverDao extends DaoImp<Caregiver> {

    public CaregiverDao(Connection connection) {
        super(connection);
    }

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

    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        while (result.next()) {
            list.add(getInstanceFromResultSet(result));
        }
        return list;
    }

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
