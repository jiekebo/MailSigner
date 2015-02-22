# MailSigner client

The client is a simple taskbar application which uses UDP multicasting to locate the server. Upon locating the server, the client can load data and create the users signatures, ready for pasting into emails.

# MailSigner persistence library

This project creates the persistence objects used for database access.

# MailSigner server

Server for creating, managing and distributing mail signatures to clients. Server uses HSQL-DB and Eclipse-Link for store and persistence.
A signature comes in three varieties: HTML, RTF and raw text. The signatures are automatically filled with details of each user, which is
either entered manually in the server gui, or retreived from an active directory or ldap.

The server supports UDP-multicast service, which the clients automatically will hook onto, and download signature data via webservice calls.

# Word Processor

A Swing gui component used for RTF editing in MailSigner.
