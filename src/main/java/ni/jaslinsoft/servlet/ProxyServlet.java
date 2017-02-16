package ni.jaslinsoft.servlet;

import java.io.IOException;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by jfflores on 02-15-17.
 */
public class ProxyServlet extends javax.servlet.http.HttpServlet {

    private static final Logger logger = Logger.getLogger(ProxyServlet.class.getCanonicalName());

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        cargar(request,response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        cargar(request,response);
    }

    private void cargar(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {

        try {
            //http://jaslin-pc.internal.inss.org:8080/JTunelJaslinWeb/cargar?p_miurl=http://www.youtube.com


            String miurl=req.getParameter("p_miurl");
            if(miurl==null)
            {
                miurl="http://www.facebook.com";
            }

            logger.log(Level.WARNING, "\n****p_miurl: " + miurl + " \n****Url:" + req.getParameter("fwdurl"));



            URL fwdURL = new URL(miurl);
            HttpURLConnection fwdConnection =
                    (HttpURLConnection) fwdURL.openConnection();

            for (String key : fwdConnection.getHeaderFields().keySet()) {
                rsp.setHeader(key, fwdConnection.getHeaderField(key));
                logger.log(Level.WARNING, "****key: " +key +", valor: " + fwdConnection.getHeaderField(key));
            }

            //
			/*for (Header header : rsp.getAllHeaders()) {
				String headerName = header.getName();
				String headerValue = header.getValue();
				rsp.addHeader(headerName, rewriteUrlFromResponse(req, headerValue));

			}*/



            copy(fwdConnection.getInputStream(), rsp.getOutputStream());
            logger.log(Level.WARNING, "****Todo Ok...");

        } catch (Exception e) {
            e.printStackTrace(rsp.getWriter());

        }
    }

    private void copy(InputStream in, OutputStream out) throws IOException {
        int len = 0;
        byte[] buf = new byte[1024];
        int contador =0;

        while ((len = in.read(buf)) > 0) {
            String linea = new String(buf);
            System.out.println("Buf Jaslin >> "+linea);
            out.write(buf, 0, len);
            contador++;
        }

        in.close();
    }

}
