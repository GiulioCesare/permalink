package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Permalink
 */
public class Permalink extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Properties properties=null;       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Permalink() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
        String bid=request.getParameter("bid");  
        //response.sendRedirect("https://www.google.co.in/#q="+bid);  
        String targetBid = getTargetBid(bid);
        
//        response.sendRedirect("http://www.sbn.it/opacsbn/opaclib?select_db=solr_iccu&searchForm=opac%2Ficcu%2Favanzata.jsp&do_cmd=search_show_cmd&db=solr_iccu&Invia=Avvia+la+ricerca&saveparams=false&resultForward=opac%2Ficcu%2Ffull.jsp&nentries=1&rpnlabel=+Identificativo+SBN+%3D+MOD0945354+%28parole+in+AND%29+&rpnquery=%2540attrset%2Bbib-1%2B%2B%2540attr%2B1%253D1032%2B%2540attr%2B4%253D6%2B%2522"+targetBid+"%2522&&fname=none&from=1");
        String redirect_url = properties.getProperty("redirect_url").replaceAll("#target_bid#", targetBid);
        response.sendRedirect(redirect_url);
	
	}

    String getTargetBid(String bid)
    {
    	String targetBid = "";
    	Connection con=null;
    	// WE REALLY SHOULD NEED A CONNECTION POOL!!.. TODO
        try
        {
        	String driver = properties.getProperty("jdbc_driver");
            Class.forName(driver); // "oracle.jdbc.OracleDriver"
             
            //con=DriverManager.getConnection("jdbc:oracle:thin:@10.30.1.54:1521:INDSVI","multimateriale","multimateriale");
//            String connection = properties.getProperty("jdbc_connection");
            String connection = properties.getProperty("col_jdbc_connection");
            
            
            String user = properties.getProperty("jdbc_user");
            String pwd = properties.getProperty("jdbc_pwd");
            con=DriverManager.getConnection(connection, user, pwd);
            Statement st=con.createStatement();
//             System.out.println("connection established successfully...!!");     
 
             ResultSet rs=st.executeQuery("select bid_new from tb_permalink where bid_old = '"+bid+"'");
             if (rs.next())
             {
            	 targetBid = rs.getString(1);
             }
             else
             {
            	 if (con!=null)
            		 con.close();
            	 return bid;
             }
             
           	 con.close();
        }
        catch (Exception e){
	       	 if (con!=null)
				try {
					con.close();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            e.printStackTrace();
            // TODO 
        }
    	
//    	return "MOD0945354";
     return targetBid; 
    }
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}


	public Object getPropery(String key) throws IOException{
		if (properties == null)
			return properties; 
		return properties.get(key); 
    }	
	
	
	public void init() {
		// Configure via property file 

		properties = new Properties();
		try {
			properties.load(getServletContext().getResourceAsStream("/WEB-INF/config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	}
	
}
