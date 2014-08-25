/*
 This file is part of theunibot.

 theunibot is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 theunibot is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with theunibot.  If not, see <http://www.gnu.org/licenses/>.

 Copyright (c) 2014 Unidesk Corporation
 */
package server;

import database.DatabaseWrapper;
import imagedb.Main;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import server.nanohttpd.NanoHTTPD;
import static server.nanohttpd.NanoHTTPD.MIME_HTML;
import static server.nanohttpd.NanoHTTPD.MIME_PLAINTEXT;

/**
 *
 */
public class WebServer extends NanoHTTPD
{

    private static final int PORT = 8080;

    private static final String GET_IMAGE_ID_URI = "/GET_ID";
    private static final String GET_IMAGE_URI = "/GET_IMAGE";
    private static final String GET_COUNT_URI = "/GET_COUNT";
    private static final String FAV_ICO = "/favicon.ico";
    
    private final String XSS_KEY = "Access-Control-Allow-Origin";
    private final String XSS_VALUE = "*";

    public WebServer()
    {
        super(PORT);
    }

    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session)
    {
        DatabaseWrapper dw = DatabaseWrapper.getInstance();
        System.out.println("URI: " + session.getUri());

        String URI = session.getUri().trim();

        //gets a new ID to be used for that message
        if (URI.startsWith(GET_IMAGE_ID_URI))
        {
            JSONObject json = new JSONObject();

            int newImageId = dw.addNewImage();

            json.put("id", newImageId);
            
            return new NanoHTTPD.Response(json.toString());
        }
        //returns the generated image and notates a view of the image
        else if (URI.startsWith(GET_IMAGE_URI + "/"))
        {
            Response response = new Response("");
            response.addHeader(XSS_KEY, XSS_VALUE);
            JSONObject json = new JSONObject();
            //string following the request URI
            try
            {
                int requestID = Integer.parseInt(session.getUri().trim().replaceFirst(GET_IMAGE_URI + "/", ""));
                System.out.println("Request ID: " + requestID);
                response.setMimeType("image/png");

                //if succesfully updated viewcount
                if (dw.incrementViewcount(requestID))
                //gets the file data for the 1x1 generated image
                {
                    response.setData(new FileInputStream(Main.FAV_ICON_FILE_LOCATION));
                    response.setStatus(Response.Status.OK);
                    return response;
                }
                else
                {
                    //error in parsing of number or getting file
                    json.put("error", "Image not found");
                    response = new Response(json.toString());
                    response.addHeader(XSS_KEY, XSS_VALUE);
                    response.setMimeType("application/json");
                    response.setStatus(Response.Status.INTERNAL_ERROR);
                    return response;
                }
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            catch (NumberFormatException ex)
            {
                System.err.println("Parse of URI for int failed");
            }
            //error in parsing of number or getting file
            json.put("error", "Image could not be served");
            response = new Response(json.toString());
            response.setMimeType("application/json");
            response.addHeader(XSS_KEY, XSS_VALUE);
            response.setStatus(Response.Status.INTERNAL_ERROR);
            return response;
        }
        //gets the count for the number of views
        else if (URI.startsWith(GET_IMAGE_URI + "/"))
        {
            Response response = new Response("");
            JSONObject json = new JSONObject();
            int requestID = Integer.parseInt(session.getUri().trim().replaceFirst(GET_IMAGE_URI + "/", ""));
            int viewcount = -1;
            if((viewcount = dw.getViewcount(requestID)) != -1)
            {
                json.put("viewcount", viewcount);
                response = new Response(json.toString());
                response.setStatus(Response.Status.OK);
                response.setMimeType("application/json");
                return response;
            }
            else
            {
                json.put("error", "Viewcount failed");
                response = new Response(json.toString());
                response.setMimeType("application/json");
                response.setStatus(Response.Status.INTERNAL_ERROR);
                return response;
            }
        }
        else if (URI.equals(FAV_ICO))
        {
            try
            {
                return new NanoHTTPD.Response(NanoHTTPD.Response.Status.ACCEPTED, "image/x-icon", new FileInputStream(Main.FAV_ICON_FILE_LOCATION));
            }
            catch (FileNotFoundException ex)
            {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            JSONObject json = new JSONObject();
            json.put("error", "Fav icon load failed.");
            return new NanoHTTPD.Response(json.toString());
        }
        else
        {
            JSONObject json = new JSONObject();
            json.put("error", "Please make a proper request.");
            return new NanoHTTPD.Response(json.toString());
        }
    }
}
