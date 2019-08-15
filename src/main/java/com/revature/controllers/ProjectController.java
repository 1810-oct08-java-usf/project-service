	package com.revature.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.revature.exceptions.InvalidStatusException;
import com.revature.exceptions.ProjectNotAddedException;
import com.revature.exceptions.ProjectNotFoundException;
import com.revature.models.Project;
import com.revature.models.ProjectDTO;
import com.revature.models.ProjectErrorResponse;
import com.revature.services.ProjectService;

/**
 * The ProjectController maps service endpoints for essential CRUD operations on Projects
 */
@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ProjectController {

	private ProjectService projectService;

	@Autowired
	public ProjectController(ProjectService projectService) {
		this.projectService = projectService;
	}
	
	
	

	/**
	 * This method retrieves all of the projects stored within embedded MongoDB Uses
	 * HTTP method GET and only retrieves JSON data.
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 *  @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public List<Project> getAllProjects() {
		if(projectService.findAllProjects() == null) {
			throw new ProjectNotFoundException("There are no projects in the database.");
		}
		return projectService.findAllProjects();
	}

	/**
	 * This method retrieves project by ID Uses HTTP method GET and only retrieves
	 * JSON data
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * @param id: String that serves as the id for the project
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 *  @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public Project getProjectById(@PathVariable String id) {
		if(projectService.findById(id) == null) {
			throw new ProjectNotFoundException("There is no project with id: " + id + ", in the database.");
		}
		return projectService.findById(id);
	}

	
	@GetMapping(value = "/userId/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<Project> getProjectByUserId(@PathVariable Integer userId) {
        System.out.println("In Project Controller getProjectById "+ userId);
        if(projectService.findByUserId(userId) == null) {
            throw new ProjectNotFoundException("There is no project with userId: " 
            										+ userId + ", in the database.");
        }
        return projectService.findByUserId(userId);
    }
	
	/**
	 * This method retrieves project by name Uses HTTP method GET and only retrieves
	 * JSON data
	 * 
	 *Added Spring Security annotations to prevent unauthorized users from accessing database 
	 * 
	 * @param name: String that serves as the name of the project
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public List<Project> getProjectsByName(@PathVariable String name) {
		if(projectService.findByName(name) == null) {
			throw new ProjectNotFoundException("There is no project named: " + name + ", in the database.");
		}
		return projectService.findByName(name);
	}

	/**
	 * This method retrieves project by batch Uses HTTP method GET and only
	 * retrieves JSON data
	 * 
	 * Added Spring Security annotations to prevent outside users from accessing database
	 * 
	 * @param batch: String that serves as the batch for the project
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@GetMapping(value = "/batch/{batch}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN', 'USER')")
	public List<Project> getProjectsByBatch(@PathVariable String batch) {
		List<Project> result = projectService.findByBatch(batch);
		if(result.isEmpty()) {
			throw new ProjectNotFoundException("There is no project associated with batch: " + batch + ", in the database.");
		}
		return projectService.findByBatch(batch);
	}

	/**
	 * This method retrieves project by status Uses HTTP method GET and only
	 * retrieves JSON data
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * @param status: String that serves as the status of the project
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@GetMapping(value = "/status/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN')")
	public List<Project> getProjectsByStatus(@PathVariable String status) {
		if(projectService.findByStatus(status) == null) {
			throw new ProjectNotFoundException("There are currently no projects with status: " + status + ", in the database");
		}
		return projectService.findByStatus(status);
	}
	
	
	/**
     * This method allows a user to submit an edit request on
     * one of their projects.   
     * Uses HTTP method POST and only consumes JSON data.
     * 
     * @param project: The new project object for the submitted edit request.
     * 
     * @author Mikaela Enters (1905-May20-Java-USF)
     * @author Tevin Thomas (1905-May20-Java-USF)
     * @author Aisha Hodge (1905-May20-Java-USF)
     * @author Matthew Alston (1905-May20-Java-USF)
     */
    @PostMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public boolean submitEditRequest(@RequestBody Project project) {
        return projectService.submitEditRequest(project);
    }
    

	/**
	 * 
	 * This method accepts each field of a ProjectDTO object in the form of multipart form data.
	 * A ProjectDTO object is created from the fields and sent to the service layer to be converted 
	 * to a Project object and saved.
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * @param name the name field of the form data
	 * @param batch the batch field of the form data
	 * @param trainer the trainer field of the form data
	 * @param groupMembers the groupMembers field of the form data
	 * @param screenShots the screenShots field of the form data
	 * @param zipLinks the zipLinks field of the form data
	 * @param description the description field of the form data
	 * @param techStack the techStack field of the form data
	 * @param status the status field of the form data
	 * 
	 * @return project: The Project object derived from ProjectDTO in the service layer. 
	 * @author Bjorn Pedersen (190107-Java-Spark-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 *  @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	
	@PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
//	@PreAuthorize("hasRole('USER')")
	public Project addProject
	( 
		@RequestParam("name") String name,
		@RequestParam("batch") String batch,
		@RequestParam("trainer") String trainer, 
		@RequestParam("groupMembers") List<String> groupMembers,
		@RequestParam("screenShots") List<MultipartFile> screenShots, 
		@RequestParam("zipLinks") List<String> zipLinks,
		@RequestParam("description") String description, 
		@RequestParam("techStack") String techStack, 
		@RequestParam("status") String status,
		@RequestParam("dataModel") List<MultipartFile> dataModel,
		@RequestParam("userId") Integer userId
	) 
	{
		if (name == null || name.equals(""))
			throw new ProjectNotAddedException("The 'name' input cannot be empty when adding project");
		if (batch == null || batch.equals(""))
			throw new ProjectNotAddedException("The 'batch' input cannot be empty when adding project");		
		if (techStack == null || techStack.equals(""))
			throw new ProjectNotAddedException("The 'tech stack' input cannot be empty when adding project");
		
		ProjectDTO projectDTO = new ProjectDTO.ProjectDTOBuilder()
		.setName(name)
		.setBatch(batch)
		.setTrainer(trainer)
		.setGroupMembers(groupMembers)
		.setScreenShots(screenShots)
		.setZipLinks(zipLinks)
		.setDescription(description)
		.setTechStack(techStack)
		.setStatus(status)
		.setDataModel(dataModel)
		.setUserId(userId)
		.build();
			
		return projectService.createProjectFromDTO(projectDTO);
	}
		
	/*
	 * This method adds a new project.
	 * 
	 * Uses HTTP method POST. Retrieves form data because this method have values
	 * that are collection.
	 * 
	 * @param name: Requests a String that specifies the name from whatever hits
	 * this endpoint.
	 * 
	 * @param batch: Requests a String that specifies the batch from whatever hits
	 * this endpoint.
	 * 
	 * @param trainer: Requests a String that specifies the trainer from whatever
	 * hits this endpoint.
	 * 
	 * @param groupMembers: Requests a list collection of Strings that specifies the
	 * groupMembers from whatever hits this endpoint.
	 * 
	 * @param screenShots: Requests a list collection of MultipartFile that
	 * specifies the screenShots from whatever hits this endpoint.
	 * 
	 * @param zipLinks: Requests a list collection of Strings that specifies the
	 * zipLinks from whatever hits this endpoint.
	 * 
	 * @param description: Requests a String that specifies the description from
	 * whatever hits this endpoint.
	 * 
	 * @param techStack: Requests a String that specifies the techStack from
	 * whatever hits this endpoint.
	 * 
	 * @param status: Requests a String that specifies the status from whatever hits
	 * this endpoint.
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 */

//	public Project addProject(// TODO should be retrieved from auth service
//			@RequestParam String name, 
//			@RequestParam String batch,
//			@RequestParam String trainer, 
//			@RequestParam List<String> groupMembers,
//			@RequestParam List<MultipartFile> screenShots, 
//			@RequestParam List<String> zipLinks,
//			@RequestParam String description, 
//			@RequestParam String techStack, 
//			@RequestParam String status) {
//		ProjectDTO projectDTO = new ProjectDTO();
//
//		projectDTO.setName(name);
//		projectDTO.setBatch(batch);
//		projectDTO.setTrainer(trainer);
//		projectDTO.setScreenShots(screenShots);
//		projectDTO.setGroupMembers(groupMembers);
//		projectDTO.setZipLinks(zipLinks);
//		projectDTO.setDescription(description);
//		projectDTO.setTechStack(techStack);
//		projectDTO.setStatus(status);
//
//		if (projectDTO.getBatch() == null)
//			throw new ProjectNotAddedException("The 'batch' input cannot be null when adding project");
//		if (projectDTO.getName() == null)
//			throw new ProjectNotAddedException("The 'name' input cannot be null when adding project");
//		if (projectDTO.getTechStack() == null)
//			throw new ProjectNotAddedException("The 'tech stack' input cannot be null when adding project");
//
//		return projectService.createProjectFromDTO(projectDTO);
//	}

	/**
	 * This method is used to delete an entry into the embedded MongoDB based on the
	 * ID
	 * 
	 * Uses HTTP method DELETE and only retrieves JSON data
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * @param id: String that serves as the id for the project
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF)
	 */
	@DeleteMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN')")
	public Boolean deleteById(@PathVariable String id) {
		Project ID = projectService.findById(id);
		if (ID == null) {
			throw new ProjectNotFoundException("Project with id: " + id + ", cannot be found to delete this project.");
		}
		return projectService.deleteById(id);
	}

	/**
	 * This method is used to update an entry into the embedded MongoDB based on the
	 * ID
	 * 
	 * Added Spring Security annotations to prevent unauthorized users from accessing database
	 * 
	 * Uses HTTP method PUT. Retrieves and produces JSON data
	 * 
	 * @param project: Requests that the user enters a project
	 * 
	 * @param id: String that serves as the id for the project
	 * 
	 * 
	 * @author Sadiki Solomon (1810-Oct08-Java-USF)
	 * @author Michael Grammens (1810-Oct22-Java-USF)
	 * @author Bronwen Hughes (1810-Oct22-Java-USF)
	 * @author Phillip Pride (1810-Oct22-Java-USF)
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * @author Stuart Pratuch & Tucker Mitchell (190422-JAVA-SPARK-USF) 
	 */
	// TODO should let you update screenshots and repositories
	// TODO If the project is approved, it will keep a version of the old approved project.
	@PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
//	@PreAuthorize("hasRole('ADMIN')")
	public Boolean updateProject(@RequestBody Project project, @PathVariable String id) {
		Project backendProject = projectService.findById(id);
		//check that the project exists
		if (backendProject == null) {	
			throw new ProjectNotFoundException("Project with id: " + id + ", cannot be found to update this project.");
		}
		//check that status is valid
		if(!project.getStatus().equalsIgnoreCase("approved") && !project.getStatus().equalsIgnoreCase("denied") && !project.getStatus().equalsIgnoreCase("pending")) {
			throw new InvalidStatusException("Status: " + project.getStatus() + ", is unacceptable.");
		}
		
			return projectService.updateProject(project, id);
		
		
	}

	/**
	 * This method is used to send a status code into the client based on the
	 * validity of the information sent.
	 * 
	 * Exception Handler for Response Status Not found which is used for findById()
	 * [/{id}] & deleteById() [delete/{id}]
	 * 
	 * Uses @ExceptionHandler annotation. Creates a new error response
	 * error.setStatus: Defines the value of the status code returned if
	 * thrown(NOT_FOUND) error.setMessage: Defines a custom message sent to the
	 * client if the exception is thrown error.setTimeStamp: Defines the time this
	 * error was thrown
	 * 
	 * @author Miles LaCue (1810-Oct08-Java-USF)
	 * /
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)

	public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnfe) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(pnfe.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		return error;
	}

	/**
	 * This method is used to send a status code into the client based on the
	 * validity of the information sent.
	 * 
	 * Exception Handler for Response Status Bad Request which is used for
	 * addProject() [/add]
	 * 
	 * Uses @ExceptionHandler annotation. Creates a new error response
	 * error.setStatus: Defines the value of the status code returned if thrown
	 * (BAD_REQUEST) error.setMessage: Defines a custom message sent to the client
	 * if the exception is thrown error.setTimeStamp: Defines the time this error
	 * was thrown
	 * 
	 * @author Miles LaCue (1810-Oct08-Java-USF)
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProjectErrorResponse handleExceptions(ProjectNotAddedException pnae) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(pnae.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		return error;
	}
	
	@ExceptionHandler
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ProjectErrorResponse handleExceptions(ProjectNotFoundException pnae) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.NOT_FOUND.value());
		error.setMessage(pnae.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		return error;
	}
	
	/**
	 * This method is used to send a status code into the client based on the
	 * validity of the information sent.
	 * 
	 * Exception Handler for Invalid Status Response which is used for updateProject()
	 * 
	 * Uses @ExceptionHandler annotation. Creates a new error response
	 * error.setStatus: Defines the value of the status code returned if
	 * thrown(BAD_REQUEST) error.setMessage: Defines a custom message sent to the
	 * client if the exception is thrown error.setTimeStamp: Defines the time this
	 * error was thrown
	 * 
	 * @author Austin Bark & Kevin Ocampo (190422-Java-Spark-USF)
	 * 
	 */
	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ProjectErrorResponse handleExceptions(InvalidStatusException ise) {
		ProjectErrorResponse error = new ProjectErrorResponse();
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setMessage(ise.getMessage());
		error.setTimeStamp(System.currentTimeMillis());
		return error;
	}

}
