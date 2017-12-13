package com.kote;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.model.ZipPath;
import com.pluralsight.repository.ZipFileRepository;

@Path("elasticsearch")
public class ZipFileRESTServices {

	@Inject
	private ZipFileRepository zipFileRepository;
	
	@POST	
	@Path("/getzipcontent")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.APPLICATION_JSON)
	public String getZipFileContent(ZipPath pathToZip) {
		
		String json = zipFileRepository.getZipFileContent(pathToZip);
		return json;
		
	}
}
