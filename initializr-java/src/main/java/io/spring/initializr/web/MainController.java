package io.spring.initializr.web;

import io.spring.initializr.ProjectMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Stephane Nicoll
 */
@Controller
public class MainController {

	@Autowired
	private ProjectMetadata projectMetadata;

	@RequestMapping(value = "/")
	@ResponseBody
	public ProjectMetadata projects() {
		return projectMetadata;
	}
}
