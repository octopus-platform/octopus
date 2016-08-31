package octopus.server.gremlinShell;

import groovy.lang.Closure;
import groovy.lang.Script;

import java.util.Map;


public abstract class OctopusScriptBase extends Script
{

	public boolean newSessionStep(String name, Closure closure)
	{
		Map<String, Closure> sessionSteps = getSessionSteps();
		if (!sessionSteps.containsKey(name))
		{
			sessionSteps.put(name, closure);
			return true;
		}
		return false;
	}

	public void deleteSessionStep(String name)
	{
		getSessionSteps().remove(name);
	}

	private Closure getSessionStep(String name)
	{
		return getSessionSteps().get(name);
	}

	private Map<String, Closure> getSessionSteps()
	{
		Map<String, Closure> sessionSteps = (Map<String, Closure>) getBinding().getVariable("sessionSteps");
		return sessionSteps;
	}

}
