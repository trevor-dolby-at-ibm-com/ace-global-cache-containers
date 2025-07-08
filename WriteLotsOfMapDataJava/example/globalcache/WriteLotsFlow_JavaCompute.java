package example.globalcache;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbGlobalMap;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class WriteLotsFlow_JavaCompute extends MbJavaComputeNode {

	static boolean firstRun = true;
	
	public void evaluate(MbMessageAssembly inAssembly) throws MbException {
		MbOutputTerminal out = getOutputTerminal("out");

		MbMessage inMessage = inAssembly.getMessage();
		MbMessageAssembly outAssembly = null;
		try {
			// create new message as a copy of the input
			MbMessage outMessage = new MbMessage(inMessage);
			outAssembly = new MbMessageAssembly(inAssembly, outMessage);
			// ----------------------------------------------------------
			// Add user code below
			
			if ( firstRun )
			{
				System.out.println("WriteLotsOfMapData delaying initial write attempt for ten seconds");
				Thread.sleep(10000);
				firstRun = false;
			}
			System.out.println("WriteLotsOfMapData looking for EXAMPLE.CONTAINER.MAP");
            MbGlobalMap map = MbGlobalMap.getGlobalMap("EXAMPLE.CONTAINER.MAP");

            String hostname = "localhost";
            hostname = java.net.InetAddress.getLocalHost().getHostName();
        	
            long lastModifiedTime = System.currentTimeMillis() / 1000;

			String currentHostnameListValue = (String)map.get("hostnameList");
       		System.out.println("WriteLotsOfMapData currentHostnameListValue "+currentHostnameListValue);
           	if ( currentHostnameListValue == null )
           	{
           		System.out.println("WriteLotsOfMapData setting initial hostnameList to "+hostname);
           		map.put("hostnameList", hostname);
           	}
           	else
           	{
           		String hostnameList = (String)currentHostnameListValue;
           		if ( !hostnameList.contains(hostname) )
           		{
           			System.out.println("WriteLotsOfMapData updating hostnameList to "+hostnameList+";"+hostname);
           			map.update("hostnameList", hostnameList+";"+hostname);
           		}
           	}
			System.out.println("WriteLotsOfMapData setting 1024 entries");
			int countValue = 0;
		
			for ( int i=0 ; i<1024 ; i++ )
			{
				String currentKey = hostname + "_" + i;
				String newValue = currentKey + "_" + countValue++ + "_" + lastModifiedTime;
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				newValue = newValue+"0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";
				String currentValue = (String)map.get(currentKey);
	           	if ( currentValue == null )
	               	map.put(currentKey, newValue);
	           	else
	               	map.update(currentKey, newValue);
			}
           	
			System.out.println("WriteLotsOfMapData finished");
			
			// End of user code
			// ----------------------------------------------------------
		} catch (MbException e) {
			// Re-throw to allow Broker handling of MbException
			throw e;
		} catch (RuntimeException e) {
			// Re-throw to allow Broker handling of RuntimeException
			throw e;
		} catch (Exception e) {
			// Consider replacing Exception with type(s) thrown by user code
			// Example handling ensures all exceptions are re-thrown to be handled in the flow
			throw new MbUserException(this, "evaluate()", "", "", e.toString(), null);
		}
		// The following should only be changed
		// if not propagating message to the 'out' terminal
		out.propagate(outAssembly);

	}

	/**
	 * onPreSetupValidation() is called during the construction of the node
	 * to allow the node configuration to be validated.  Updating the node
	 * configuration or connecting to external resources should be avoided.
	 *
	 * @throws MbException
	 */
	@Override
	public void onPreSetupValidation() throws MbException {
	}

	/**
	 * onSetup() is called during the start of the message flow allowing
	 * configuration to be read/cached, and endpoints to be registered.
	 *
	 * Calling getPolicy() within this method to retrieve a policy links this
	 * node to the policy. If the policy is subsequently redeployed the message
	 * flow will be torn down and reinitialized to it's state prior to the policy
	 * redeploy.
	 *
	 * @throws MbException
	 */
	@Override
	public void onSetup() throws MbException {
	}

	/**
	 * onStart() is called as the message flow is started. The thread pool for
	 * the message flow is running when this method is invoked.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStart() throws MbException {
	}

	/**
	 * onStop() is called as the message flow is stopped. 
	 *
	 * The onStop method is called twice as a message flow is stopped. Initially
	 * with a 'wait' value of false and subsequently with a 'wait' value of true.
	 * Blocking operations should be avoided during the initial call. All thread
	 * pools and external connections should be stopped by the completion of the
	 * second call.
	 *
	 * @throws MbException
	 */
	@Override
	public void onStop(boolean wait) throws MbException {
	}

	/**
	 * onTearDown() is called to allow any cached data to be released and any
	 * endpoints to be deregistered.
	 *
	 * @throws MbException
	 */
	@Override
	public void onTearDown() throws MbException {
	}

}
