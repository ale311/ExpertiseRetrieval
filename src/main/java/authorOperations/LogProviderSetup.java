package authorOperations;

import org.neo4j.logging.Log;
import org.neo4j.logging.LogProvider;

public class LogProviderSetup {
	public static LogProvider getLogProvider(){
		LogProvider lp = new LogProvider() {
			
			@Override
			public Log getLog(String name) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Log getLog(Class loggingClass) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		return lp;
		
	}
}
