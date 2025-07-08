package example.globalcache;

import com.ibm.broker.javacompute.MbJavaComputeNode;
import com.ibm.broker.plugin.MbException;
import com.ibm.broker.plugin.MbGlobalMap;
import com.ibm.broker.plugin.MbMessage;
import com.ibm.broker.plugin.MbMessageAssembly;
import com.ibm.broker.plugin.MbOutputTerminal;
import com.ibm.broker.plugin.MbUserException;

public class ReadLotsFlow_JavaCompute extends MbJavaComputeNode {

	static boolean firstRun = true;
	static ReadFromCache rfc;
	static int     numCacheReads = 0;
    public class ReadFromCache extends Thread
    {
    	public int numCacheReads = 0;
    	public ReadFromCache()
    	{
    	}
    	@Override
    	public void run()
    	{
    		try 
    		{
                MbGlobalMap map = MbGlobalMap.getGlobalMap("EXAMPLE.CONTAINER.MAP");

                while ( true )
                {
                	String currentHostnameListValue = (String)map.get("hostnameList");
                	if ( currentHostnameListValue == null )
                	{
                		System.out.println("ReadFromCache run() found no hostname list");
                    	Thread.sleep(5000);
                	}
                	else
                	{
                		String hostnameList = (String)currentHostnameListValue;
                		String [] hostnames = hostnameList.split(";");
                		for ( int j=0 ; j<hostnames.length ; j++ )
                		{
                    		boolean readAnything = false;
                			String hostname = hostnames[j];
                			for ( int i=0 ; i<1024 ; i++ )
                			{
                				String currentKey = hostname + "_" + i;
                				String currentValue = (String)map.get(currentKey);
                	           	if ( currentValue != null )
                	           		readAnything = true;
                			}
                    		if ( readAnything )
                    			numCacheReads++;
                		}
                	}
                	Thread.sleep(20);
               	}
			}
    		catch (Exception e) 
    		{
				e.printStackTrace();
			}
    	}
    }

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
				System.out.println("ReadLotsOfMapData delaying initial read attempt for ten seconds");
				Thread.sleep(10000);

				rfc = new ReadFromCache();
				rfc.start();
				firstRun = false;
			}
			else
			{
				int currentNumCacheReads = rfc.numCacheReads;
				System.out.println("ReadLotsOfMapData found "+(currentNumCacheReads - numCacheReads)+" reads since last check");
				numCacheReads = currentNumCacheReads;
			}
			
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
