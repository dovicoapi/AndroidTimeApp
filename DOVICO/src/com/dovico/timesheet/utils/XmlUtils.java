package com.dovico.timesheet.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.dovico.timesheet.beans.Assignment;
import com.dovico.timesheet.beans.Client;
import com.dovico.timesheet.beans.Employee;
import com.dovico.timesheet.beans.Project;
import com.dovico.timesheet.beans.Task;
import com.dovico.timesheet.beans.TimeEntry;
import com.dovico.timesheet.db.Db.TimeEntries;

import android.util.Log;

public class XmlUtils {

	public static final String TAG = "XMLUtils";

	public static class XMLTags {
		
		
		public static class ClientsXMLTags {
			/*
			 * <Client>
                    <ID>660</ID>
                    <Name>Client 1</Name>
                    <Abbreviation>Cli 1-</Abbreviation>
                    <Contact>Sam Smith</Contact>
                    <Email>SamSmith@SmithCo.com</Email>
                    <Region>
                        <ID>-1</ID>
                        <Name>[None]</Name>
                        <GetItemURI>N/A</GetItemURI>
                    </Region>
                    <Archive>F</Archive>
                    <Integrate></Integrate>
                    <CustomFields>
                        <CustomField>
                            <ID>1631</ID>
                            <TemplateID>148</TemplateID>
                            <Name>Date of Birth</Name>
                            <Values>
                                <Value>1967-12-31</Value>
                            </Values>
                            <GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/148/?version=1</GetCustomTemplateURI>
                        </CustomField>
                        <CustomField>
                            ...
                        </CustomField>
                    </CustomFields>
                </Client>
			 */
			
			public static final String BEGIN_TAG = "<Client>";
			public static final String END_TAG = "</Client> ";
			public static final String CLIENT_ID_BEGIN_TAG = "<ID>";
			public static final String CLIENT_ID_END_TAG = "</ID>";
			public static final String NAME_BEGIN_TAG = "<Name>";
			public static final String NAME_END_TAG = "</Name>";
			public static final String CONTACT_BEGIN_TAG = "<Contact>";
			public static final String CONTACT_END_TAG = "</Contact>";
			public static final String EMAIL_BEGIN_TAG = "<Email>";
			public static final String EMAIL_END_TAG = "</Email>";
			
		}
		
		public static class ProjectsXMLTags {
			/*
			 * "><Projects><Project><ID>115</ID><Client><ID>0</ID><Name>[None]</Name><GetItemURI>N/A</GetItemURI></Client>
			 * <Name>Administration</Name><Leader><ID>100</ID><Name>admin, admin</Name><GetItemURI>https://api.dovico.com/Employees/100/?version=1</GetItemURI></Leader><Description/>
			 * <Status>E</Status><ProjectGroup><ID>0</ID><Name>[None]</Name><GetItemURI>N/A</GetItemURI></ProjectGroup><StartDate>2011-03-28</StartDate>
			 * <EndDate>2011-03-28</EndDate><BillingBy>A</BillingBy><FixedCost>0</FixedCost>
			 * <Currency><ID>280</ID><Symbol>USD $</Symbol><GetItemURI>https://api.dovico.com/Currencies/280/?version=1</GetItemURI></Currency>
			 * <BudgetRateDate>2011-03-28</BudgetRateDate><HideTasks>F</HideTasks><PreventEntries>F</PreventEntries><TimeBillableByDefault>T</TimeBillableByDefault><ExpensesBillableByDefault>T</ExpensesBillableByDefault>
			 * <Linked>F</Linked><MSPConfig/><RSProject>F</RSProject><Archive>F</Archive><Integrate/>
			 * <CustomFields><CustomField><ID>101</ID><TemplateID>100</TemplateID><Name>Custom Field</Name><Values><Value>Easily add user defined fields</Value></Values
<GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/100/?version=1</GetCustomTemplateURI></CustomField><CustomField><ID>361</ID><TemplateID>105</TemplateID><Name>Custom Field</Name><Values><Value>Easily add user defined fields</Value></Values><GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/105/?version=1</GetCustomTemplateURI></CustomField><CustomField><ID>370</ID><TemplateID>108</TemplateID><Name>Custom Field</Name><Values><Value>Easily add user defined fields</Value></Values><GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/108/?version=1</GetCustomTemplateURI></CustomField></CustomFields></Project>
			 */
			
