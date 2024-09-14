package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.User_DTO;
import entity.Address;
import entity.City;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@WebServlet(name = "Pcheckout", urlPatterns = {"/Pcheckout"})
public class Pcheckout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJsonObject = gson.fromJson(request.getReader(), JsonObject.class);

        JsonObject responseJsonObject = new JsonObject();
        responseJsonObject.addProperty("success", false);

        HttpSession httpSession = request.getSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        boolean currentAddressCheked = requestJsonObject.get("currentAddressCheked").getAsBoolean();
        String first_name = requestJsonObject.get("first_name").getAsString();
        String last_name = requestJsonObject.get("last_name").getAsString();
        String address1 = requestJsonObject.get("address1").getAsString();
        String address2 = requestJsonObject.get("address2").getAsString();
        String postal_Code = requestJsonObject.get("postal_Code").getAsString();
        String mobile = requestJsonObject.get("mobile").getAsString();
        String city_id = requestJsonObject.get("city_id").getAsString();

//        System.out.println(currentAddressCheked);      
//        System.out.println(first_name);
//        System.out.println(last_name);
//        System.out.println(address1);
//        System.out.println(address2);
//        System.out.println(postal_Code);     
//        System.out.println(mobile);
//        System.out.println(city_id);

        if (httpSession.getAttribute("user") != null) {
            //search user
            User_DTO user_DTO = (User_DTO) httpSession.getAttribute("user");
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", user_DTO.getEmail()));
            User user = (User) criteria1.uniqueResult();

            if (currentAddressCheked) {
                //current address

                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);

                if (criteria2.list().isEmpty()) {
                    responseJsonObject.addProperty("message", "Current Address Not Found. Please Create a New Address");
                } else {
                    //get current address ek
                    Address address = (Address) criteria2.list().get(0);
                }
            } else {
                //new address

                if (first_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please Fill First Name");
                } else if (last_name.isEmpty()) {
                    responseJsonObject.addProperty("message", "Please Fill Last Name");
                } else if (!Validation.isInteger(city_id)) {
                    responseJsonObject.addProperty("message", "Invalid City Selected");
                } else {
                    //Check city
                    Criteria criteria3 = session.createCriteria(City.class);
                    criteria3.add(Restrictions.eq("id", Integer.parseInt(city_id)));

                    if (criteria3.list().isEmpty()) {
                        responseJsonObject.addProperty("message", "Invalid City Selected");
                    } else {
                        City city = (City) criteria3.list().get(0);

                        if (address1.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please Fill Address Line 1");
                        } else if (address2.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please Fill Address Line 2");
                        } else if (postal_Code.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please Fill Postal Code");
                        } else if (postal_Code.length() !=5) {
                            responseJsonObject.addProperty("message", "Invalid Postal Code");
                        }else if (!Validation.isInteger(postal_Code)) {
                            responseJsonObject.addProperty("message", "Invalid Postal Code");
                        }  else if (mobile.isEmpty()) {
                            responseJsonObject.addProperty("message", "Please Fill Mobile Number");
                        }else if (!Validation.isMobile(mobile)) {
                            responseJsonObject.addProperty("message", "Invalid Mobile Number");
                        }else{
                            //add new Address
                            
                            Address address = new Address();
                            address.setCity(city);
                            address.setFirst_name(first_name);
                            address.setLast_name(last_name);
                            address.setLine1(address1);
                            address.setLine2(address2);
                            address.setMobile(mobile);
                            address.setPostal_code(postal_Code);
                            address.setUser(user);
                            
                            session.save(address);
                        }
                    }
                }

            }

        } else {
            responseJsonObject.addProperty("message", "User Not Sign In");
        }
        
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseJsonObject));

    }

}
