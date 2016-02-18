package de.micromata.genome.db.jdbc.wrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * Wrapper to Statement.
 *
 * @author roger
 */
public class StatementWrapper implements Statement
{

  /**
   * The nested statement.
   */
  protected Statement nestedStatement;

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isClosed() throws SQLException
  {
    return nestedStatement.isClosed();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isPoolable() throws SQLException
  {
    return nestedStatement.isPoolable();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException
  {
    return nestedStatement.isWrapperFor(iface);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setPoolable(boolean poolable) throws SQLException
  {
    nestedStatement.setPoolable(poolable);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException
  {
    return nestedStatement.unwrap(iface);
  }

  /**
   * Instantiates a new statement wrapper.
   */
  public StatementWrapper()
  {

  }

  /**
   * Instantiates a new statement wrapper.
   *
   * @param nestedStatement the nested statement
   */
  public StatementWrapper(Statement nestedStatement)
  {
    this.nestedStatement = nestedStatement;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void addBatch(String sql) throws SQLException
  {
    nestedStatement.addBatch(sql);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void cancel() throws SQLException
  {
    nestedStatement.cancel();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void clearBatch() throws SQLException
  {
    nestedStatement.clearBatch();

  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void clearWarnings() throws SQLException
  {
    nestedStatement.clearWarnings();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void close() throws SQLException
  {
    nestedStatement.close();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean execute(String sql) throws SQLException
  {
    return nestedStatement.execute(sql);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
  {
    return nestedStatement.execute(sql, autoGeneratedKeys);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean execute(String sql, int[] columnIndexes) throws SQLException
  {
    return nestedStatement.execute(sql, columnIndexes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean execute(String sql, String[] columnNames) throws SQLException
  {
    return nestedStatement.execute(sql, columnNames);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int[] executeBatch() throws SQLException
  {
    return nestedStatement.executeBatch();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public ResultSet executeQuery(String sql) throws SQLException
  {
    return nestedStatement.executeQuery(sql);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int executeUpdate(String sql) throws SQLException
  {
    return nestedStatement.executeUpdate(sql);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
  {
    return nestedStatement.executeUpdate(sql, autoGeneratedKeys);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
  {
    return nestedStatement.executeUpdate(sql, columnIndexes);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int executeUpdate(String sql, String[] columnNames) throws SQLException
  {
    return nestedStatement.executeUpdate(sql, columnNames);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public Connection getConnection() throws SQLException
  {
    return nestedStatement.getConnection();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getFetchDirection() throws SQLException
  {
    return nestedStatement.getFetchDirection();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getFetchSize() throws SQLException
  {
    return nestedStatement.getFetchSize();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public ResultSet getGeneratedKeys() throws SQLException
  {
    return nestedStatement.getGeneratedKeys();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getMaxFieldSize() throws SQLException
  {
    return nestedStatement.getMaxFieldSize();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getMaxRows() throws SQLException
  {
    return nestedStatement.getMaxRows();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean getMoreResults() throws SQLException
  {
    return nestedStatement.getMoreResults();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean getMoreResults(int current) throws SQLException
  {
    return nestedStatement.getMoreResults(current);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getQueryTimeout() throws SQLException
  {
    return nestedStatement.getQueryTimeout();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public ResultSet getResultSet() throws SQLException
  {
    return nestedStatement.getResultSet();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getResultSetConcurrency() throws SQLException
  {
    return nestedStatement.getResultSetConcurrency();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getResultSetHoldability() throws SQLException
  {
    return nestedStatement.getResultSetHoldability();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getResultSetType() throws SQLException
  {
    return nestedStatement.getResultSetType();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public int getUpdateCount() throws SQLException
  {
    return nestedStatement.getUpdateCount();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public SQLWarning getWarnings() throws SQLException
  {
    return nestedStatement.getWarnings();
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setCursorName(String name) throws SQLException
  {
    nestedStatement.setCursorName(name);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setEscapeProcessing(boolean enable) throws SQLException
  {
    nestedStatement.setEscapeProcessing(enable);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setFetchDirection(int direction) throws SQLException
  {
    nestedStatement.setFetchDirection(direction);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setFetchSize(int rows) throws SQLException
  {
    nestedStatement.setFetchSize(rows);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setMaxFieldSize(int max) throws SQLException
  {
    nestedStatement.setMaxFieldSize(max);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setMaxRows(int max) throws SQLException
  {
    nestedStatement.setMaxRows(max);
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void setQueryTimeout(int seconds) throws SQLException
  {
    nestedStatement.setQueryTimeout(seconds);
  }

  /**
   * Gets the nested statement.
   *
   * @return the nested statement
   */
  public Statement getNestedStatement()
  {
    return nestedStatement;
  }

  /**
   * Sets the nested statement.
   *
   * @param nestedStatement the new nested statement
   */
  public void setNestedStatement(Statement nestedStatement)
  {
    this.nestedStatement = nestedStatement;
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public void closeOnCompletion() throws SQLException
  {
    Jdbc17Utils.invoke(nestedStatement, SQLException.class, "closeOnCompletion");
  }

  /**
   * {@inheritDoc}
   *
   */

  @Override
  public boolean isCloseOnCompletion() throws SQLException
  {
    return (Boolean) Jdbc17Utils.invoke(nestedStatement, SQLException.class, "isCloseOnCompletion");
  }

}