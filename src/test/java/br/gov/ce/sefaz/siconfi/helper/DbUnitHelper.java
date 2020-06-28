package br.gov.ce.sefaz.siconfi.helper;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import javax.persistence.EntityManager;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;

public class DbUnitHelper {

	private DatabaseConnection conexaoDBUnit;
	private String xmlFolder;
	private EntityManager em;

	public DbUnitHelper(EntityManager em, String xmlFolder) {
		this.xmlFolder = xmlFolder;
		this.em = em;
		try {
			Session session = this.em.unwrap(Session.class);
			session.doWork(new Work() {				
				@Override
				public void execute(Connection connection) throws SQLException {
					try {
						conexaoDBUnit = new DatabaseConnection(connection);
					} catch (DatabaseUnitException e) {
						e.printStackTrace();
					}
				}
			});
			
			DatabaseConfig config = conexaoDBUnit.getConfig();
			config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new H2DataTypeFactory());
		} catch (Exception e) {
			throw new RuntimeException("Erro inicializando DBUnit", e);
		}
	}

	public void execute(DatabaseOperation operation, String xml) {
		try {
			InputStream is = getClass().getResourceAsStream("/" + xmlFolder + "/" + xml);
			FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
			IDataSet dataSet = builder.build(is);
			operation.execute(conexaoDBUnit, dataSet);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro executando DbUnit", e);
		}
	}

	public void close() {
		try {
			conexaoDBUnit.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}	
}