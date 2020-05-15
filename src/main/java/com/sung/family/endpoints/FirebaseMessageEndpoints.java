package com.sung.family.endpoints;

import java.sql.ResultSet;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.mysql.cj.jdbc.PreparedStatement;
import com.sung.family.constants.AppMetadata;
import com.sung.family.data.MysqlConnect;
import com.sung.family.models.HttpCourierResponse;
import com.sung.family.models.Review;
import com.sung.family.models.StatusType;
import com.sung.family.models.User;
import com.sung.family.response.CustomResponse;
import com.sung.family.utils.Utils;


@Path("/firebase")
public class FirebaseMessageEndpoints
{
	private static final Gson gs = new Gson();
	
	@POST
	@Path("/v1/auth")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(String payload)
	{
		HttpCourierResponse<User> response = new HttpCourierResponse<User>();
		try
		{
			User user = gs.fromJson(payload, User.class);
			MysqlConnect connect = new MysqlConnect(AppMetadata.FORUM_DB);
	
			PreparedStatement ps = (PreparedStatement) connect.getConnect().prepareStatement("SELECT * FROM user WHERE Email=?");
			ps.setString(1, user.getEmail());
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				// login successful
				user.setId(rs.getInt("Id"));
				user.setFullName(rs.getString("FullName"));
				response.result = user;
			}
			else {
				return CustomResponse.serverErrorResponse(response);
			}
			
			ps = (PreparedStatement) connect.getConnect().prepareStatement("UPDATE user Set IsLogin = 1 WHERE Id=?");
			ps.setInt(1, user.getId());
			int result = ps.executeUpdate();
			if(result < 0) {
				//update fails...
				return CustomResponse.serverErrorResponse(response);
			}
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			return CustomResponse.serverErrorResponse(response, ex);
		}
		return CustomResponse.regularResponse(response);
	}	
	
	@GET
	@Path("/v1/send/message")
	@Produces(MediaType.APPLICATION_JSON)
	public Response sendFirebaseMessage(@QueryParam("userId") Integer userId, @QueryParam("message") String message)
	{
		HttpCourierResponse<String> response = new HttpCourierResponse<String>();
		MysqlConnect connect = new MysqlConnect(AppMetadata.FORUM_DB);
		try
		{
			PreparedStatement ps = (PreparedStatement) connect.getConnect().prepareStatement("SELECT * FROM user WHERE Id=?");
			if(userId == 1)
			{
				ps.setInt(1, 2);
			}
			else if(userId == 2) {
				ps.setInt(1, 1);
			}
		
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				response.result = rs.getString("FirebaseToken");
			}
			else {
				return CustomResponse.serverErrorResponse(response);
			}
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		String ackResponse = Utils.getInstance().postToGCM(AppMetadata.GCM_SERVER_URL, 
				AppMetadata.getAPIKey(), 
				Utils.getInstance().createSimpleMessage( response.result, message));
		
		System.out.println(ackResponse);
		return CustomResponse.regularResponse(response);
	}
	
	
	@POST
	@Path("/v1/write/review")
	@Produces(MediaType.APPLICATION_JSON)
	public Response writeReview(String payload)
	{
		Review review = new Gson().fromJson(payload, Review.class);
		String ackResponse = Utils.getInstance().postToGCM(AppMetadata.GCM_SERVER_URL, 
				AppMetadata.getAPIKey(), 
				Utils.getInstance().createReview( review.getRecipientFbToken(), review));
		System.out.println(ackResponse);
		HttpCourierResponse response = new HttpCourierResponse();
		response.setResponseCode(StatusType.OK);
		return CustomResponse.regularResponse(response);
	}
	
}
