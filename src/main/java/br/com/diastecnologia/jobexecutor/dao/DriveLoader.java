package br.com.diastecnologia.jobexecutor.dao;

import org.apache.log4j.Logger;

public class DriveLoader {

	private static Logger logger = Logger.getLogger(DriveLoader.class);
	
	public static boolean init(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			logger.info("Driver carregado com sucesso.");
			
			return true;
		}catch(Exception ex){
			logger.error("Erro ao carregar driver", ex);
			return false;
		}
	}
	
}
