package com.sung.family.response;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.sung.family.models.HttpCourierResponse;
import com.sung.family.models.StatusType;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CustomResponse
{
	public static Response regularResponse(HttpCourierResponse json)
	{
		json.status = "OK";
		json.setResponseCode(StatusType.OK);
		json.setResponseMessage("success");
		Gson gson = new Gson();
		return Response.status(Status.OK)
					   .entity(gson.toJson(json).toString())
				       .build();	
	}
	
	public static Response serverErrorResponse(HttpCourierResponse json)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.INTERNAL_SERVER_ERROR);
		Gson gson = new Gson();
		return Response.status(Status.INTERNAL_SERVER_ERROR)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
	
	public static Response serverErrorResponse(HttpCourierResponse json, Exception ex)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.INTERNAL_SERVER_ERROR);
		json.setResponseMessage(ex.getMessage());
		Gson gson = new Gson();
		return Response.status(Status.INTERNAL_SERVER_ERROR)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
	
	public static Response conflictingResourceResponse(HttpCourierResponse json)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.CONFLICT);
		Gson gson = new Gson();
		return Response.status(Status.CONFLICT)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
	
	public static Response incorrectMethodOrNotFoundResponse(HttpCourierResponse json)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.NOT_FOUND);
		Gson gson = new Gson();
		return Response.status(Status.NOT_FOUND)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
	
	public static Response unauthorizedResponse(HttpCourierResponse json)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.UNAUTHORIZED);
		Gson gson = new Gson();
		return Response.status(Status.UNAUTHORIZED)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
	
	public static Response badRequestResponse(HttpCourierResponse json)
	{
		json.status = "ERROR";
		json.setResponseCode(StatusType.BAD_REQUEST);
		json.setResponseMessage("Bad request");
		Gson gson = new Gson();
		return Response.status(Status.BAD_REQUEST)
					   .entity(gson.toJson(json).toString())
				       .build();
	}
}