			public static final String BEGIN_TAG = "<Project>";
			public static final String END_TAG = "</Project> ";
			public static final String ID_BEGIN_TAG = "<ID>";
			public static final String ID_END_TAG = "</ID>";
			public static final String CLIENT_ID_BEGIN_TAG = "<ID>";
			public static final String CLIENT_ID_END_TAG = "</ID>";
			public static final String NAME_BEGIN_TAG = "</Client><Name>";
			public static final String NAME_END_TAG = "</Name>";

			
		}
		
		public static class TasksXMLTags {
			/*
			 * ><Task><ID>113</ID><Name>&gt; drag and drop tasks over your projects</Name><TaskGroup><ID>0</ID><Name>[None]</Name>
			 * <GetItemURI>N/A</GetItemURI></TaskGroup><Description>Full production of product</Description><ForceDescription>F</ForceDescription>
			 * <Global>F</Global><Prorate>1</Prorate><RSTask>F</RSTask><WBS/><Archive>F</Archive><Integrate/>
			 * <CustomFields><CustomField><ID>369</ID><TemplateID>105</TemplateID><Name>Custom Field</Name>
			 * <Values><Value>Easily add user defined fields</Value></Values><GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/105/?version=1</GetCustomTemplateURI></CustomField>
			 * <CustomField><ID>360</ID><TemplateID>108</TemplateID><Name>Custom Field</Name><Values><Value>Easily add user defined fields</Value></Values><GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/108/?version=1</GetCustomTemplateURI></CustomField>
			 * <CustomField><ID>-1</ID><TemplateID>111</TemplateID><Name>Custom Field</Name><Values><Value>Easily add user defined fields</Value></Values><GetCustomTemplateURI>https://api.dovico.com/CustomFieldTemplates/111/?version=1</GetCustomTemplateURI></CustomField></CustomFields></Task>
			 */
			
			public static final String BEGIN_TAG = "<Task>";
			public static final String END_TAG = "</Task> ";
			public static final String ID_BEGIN_TAG = "<ID>";
			public static final String ID_END_TAG = "</ID>";
			public static final String NAME_BEGIN_TAG = "<Name>";
			public static final String NAME_END_TAG = "</Name>";
			public static final String DESCRIPTION_BEGIN_TAG = "<Description>";
			public static final String DESCRIPTION_END_TAG = "</Description>";			
			public static final String PROJECT_ID_BEGIN_TAG = "<ID>";
			public static final String PROJECT_ID_END_TAG = "</ID>";
			
		}
		
		public static class AssignmentsXMLTags {
			
			public static final String BEGIN_TAG = "<Assignment>";
			public static final String END_TAG = "</Assignment> ";
			public static final String ID_BEGIN_TAG = "<AssignmentID>";
			public static final String ID_END_TAG = "</AssignmentID>";		
			public static final String PROJECT_ID_BEGIN_TAG = "<ItemID>";
			public static final String PROJECT_ID_END_TAG = "</ItemID>";
			public static final String NAME_BEGIN_TAG = "<Name>";
			public static final String NAME_END_TAG = "</Name>";
			public static final String GET_ASSIGNMENT_URI_BEGIN_TAG = "<GetAssignmentsURI>";
			public static final String GET_ASSIGNMENT_URI_END_TAG = "</GetAssignmentsURI>";
			
		}
		
		public static class TimeEntriesXMLTags {
			
