package com.masparaga.library.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RedisController {

    public void begin(Model model, HttpSession httpSession){
        model.addAttribute("sessionId", httpSession.getId());
    }

    public boolean addAddress(String address, HttpServletRequest request){
        try{
            request.getSession().setAttribute("address", address);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean addCardInfos(String cardInfos, HttpServletRequest request){
        try{
            request.getSession().setAttribute("cardInfos", cardInfos);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public boolean buyStep(String address, String cardInfos, //requestbody ile apiden alınacak stepler
                        HttpServletRequest request,
                        Model model,
                        HttpSession session){
        begin(model, session);

        if(!addAddress(address, request)){
            return false;
        }
        if(!addCardInfos(cardInfos, request)){
            return false;
        }
        return true;

    }

    public String destroy(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }
}
