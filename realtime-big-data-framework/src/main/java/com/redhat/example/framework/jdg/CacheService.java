/**
 * 
 */
package com.redhat.example.framework.jdg;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * @author kunallimaye
 *
 */

/**
 * A rest-ws endpoint to expose the functionality as a RESTful service
 */
@Path("/")
public class CacheService {

	@EJB
	JdgLibServiceBean jdgService;
	
	@POST
	@Path("{cacheName}/{key}/{value}/{time}")
	public Response insertDataIntoCache(@Context UriInfo info,
										@PathParam("cacheName") String cacheName,
										@PathParam("key") String key,
										@PathParam("value") String value,
										@PathParam("time") @DefaultValue("10") String time)
//										,
//										@PathParam("timeUnit") @DefaultValue(TimeUnit.SECONDS) TimeUnit timeUnit)
										{
		String rawPath = info.getAbsolutePath().getRawPath().replace(cacheName, key);
        UriBuilder uriBuilder = info.getAbsolutePathBuilder().replacePath(rawPath);
        URI uri = uriBuilder.build();
        
        jdgService.get(cacheName).put(key, value, new Long(time).longValue(), TimeUnit.SECONDS);
        return Response.created(uri).build();
	}
}
