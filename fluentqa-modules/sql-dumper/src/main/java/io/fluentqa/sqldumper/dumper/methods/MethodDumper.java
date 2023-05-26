package io.fluentqa.sqldumper.dumper.methods;


import io.fluentqa.sqldumper.dumper.Dumper;
import io.fluentqa.sqldumper.utilities.SqlCommentUtility;
import io.fluentqa.sqldumper.utilities.SqlQueryUtility;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Abstract class, used to dump functions, procedures,
 * triggers and events.
 *
 * @author Elyar Adil
 * @since 1.0
 */
public abstract class MethodDumper extends Dumper {
    /**
     * Constructor
     *
     * @param connection  set connection of dumper
     * @param printWriter set printWriter of dumper
     */
    public MethodDumper(Connection connection, PrintWriter printWriter) {
        super(connection, printWriter);
    }

    /**
     * Dump target entity to printWriter
     *
     * @param targetName name of the target
     * @throws SQLException if a database access error occurs
     */
    @Override
    public abstract void dump(String targetName) throws SQLException;

    /**
     * Dump method.
     *
     * @param methodName      name of the method
     * @param commentTemplate used to add comment before dump
     * @param dropTemplate    used to dump drop statement
     * @param createTemplate  template to get create statement form
     *                        the database
     * @param createIndex     which column the create statement lays
     * @throws SQLException if a database access error occurs
     */
    public void dump(String methodName, String commentTemplate, String dropTemplate, String createTemplate, int createIndex) throws SQLException {
        // comment before dump
        String structureHeadComment = String.format(commentTemplate, methodName);
        SqlCommentUtility.printCommentHeader(printWriter, structureHeadComment);
        // dump drop statement
        String dropSql = String.format(dropTemplate, methodName);
        printWriter.println(dropSql);
        // dump create statement
        String doubleDelimiter = SQL_DELIMITER + SQL_DELIMITER;
        printWriter.println("delimiter " + doubleDelimiter);
        String sql = String.format(createTemplate, methodName);
        printWriter.println(SqlQueryUtility.queryString(this.connection, sql, createIndex) + SQL_DELIMITER);
        printWriter.println(doubleDelimiter);
        printWriter.println("delimiter " + SQL_DELIMITER);
        printWriter.println();

        printWriter.flush();
    }
}