			public static final String BEGIN_TAG = "<TimeEntry>";
			public static final String END_TAG = "</TimeEntry> ";
			public static final String ID_BEGIN_TAG = "<ID>";
			public static final String ID_END_TAG = "</ID>";		
			public static final String SHEET_ID_BEGIN_TAG = "<ID>";
			public static final String SHEET_ID_END_TAG = "</ID>";
			public static final String PROJECT_ID_BEGIN_TAG = "<ID>";
			public static final String PROJECT_ID_END_TAG = "</ID>";
			public static final String PROJECT_NAME_BEGIN_TAG = "<Name>";
			public static final String PROJECT_NAME_END_TAG = "</Name>";
			public static final String TASK_BEGIN_TAG = "<ID>";
			public static final String TASK_END_TAG = "</ID>";
			public static final String TASK_NAME_BEGIN_TAG = "<Name>";
			public static final String TASK_NAME_END_TAG = "</Name>";
			public static final String DATE_BEGIN_TAG = "<Date>";
			public static final String DATE_END_TAG = "</Date>";
			public static final String TOTAL_HOURS_BEGIN_TAG = "<TotalHours>";
			public static final String TOTAL_HOURS_END_TAG = "</TotalHours>";
			public static final String STATUS_BEGIN_TAG = "<Status>";
			public static final String STATUS_END_TAG = "</Status>";
			public static final String DESCRIPTION_BEGIN_TAG = "<Description>";
			public static final String DESCRIPTION_END_TAG = "</Description>";
			
		}
		
		public static class EmployeeXMLTags {
			/*
			 * <Employee>
                    <ID>100</ID>
                    <LastName>Smith</LastName>
                    <FirstName>Sam</FirstName>
                    <GetItemURI>https://api.dovico.com/Employees/100/?version=1</GetItemURI>
                </Employee>
			 */
			
			public static final String BEGIN_TAG = "<Employee>";
			public static final String END_TAG = "</Employee> ";
			public static final String ID_BEGIN_TAG = "<ID>";
			public static final String ID_END_TAG = "</ID>";
			public static final String NAME_BEGIN_TAG = "<FirstName>";
			public static final String NAME_END_TAG = "</FirstName>";
			public static final String SURNAME_BEGIN_TAG = "<LastName>";
			public static final String SURNAME_END_TAG = "</LastName>";
			
		}
		
		
		

	}

	public static List<Client> parseClients(
			String stringFromXml) throws IOException {
	
		List<Client> clients = new ArrayList<Client>();

			while (stringFromXml.contains(XMLTags.ClientsXMLTags.BEGIN_TAG)) {
				
				clients.add(parseClient(stringFromXml));
				
				String clientEndTag = XMLTags.ClientsXMLTags.END_TAG.substring(0, XMLTags.ClientsXMLTags.END_TAG.length() - 1);
				int clientDefinitionEndIndex = stringFromXml.indexOf(clientEndTag);
				if (clientDefinitionEndIndex != -1) {
					stringFromXml = stringFromXml.substring(clientDefinitionEndIndex + 1);
					Logger.i(TAG, "curr line: " + stringFromXml);
					} else {
						break;
					}
		}
		return clients;
	}
	
