package de.regensburg.oauth.webclient.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String error = request.getParameter("error");
        String errorDescription = request.getParameter("error_description");
        String errorUri = request.getParameter("error_uri");
        String state = request.getParameter("state");

        if (status != null) {
            model.addAttribute("errorStatusDescription", HttpStatus.valueOf(Integer.valueOf(status.toString())).getReasonPhrase()); 
            model.addAttribute("errorStatus", status.toString());
        }
        model.addAttribute("error", error);
        model.addAttribute("errorDescription", errorDescription);
        model.addAttribute("errorUri", errorUri);
        model.addAttribute("state", state);

		return "error";
    }

}
