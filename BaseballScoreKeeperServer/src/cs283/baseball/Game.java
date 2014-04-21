package cs283.baseball;


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/Game")
public class Game extends HttpServlet {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BaseballGame currentGame;

    public Game() {
    	this.currentGame = new BaseballGame();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
	    PrintWriter out = response.getWriter();

	    if(request.getParameter("type").startsWith("update")){
	    	
	    	this.currentGame.setAwayTeamErrors(Integer.parseInt(request.getParameter("AWAYERRORS")));
		    this.currentGame.setAwayTeamHits(Integer.parseInt(request.getParameter("AWAYHITS")));
		    this.currentGame.setAwayTeamName(request.getParameter("AWAYNAME"));
		    this.currentGame.setAwayTeamRuns(Integer.parseInt(request.getParameter("AWAYRUNS")));
		    
		    this.currentGame.setHomeTeamErrors(Integer.parseInt(request.getParameter("HOMEERRORS")));
		    this.currentGame.setHomeTeamHits(Integer.parseInt(request.getParameter("HOMEHITS")));
		    this.currentGame.setHomeTeamName(request.getParameter("HOMENAME"));
		    this.currentGame.setHomeTeamRuns(Integer.parseInt(request.getParameter("HOMERUNS")));
		    
		    this.currentGame.setInningNumber(Integer.parseInt(request.getParameter("INNING")));
		    
		    if (request.getParameter("HALFINNING").startsWith("TOP")) {
		    	 this.currentGame.setHalfInning(0);
		    } else {
		    	this.currentGame.setHalfInning(1);
		    }

		    this.currentGame.setOuts(Integer.parseInt(request.getParameter("OUTS")));
		    this.currentGame.setStrikes(Integer.parseInt(request.getParameter("STRIKES")));
		    this.currentGame.setBalls(Integer.parseInt(request.getParameter("BALLS")));
		    
		    if (request.getParameter("FIRST") == null) {
			    this.currentGame.setFirstBase(false);
		    } else {
			    this.currentGame.setFirstBase(true);
		    }
		    
		    if (request.getParameter("SECOND") == null) {
			    this.currentGame.setSecondBase(false);
		    } else {
			    this.currentGame.setSecondBase(true);
		    }
		    
		    if (request.getParameter("THIRD") == null) {
			    this.currentGame.setThirdBase(false);
		    } else {
			    this.currentGame.setThirdBase(true);
		    }
		    
		    
	    } else if(request.getParameter("type").startsWith("query")){
	    	
	    	response.setContentType("application/json");           
	        response.setHeader("Cache-Control", "no-cache");
	        
	        try {
	        JSONObject object = new JSONObject();
	        	
	        	object.put("AWAYERRORS", this.currentGame.getAwayTeamErrors());
	            object.put("AWAYHITS", this.currentGame.getAwayTeamHits());
	            object.put("AWAYNAME", this.currentGame.getAwayTeamName());
	            object.put("AWAYRUNS", this.currentGame.getAwayTeamRuns());
	            
	            object.put("HOMEERRORS", this.currentGame.getHomeTeamErrors());
	            object.put("HOMEHITS", this.currentGame.getHomeTeamHits());
	            object.put("HOMENAME", this.currentGame.getHomeTeamName());
	            object.put("HOMERUNS", this.currentGame.getHomeTeamRuns());
	            
	            object.put("INNING", this.currentGame.getInningNumber());
	            object.put("HALFINNING", this.currentGame.getHalfInning());
	            object.put("OUTS", this.currentGame.getOuts());
	            object.put("STRIKES", this.currentGame.getStrikes());
	            object.put("BALLS", this.currentGame.getBalls());
	            
	            object.put("FIRST", this.currentGame.getFirst());
	            object.put("SECOND", this.currentGame.getSecond());
	            object.put("THIRD", this.currentGame.getThird());

	        
	            response.getWriter().write(object.toString());

	        } catch (JSONException e) {
	        	
	        }
	        
	    } else {
	    	out.println("Unknown Type");
	    }
		
	    out.close();
	}
	
}