	public static Client parseClient(String stringFromXml) throws IOException{
		Client newClient = new Client();
		
		if (stringFromXml.contains(XMLTags.ClientsXMLTags.CLIENT_ID_BEGIN_TAG)) {
			newClient.setId(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.ClientsXMLTags.CLIENT_ID_END_TAG, stringFromXml.indexOf(XMLTags.ClientsXMLTags.CLIENT_ID_BEGIN_TAG))));
		}				
		if (stringFromXml.contains(XMLTags.ClientsXMLTags.NAME_BEGIN_TAG))
			newClient.setName(extractTagValue(stringFromXml,
					XMLTags.ClientsXMLTags.NAME_END_TAG,  stringFromXml.indexOf(XMLTags.ClientsXMLTags.NAME_BEGIN_TAG)));
		
		if (stringFromXml.contains(XMLTags.ClientsXMLTags.EMAIL_BEGIN_TAG))
			newClient.setEmail(extractTagValue(stringFromXml,
					XMLTags.ClientsXMLTags.EMAIL_END_TAG, stringFromXml.indexOf(XMLTags.ClientsXMLTags.EMAIL_BEGIN_TAG)));
		
		
		
		Logger.d(TAG, "newClient id: " + newClient.getId());
		Logger.d(TAG, "newClient contact: " + newClient.getContact());
		Logger.d(TAG, "newClient email: " + newClient.getEmail());
		Logger.d(TAG, "newClient name: " + newClient.getName());
		
		return newClient;
	}
	
	public static List<Project> parseProjects(
			String stringFromXml) throws IOException {
	
		List<Project> projects = new ArrayList<Project>();

			while (stringFromXml.contains(XMLTags.ProjectsXMLTags.BEGIN_TAG)) {
				
				projects.add(parseProject(stringFromXml));
				
				String projectEndTag = XMLTags.ProjectsXMLTags.END_TAG.substring(0, XMLTags.ProjectsXMLTags.END_TAG.length() - 1);
				int projectDefinitionEndIndex = stringFromXml.indexOf(projectEndTag);
				if (projectDefinitionEndIndex != -1) {
					stringFromXml = stringFromXml.substring(projectDefinitionEndIndex + 1);
					Logger.i(TAG, "curr line: " + stringFromXml);
					} else {
						break;
					}
		}
		return projects;
	}
	
	public static Project parseProject(String stringFromXml) throws IOException{
		Project newProject = new Project();
	
		if (stringFromXml.contains(XMLTags.ProjectsXMLTags.ID_BEGIN_TAG)) {
			newProject.setId(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.ProjectsXMLTags.ID_END_TAG, stringFromXml.indexOf(XMLTags.ProjectsXMLTags.ID_BEGIN_TAG))));
			stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.ProjectsXMLTags.ID_END_TAG));
		}
					
		
		if (stringFromXml.contains(XMLTags.ProjectsXMLTags.CLIENT_ID_BEGIN_TAG)) {
			newProject.setClientID(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.ProjectsXMLTags.CLIENT_ID_END_TAG, stringFromXml.indexOf(XMLTags.ProjectsXMLTags.CLIENT_ID_BEGIN_TAG))));
			stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.ProjectsXMLTags.CLIENT_ID_END_TAG));
		}	
		
						
		if (stringFromXml.contains(XMLTags.ProjectsXMLTags.NAME_BEGIN_TAG))
			newProject.setName(extractTagValue(stringFromXml,
					XMLTags.ProjectsXMLTags.NAME_END_TAG,  stringFromXml.indexOf(XMLTags.ProjectsXMLTags.NAME_BEGIN_TAG) + 9));
		
		Logger.d(TAG, "newProject id: " + newProject.getId());
		Logger.d(TAG, "newProject clientId: " + newProject.getClientID());
		Logger.d(TAG, "newProject name: " + newProject.getName());		

