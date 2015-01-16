package br.com.diastecnologia.jobexecutor.emails;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

import br.com.diastecnologia.jobexecutor.Application;

public class EmailSender {

	private static Logger logger = Logger.getLogger(Application.class);
	
	private static String path = "http://www.shopmaquinas.com.br/emails/";
	private static String username = "contato@shopmaquinas.com.br";
	private static String password = "shop1!2@3#";
	private static String smtp = "smtp.shopmaquinas.com.br";
	private static int port = 587;
	private static String name = "Contato Shop Máquinas";
	private static String cc="marcelo.dias@e-deploy.com.br";
	
	public static String getHtmlBody( String emailName ) throws IOException{
		URL url = new URL( path + emailName + "?data=" + Calendar.getInstance().getTimeInMillis() );
		URLConnection connection = url.openConnection();
		InputStream stream = connection.getInputStream();
		
		byte[] buffer = new byte[1024 * 1024];
		String content = "";
		int read = 0;
		while( (read = stream.read(buffer, 0, buffer.length) ) != -1){
			content += new String( buffer, 0 , read);
		}
		return content;
	}
	
	public static void sendEmail( String subject, String htmlBody, String to, String toName){
		try{
			logger.info("Enviando e-mail " +  subject + " para " + to);
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName(smtp);
			email.setSmtpPort(port);
			
			email.setAuthenticator(new DefaultAuthenticator(username, password));
			
			email.setSSLOnConnect(false);
			email.setFrom(username, name);
			
			email.addCc(cc);

			email.setSubject(subject);
			email.setHtmlMsg(htmlBody);
			
			email.addTo( to, toName );
			email.send();
			
			logger.info("Enviando com sucesso.");
		}catch(Exception ex){
			logger.error("Falha ao enviar e-mail: ", ex);
		}
	}
	
}
