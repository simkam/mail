/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.cts.greenmail;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Eduardo Martins
 */
public class Main {

    public static void main(String[] args) {
        final String host = System.getProperty("mailHost", "localhost");
        final String user = System.getProperty("mailuser1", "nobody");
        final String password = System.getProperty("javamail.password", "password");
        final Set<ServerSetup> set = new HashSet<ServerSetup>();
        final GreenMail greenMail;

        if (args.length > 0) {
            int smtpPort = 8933;

            int imapPort = 8932;

            if( args.length > 1 && "imap".equals(args[0])) {
                imapPort = Integer.parseInt(args[1]);
            }
            if(args.length > 3 && "smtp".equals(args[2])) {
                smtpPort = Integer.parseInt(args[3]);
            }
            set.add(new ServerSetup(imapPort, host, ServerSetup.PROTOCOL_IMAP));
            set.add(new ServerSetup(smtpPort, host, ServerSetup.PROTOCOL_SMTP));
            System.out.println("imap will listen on port " + imapPort);
            System.out.println("smtp will listen on port " + smtpPort);
            greenMail = new GreenMail(set.toArray(new ServerSetup[2]));
        } else {
            for (ServerSetup serverSetup : ServerSetup.ALL) {
                System.out.println("protocol " + serverSetup.getProtocol() + ", port " + serverSetup.getPort());
                set.add(new ServerSetup(serverSetup.getPort(), host, serverSetup.getProtocol()));
            }
            greenMail = new GreenMail(set.toArray(new ServerSetup[ServerSetup.ALL.length]));
        }

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                greenMail.stop();
                System.out.println("GreenMail stopped.");
            }
        });
        greenMail.setUser(user + "@" + host, user, password);
        greenMail.start();
        System.out.println("GreenMail started.");
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
