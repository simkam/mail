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

/**
 * @author Eduardo Martins
 */
public class Main {

    public static void main(String[] args) {
        final String host = System.getProperty("mailHost", "localhost");
        final String user = System.getProperty("mailuser1", "nobody");
        final String password = System.getProperty("javamail.password", "password");
        final int smptPort = Integer.getInteger("smtp.port", 25);
        final int imapPort = Integer.getInteger("imap.port", 143);

        ServerSetup[] setup = new ServerSetup[] {
                new ServerSetup(imapPort, host, ServerSetup.PROTOCOL_IMAP),
                new ServerSetup(smptPort, host, ServerSetup.PROTOCOL_SMTP)
        };

        for (ServerSetup serverSetup : setup) {
            System.out.println("bind address " + host + ", protocol " + serverSetup.getProtocol() + ", port " + serverSetup.getPort());
        }

        final GreenMail greenMail = new GreenMail(setup);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                greenMail.stop();
                System.out.println("GreenMail stopped.");
            }
        });
        System.out.println("user " + user);
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
