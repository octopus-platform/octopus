package octopus.server.commands.manageprojects;

import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.orientechnologies.orient.server.network.protocol.http.OHttpRequest;
import com.orientechnologies.orient.server.network.protocol.http.OHttpResponse;
import com.orientechnologies.orient.server.network.protocol.http.OHttpUtils;
import com.orientechnologies.orient.server.network.protocol.http.command.OServerCommandAbstract;

import octopus.server.components.projectmanager.ProjectManager;

public class ManageProjectsHandler extends OServerCommandAbstract
{

	public ManageProjectsHandler(final OServerCommandConfiguration iConfiguration)
	{
	}

	@Override
	public boolean execute(OHttpRequest iRequest, OHttpResponse iResponse) throws Exception
	{

		String[] urlParts = checkSyntax(
				iRequest.url,
				3,
				"Syntax error: manageprojects/<cmd>/<projectName>");

		String command = urlParts[1];
		String projectName = urlParts[2];

		ProjectManager manager = new ProjectManager();

		if(command.equals("create"))
			manager.create(projectName);
		else if(command.equals("delete"))
			manager.delete(projectName);

		iResponse.send(OHttpUtils.STATUS_OK_CODE, "OK", null,
				"", null);

		return false;
	}

	@Override
	public String[] getNames()
	{
		return new String[] { "GET|manageprojects/*" };
	}

}