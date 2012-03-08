package org.scribe.examples;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.XingApi;
import org.scribe.model.*;
import org.scribe.oauth.OAuthService;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class XingExample
{
  private static final String PROTECTED_RESOURCE_URL = "https://api.xing.com/v1/users/me.json";
  
  public static void main(String[] args) throws IOException {
      BufferedOutputStream bos = new BufferedOutputStream(new ByteArrayOutputStream());
      OAuthService service = new ServiceBuilder()
                                .provider(XingApi.class)
                                .apiKey("f2e9a0bb85851c110906")
                                .apiSecret("a6aaad5ca324328191d14c6c6b5b591338542de3")
                                .callback("oob").debugStream(bos)
                                .build();
    Scanner in = new Scanner(System.in);

    System.out.println("=== Xing's OAuth Workflow ===");
    System.out.println();

    // Obtain the Request Token
    System.out.println("Fetching the Request Token...");
    Token requestToken = service.getRequestToken();
    System.out.println("Got the Request Token!");
    System.out.println();

    System.out.println("Now go and authorize Scribe here:");
    System.out.println(service.getAuthorizationUrl(requestToken));
    System.out.println("And paste the verifier here");
    System.out.print(">>");
    Verifier verifier = new Verifier(in.nextLine());
    System.out.println();
    bos.flush();
      //0352
    // Trade the Request Token and Verfier for the Access Token
    System.out.println("Trading the Request Token for an Access Token...");
    Token accessToken = service.getAccessToken(requestToken, verifier);
    System.out.println("Got the Access Token!");
    System.out.println("(if your curious it looks like this: " + accessToken + " )");
    System.out.println();

    // Now let's go and ask for a protected resource!
    System.out.println("Now we're going to access a protected resource...");
    OAuthRequest request = new OAuthRequest(Verb.POST, PROTECTED_RESOURCE_URL);
    request.addBodyParameter("status", "this is sparta! *");
    service.signRequest(accessToken, request);
    Response response = request.send();
    System.out.println("Got it! Lets see what we found...");
    System.out.println();
    System.out.println(response.getBody());

    System.out.println();
    System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
  }

}