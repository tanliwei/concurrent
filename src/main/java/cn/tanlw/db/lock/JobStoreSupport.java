/* 
 * Copyright 2001-2009 Terracotta, Inc. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

package cn.tanlw.db.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * <p>
 * Contains base functionality for JDBC-based JobStore implementations.
 * </p>
 * 
 * @author <a href="mailto:jeff@binaryfeed.org">Jeffrey Wescott</a>
 * @author James House
 */
public class JobStoreSupport  {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Commit the supplied connection
     *
     * @param conn (Optional)
     * @throws JobPersistenceException thrown if a SQLException occurs when the
     * connection is committed
     */
    public static void commitConnection(Connection conn)
            throws JobPersistenceException {

        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                throw new JobPersistenceException(
                        "Couldn't commit jdbc connection. "+e.getMessage(), e);
            }
        }
    }


    /**
     * Rollback the supplied connection.
     *
     * <p>
     * Logs any SQLException it gets trying to rollback, but will not propogate
     * the exception lest it mask the exception that caused the caller to
     * need to rollback in the first place.
     * </p>
     *
     * @param conn (Optional)
     */
    public static void rollbackConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException e) {
//                getLog().error(
//                        "Couldn't rollback jdbc connection. "+e.getMessage(), e);
            }
        }
    }


    protected Logger getLog() {
        return log;
    }


    /**
     * Closes the supplied <code>Connection</code>.
     * <p>
     * Ignores a <code>null Connection</code>.
     * Any exception thrown trying to close the <code>Connection</code> is
     * logged and ignored.
     * </p>
     *
     * @param conn The <code>Connection</code> to close (Optional).
     */
    protected static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
//                getLog().error("Failed to close Connection", e);
            } catch (Throwable e) {
//                getLog().error(
//                        "Unexpected exception closing Connection." +
//                                "  This is often due to a Connection being returned after or during shutdown.", e);
            }
        }
    }
}

// EOF
