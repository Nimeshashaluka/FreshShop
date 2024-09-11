package controller;

import com.google.gson.Gson;
import dto.Response_DTO;
import entity.Category;
import entity.Model;
import entity.productitemsize;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import model.HibernateUtil;
import model.Validation;
import org.hibernate.Session;

@MultipartConfig
@WebServlet(name = "AddProduct", urlPatterns = {"/AddProduct"})
public class AddProduct extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Response_DTO response_DTO = new Response_DTO();
        Gson gson = new Gson();

        String titleId = request.getParameter("titleId");
        String descriptionId = request.getParameter("descriptionId");
        String categoryId = request.getParameter("categoryId");
        String modelId = request.getParameter("modelId");
        String productSizeId = request.getParameter("productSizeId");
        String priceSelectId = request.getParameter("priceSelectId");
        String quantitySelectId = request.getParameter("quantitySelectId");

        Part imageId = request.getPart("imageId");

        Session session = HibernateUtil.getSessionFactory().openSession();

        if (titleId.isEmpty()) {
            response_DTO.setContent("Please Fill Product Title");
        } else if (descriptionId.isEmpty()) {
            response_DTO.setContent("Please Fill Product Description");
        } else if (!Validation.isInteger(categoryId)) {
            response_DTO.setContent("Invalid Category");
        } else if (!Validation.isInteger(modelId)) {
            response_DTO.setContent("Invalid Model");
        } else if (!Validation.isInteger(productSizeId)) {
            response_DTO.setContent("Invalid Weight");
        } else if (priceSelectId.isEmpty()) {
            response_DTO.setContent("Please Fill Product Price");
        } else if (!Validation.isDouble(priceSelectId)) {
            response_DTO.setContent("Invalid Price");
        } else if (Double.parseDouble(priceSelectId) <= 0) {
            response_DTO.setContent("Price Must Be Greater than 0");
        } else if (quantitySelectId.isEmpty()) {
            response_DTO.setContent("Please Fill Product Quantity");
        } else if (!Validation.isInteger(quantitySelectId)) {
            response_DTO.setContent("Invalid Quantity");
        } else if (Integer.parseInt(quantitySelectId) <= 0) {
            response_DTO.setContent("Quantity Must Be Greater than 0");
        } else if (imageId.getSubmittedFileName() == null) {
            response_DTO.setContent("Please Upload Product Image");
        } else {
            Category category = (Category) session.get(Category.class, Integer.parseInt(categoryId));

            if (category == null) {
                response_DTO.setContent("Please Select Valid Category");
            } else {
                Model model = (Model) session.get(Model.class, Integer.parseInt(modelId));

                if (model == null) {
                    response_DTO.setContent("Please Select Valid Model");
                } else {
                    if (model.getCategory().getId() != category.getId()) {
                        response_DTO.setContent("Please Select Valid Model");

                    } else {
                        productitemsize psize = (productitemsize) session.get(productitemsize.class, Integer.parseInt(productSizeId));

                        if (psize == null) {
                            response_DTO.setContent("Please Select Valid Weight");
                        }else{
                            
                        }
                    }
                }
            }

        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(response_DTO));
        System.out.println(gson.toJson(response_DTO));
    }

}