return newProject;
}
	public static List<Task> parseTasks(
			String stringFromXml) throws IOException {
	
		List<Task> tasks = new ArrayList<Task>();

			while (stringFromXml.contains(XMLTags.TasksXMLTags.BEGIN_TAG)) {
				Task newTask = new Task();
			
				if (stringFromXml.contains(XMLTags.TasksXMLTags.ID_BEGIN_TAG)) {
					newTask.setId(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.TasksXMLTags.ID_END_TAG, stringFromXml.indexOf(XMLTags.TasksXMLTags.ID_BEGIN_TAG))));
					stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TasksXMLTags.ID_END_TAG));
				}
							
				
				if (stringFromXml.contains(XMLTags.TasksXMLTags.NAME_BEGIN_TAG)) {
					newTask.setName(extractTagValue(stringFromXml, XMLTags.TasksXMLTags.NAME_END_TAG, stringFromXml.indexOf(XMLTags.TasksXMLTags.NAME_BEGIN_TAG)));
					//stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.ProjectsXMLTags.CLIENT_ID_END_TAG));
				}	
				
				if (stringFromXml.contains(XMLTags.TasksXMLTags.PROJECT_ID_BEGIN_TAG)) {
					int indexOfProjectIdBeginTag = stringFromXml.indexOf(XMLTags.TasksXMLTags.PROJECT_ID_BEGIN_TAG);
					String tagValue = extractTagValue(stringFromXml, XMLTags.TasksXMLTags.PROJECT_ID_END_TAG, indexOfProjectIdBeginTag);
					Logger.d(TAG, "indeOf: " + indexOfProjectIdBeginTag);
					Logger.d(TAG, "tagValue: " + tagValue);
					newTask.setProjectID(Integer.parseInt(tagValue));
				}
								
				if (stringFromXml.contains(XMLTags.TasksXMLTags.DESCRIPTION_BEGIN_TAG))
					newTask.setDescription(extractTagValue(stringFromXml,
							XMLTags.TasksXMLTags.DESCRIPTION_END_TAG,  stringFromXml.indexOf(XMLTags.TasksXMLTags.DESCRIPTION_BEGIN_TAG)));
								
				Logger.d(TAG, "newTask id: " + newTask.getId());
				Logger.d(TAG, "newTask description: " + newTask.getDescription());
				Logger.d(TAG, "newTask name: " + newTask.getName());
				Logger.d(TAG, "newTask projectID:" + newTask.getProjectID());
				
				tasks.add(newTask);
				
				String taskEndTag = XMLTags.TasksXMLTags.END_TAG.substring(0, XMLTags.TasksXMLTags.END_TAG.length() - 1);
				int taskEndDefinitionIndex = stringFromXml.indexOf(taskEndTag);
				if (taskEndDefinitionIndex != -1) {
					stringFromXml = stringFromXml.substring(taskEndDefinitionIndex + 1);
					Logger.i(TAG, "curr line: " + stringFromXml);
					} else {
						break;
					}
		}
		return tasks;
	}
	
	public static List<Assignment> parseAssignments(String stringFromXml) throws IOException {
		List<Assignment> assignments = new ArrayList<Assignment>();

		while (stringFromXml.contains(XMLTags.AssignmentsXMLTags.BEGIN_TAG)) {
			Assignment assignment = new Assignment();
		
			if (stringFromXml.contains(XMLTags.AssignmentsXMLTags.ID_BEGIN_TAG)) {
				assignment.setAssignmentID(extractTagValue(stringFromXml, XMLTags.AssignmentsXMLTags.ID_END_TAG, stringFromXml.indexOf(XMLTags.AssignmentsXMLTags.ID_BEGIN_TAG)));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.AssignmentsXMLTags.ID_END_TAG));
			}
									
			if (stringFromXml.contains(XMLTags.AssignmentsXMLTags.PROJECT_ID_BEGIN_TAG)) {
				int indexOfProjectIdBeginTag = stringFromXml.indexOf(XMLTags.AssignmentsXMLTags.PROJECT_ID_BEGIN_TAG);
				String tagValue = extractTagValue(stringFromXml, XMLTags.AssignmentsXMLTags.PROJECT_ID_END_TAG, indexOfProjectIdBeginTag);
				assignment.setItemID(Integer.parseInt(tagValue));
			}					
			
			if (stringFromXml.contains(XMLTags.AssignmentsXMLTags.NAME_BEGIN_TAG))
				assignment.setName(extractTagValue(stringFromXml,
						XMLTags.AssignmentsXMLTags.NAME_END_TAG,  stringFromXml.indexOf(XMLTags.AssignmentsXMLTags.NAME_BEGIN_TAG)));
			
			if (stringFromXml.contains(XMLTags.AssignmentsXMLTags.GET_ASSIGNMENT_URI_BEGIN_TAG))
				assignment.setAssignmentURI(extractTagValue(stringFromXml,
						XMLTags.AssignmentsXMLTags.GET_ASSIGNMENT_URI_END_TAG,  stringFromXml.indexOf(XMLTags.AssignmentsXMLTags.GET_ASSIGNMENT_URI_BEGIN_TAG)));
							
			Logger.d(TAG, "assignment id: " + assignment.getAssignmentID());
			Logger.d(TAG, "assignment itemID:" + assignment.getItemID());
			Logger.d(TAG, "assignment name:" + assignment.getName());
			Logger.d(TAG, "assignmentURI:" + assignment.getAssignmentURI());
			
			assignments.add(assignment);
			
			String taskEndTag = XMLTags.AssignmentsXMLTags.END_TAG.substring(0, XMLTags.AssignmentsXMLTags.END_TAG.length() - 1);
			int taskEndDefinitionIndex = stringFromXml.indexOf(taskEndTag);
			if (taskEndDefinitionIndex != -1) {
				stringFromXml = stringFromXml.substring(taskEndDefinitionIndex + 1);
				Logger.i(TAG, "curr line: " + stringFromXml);
				} else {
					break;
				}
	}
	return assignments;
	}
	
	
	/// <history>
    /// <modified author="C. Gerard Gallant" date="2012-06-25" reason="We've modified the URI for time entries to only request the entries within the desired date range. As a result, some of the date logic within this function was no longer needed. Also added the logic to grab the description from the time entry data returned"/>
	/// </history>
	public static List<TimeEntry> parseTimeEntries(String stringFromXml, Date currentDate) throws IOException {
		
		// I don't know why but the END_TAG values defined all have an extra space at the end. The following gives us the tag without the space (I didn't remove
		// the whitespace from the tag constants because I'm not sure if it's important or not)
		String sTimeEntryEndTag = XMLTags.TimeEntriesXMLTags.END_TAG.substring(0, XMLTags.TimeEntriesXMLTags.END_TAG.length() - 1);		
		
		List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();

		while (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.BEGIN_TAG)) {
			TimeEntry timeEntry = new TimeEntry();
			
			// ID of the Time Entry
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.ID_BEGIN_TAG)) {
				timeEntry.setId(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.ID_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.ID_BEGIN_TAG)));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.ID_END_TAG) + XMLTags.TimeEntriesXMLTags.ID_END_TAG.length());
			}
						
			// SHEET ID
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.SHEET_ID_BEGIN_TAG)) {
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.SHEET_ID_END_TAG)+ XMLTags.TimeEntriesXMLTags.SHEET_ID_END_TAG.length());
			}	
			
			// SHEET STATUS
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.STATUS_BEGIN_TAG)) {
				timeEntry.setStatus(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.STATUS_END_TAG,  stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.STATUS_BEGIN_TAG)));
			}

			// PROJECT ID
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.PROJECT_ID_BEGIN_TAG)) {
				timeEntry.setProjectID(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.PROJECT_ID_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.PROJECT_ID_BEGIN_TAG))));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.PROJECT_ID_END_TAG)+ XMLTags.TimeEntriesXMLTags.PROJECT_ID_END_TAG.length());
			}		
			
			// PROJECT NAME
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.PROJECT_NAME_BEGIN_TAG)) {
				timeEntry.setProjectName(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.PROJECT_NAME_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.PROJECT_NAME_BEGIN_TAG)));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.PROJECT_NAME_END_TAG)+ XMLTags.TimeEntriesXMLTags.PROJECT_NAME_END_TAG.length());
			}	

			// TASK ID
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.TASK_BEGIN_TAG)) {
				timeEntry.setTaskID(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.TASK_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TASK_BEGIN_TAG))));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TASK_END_TAG));
			}
			
			// TASK NAME
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.TASK_NAME_BEGIN_TAG)) {
				timeEntry.setTaskName(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.TASK_NAME_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TASK_NAME_BEGIN_TAG)));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TASK_NAME_END_TAG)+ XMLTags.TimeEntriesXMLTags.TASK_NAME_END_TAG.length());
			}	

			// DATE
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.DATE_BEGIN_TAG)) {
				timeEntry.setDate(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.DATE_END_TAG, stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.DATE_BEGIN_TAG)));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.DATE_END_TAG)+ XMLTags.TimeEntriesXMLTags.DATE_END_TAG.length());
			}

			// TOTAL HOURS
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.TOTAL_HOURS_BEGIN_TAG)) {
				timeEntry.setTotalHours(Double.parseDouble(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.TOTAL_HOURS_END_TAG,  stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TOTAL_HOURS_BEGIN_TAG))));
				stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.TOTAL_HOURS_END_TAG)+ XMLTags.TimeEntriesXMLTags.TOTAL_HOURS_END_TAG.length());
			}

			// DESCRIPTION
			if (stringFromXml.contains(XMLTags.TimeEntriesXMLTags.DESCRIPTION_BEGIN_TAG)) {
				// The trick with the description is that it might be empty '<Description/>' or it might have a value '<Description>some value</Description>'. Find
				// the index of the Description open tag (<Description>) and the TimeEntry's close tag (</TimeEntry>).
				int iDescriptionOpenTagIndex = stringFromXml.indexOf(XMLTags.TimeEntriesXMLTags.DESCRIPTION_BEGIN_TAG);
				int iTimeEntryCloseTagIndex = stringFromXml.indexOf(sTimeEntryEndTag);
				
				// If the Description index is before the closing tag of this time entry then this time entry has a description (if the index is after the
				// closing tag then our search for '<Description>' failed and this entry actually holds '<Description/>')...
				if(iDescriptionOpenTagIndex < iTimeEntryCloseTagIndex){
					// Grab the description (we create a new TimeEntry object on each loop and the default value for the description value within the object is
					// an empty string so we don't have to worry about setting the description to an empty string if the current entry does not have a description)
					timeEntry.setDescription(extractTagValue(stringFromXml, XMLTags.TimeEntriesXMLTags.DESCRIPTION_END_TAG,  iDescriptionOpenTagIndex));
				} // End if(iDescriptionOpenTagIndex < iTimeEntryCloseTagIndex)
			} // End if
				
								
			Logger.d(TAG, "timeEntry id: " + timeEntry.getId());
			Logger.d(TAG, "timeEntry projectID:" + timeEntry.getProjectID());
			Logger.d(TAG, "timeEntry taskID:" + timeEntry.getTaskID());
			Logger.d(TAG, "timeEntry date:" + timeEntry.getDate());
			Logger.d(TAG, "timeEntry totalHours:" + timeEntry.getTotalHours());
			Logger.d(TAG, "timeEntry description:" + timeEntry.getDescription());

			timeEntries.add(timeEntry);
		
			
			int taskEndDefinitionIndex = stringFromXml.indexOf(sTimeEntryEndTag);
			if (taskEndDefinitionIndex != -1) {
				stringFromXml = stringFromXml.substring(taskEndDefinitionIndex + 1);
				Logger.i(TAG, "curr line: " + stringFromXml);
			} else {
				break;
			}
		}
		
		return timeEntries;
	}
	
	public static Employee parseEmployee(String stringFromXml) throws IOException{
		Employee newEmployee = new Employee();

		if (stringFromXml.contains(XMLTags.EmployeeXMLTags.BEGIN_TAG)) {
		
			if (stringFromXml.contains(XMLTags.EmployeeXMLTags.ID_BEGIN_TAG)) {
				newEmployee.setId(Integer.parseInt(extractTagValue(stringFromXml, XMLTags.EmployeeXMLTags.ID_END_TAG, stringFromXml.indexOf(XMLTags.EmployeeXMLTags.ID_BEGIN_TAG))));
			}
						
			
			if (stringFromXml.contains(XMLTags.EmployeeXMLTags.NAME_BEGIN_TAG)) {
				newEmployee.setName(extractTagValue(stringFromXml, XMLTags.EmployeeXMLTags.NAME_END_TAG, stringFromXml.indexOf(XMLTags.EmployeeXMLTags.NAME_BEGIN_TAG)));
				//stringFromXml = stringFromXml.substring(stringFromXml.indexOf(XMLTags.ProjectsXMLTags.CLIENT_ID_END_TAG));
			}	
			
			if (stringFromXml.contains(XMLTags.EmployeeXMLTags.SURNAME_BEGIN_TAG)) {
				newEmployee.setLastName(extractTagValue(stringFromXml, XMLTags.EmployeeXMLTags.SURNAME_END_TAG, stringFromXml.indexOf(XMLTags.EmployeeXMLTags.SURNAME_BEGIN_TAG)));
			}				
							
			Logger.d(TAG, "newEmployee id: " + newEmployee.getId());
			Logger.d(TAG, "newEmployee surname: " + newEmployee.getLastName());
			Logger.d(TAG, "newEmployee name: " + newEmployee.getName());
			
	}
	return newEmployee;

	}
	
	

	public static boolean tagHasMoreLinesOfText(String line, String itemEndTag) {
		if (line.contains(itemEndTag)) {
			return false;
		} else {
			return true;
		}
	}

	private static String replaceEscape(String result) {
		if (result == null) {
			return "";
		}

		// Replace XML escape characters
		result = result.replace("\\n", "\n");
		result = result.replace("&apos;", "'");
		result = result.replace("&quot;", "\"");
		result = result.replace("apos;", "'");
		result = result.replace("quot;", "\"");
		result = result.replace("&lt;", "<");
		result = result.replace("&gt;", ">");
		result = result.replace("&amp;", "");
		return result;
	}

	/**
	 * Sample input: < tag property="value" /> property
	 * 
	 * Returns: "value"
	 **/
	public static String extractTagPropertyValue(String haystack, String needle) {
		String result = "";

		if (haystack.contains(needle)) {
			int start = haystack.indexOf(needle + "=\"") + needle.length() + 2;
			int end = haystack.indexOf("\"", start);
			if (end == -1) {
				// end = haystack.length();
				Log.w(TAG, "Error in XML (unclosed quotes in property:"
						+ needle + ')');
				return "";
			}
			result = haystack.substring(start, end);
		}
		return replaceEscape(result);
	}
	
	/**
	 * Sample input: < tag property="value" /> property
	 * 
	 * Returns: "value"
	 **/
	public static String extractTagPropertyValue(String haystack, String needle, int offset) {
		String result = "";

		haystack = haystack.substring(offset);
		
		if (haystack.contains(needle)) {
			int start = haystack.indexOf(needle + "=\"") + needle.length() + 2;
			int end = haystack.indexOf("\"", start);
			if (end == -1) {
				// end = haystack.length();
				Log.w(TAG, "Error in XML (unclosed quotes in property:"
						+ needle + ')');
				return "";
			}
			result = haystack.substring(start, end);
		}
		return replaceEscape(result);
	}

	/**
	 * Sample input: <tag>value</tag> tag
	 * 
	 * Returns: "value"
	 **/
	public static String extractTagValue(String haystack, String endTag) {
		String result = "";
		//Logger.d(TAG, "haystack: " + haystack);
		if (haystack.contains(endTag)) {
			int start = haystack.indexOf('>') + 1;
			int end = haystack.indexOf(endTag, start);
			
			//Logger.d(TAG, "start: " + start + ", end: " + end);
			
			if (end == -1) {
				// end = haystack.length();
				Log.w(TAG, "Error in XML (unclosed tag:" + endTag + ')');
				return "";
			}
			result = haystack.substring(start, end);
			//Logger.d(TAG, "result: " + result);
			return result.trim();
		}
		result = replaceEscape(result);
		//Logger.d(TAG, "result: " + result);
		return result;
	}

	/**
	 * Sample input: < tag>value< /tag> tag offset
	 * 
	 * Returns: "value" from offset
	 **/
	public static String extractTagValue(String haystack, String endTag,
			int offset) {
		String result = "";
		Logger.d(TAG, "offset: " + offset + ", haystack: " + haystack );
		haystack = haystack.substring(offset);
		Logger.d(TAG, "haystack substring: " + haystack);
		if (haystack.contains(endTag)) {
			int start = haystack.indexOf('>') + 1;
			// int end =
			// haystack.toLowerCase().indexOf("</"+needle.toLowerCase(), start);
			int end = haystack.indexOf(endTag, start); // ORIGINAL
			
			//Logger.d(TAG, "start: " + start + ", end: " + end);
			
			if (end == -1) {
				// end = haystack.length();
				Log.w(TAG, "Error in XML (unclosed tag:" + endTag + ')');
				return "";
			}
			result = haystack.substring(start, end);
		}
		return replaceEscape(result);
	}

	/**
	 * Sample input: < tag>true< /tag> tag
	 * 
	 * Returns: (boolean) true
	 **/
	public static boolean extractBooleanTagValue(String haystack, String needle) {
		String result = "";

		if (haystack.contains(needle)) {
			int start = haystack.indexOf(needle + '>') + needle.length() + 1;
			int end = haystack.indexOf("</" + needle, start);
			if (end == -1) {
				// end = haystack.length();
				Log.w(TAG, "Error in XML (unclosed tag:" + needle + ')');
				return false;
			}
			result = haystack.substring(start, end);
		}
		return (result.equalsIgnoreCase("true"));
	}

	/**
	 * @param haystack
	 * @param tag
	 * @return final string that contains all tag properties
	 */
	public static String parseTagPropertiesIntoString(List<String> haystack,
			String tag) {
		Logger.d(TAG, "Packing all properties for tag: " + tag
				+ " into one line");
		Iterator<String> it = haystack.iterator();

		String line = it.next();
		while (!line.contains(tag) && it.hasNext()) {
			line = it.next();
		}
		StringBuilder builder = new StringBuilder(line);
		while (!line.contains(">")) {
			builder.append(line);
			line = it.next();
		}
		line = builder.toString();

		return line;
	}

	

}
