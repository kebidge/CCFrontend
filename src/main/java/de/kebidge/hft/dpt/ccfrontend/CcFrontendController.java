package de.kebidge.hft.dpt.ccfrontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;

@Controller
public class CcFrontendController {

    @Value("${user.endpoint}")
    private String userEndpoint;

    @GetMapping("/")
   public String displayPage(Model model) {

       CcFrontendUser[] users = WebClient
           .create(userEndpoint)
           .get()
           .retrieve()
           .bodyToMono(CcFrontendUser[].class)
           .block();

       model.addAttribute("users", users);

       return "cc-frontend-page";
   }

   @PostMapping("/")
   public String addUser(@RequestParam String newuser, Model model) {
   
    CcFrontendUser user = new CcFrontendUser();
       user.username = newuser;
   
       WebClient
           .create(userEndpoint + "add")
           .post()
           .body(Mono.just(user), CcFrontendUser.class)
           .retrieve()
           .bodyToMono(CcFrontendUser.class)
           .block();
   
       return "redirect:/";
   }
    
}
