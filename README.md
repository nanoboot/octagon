# Octagon

## Local setup

1. Go to directory octagon-clazz-loader
2. Execute mvn clean install
3. Go to directory octagon-clazz-loader/target
4. Copy file octagon-clazz-loader-*.jar to lib directory of Tomcat installation directory
5. Copy directory oct.confpath.template to oct.confpath
6. Copy file tomcat/setenv.sh to bin directory of Tomcat installation directory
7. Update file setenv.sh in bin directory to match the correct path of new directory oct.confpath

## Misc

todo - document confpath directory- create also a template

Octagon is a web application (final artifact is war- web archive).

Used tools, technologies and libraries are:
Java 16, Maven, Git, Java module system, Tomcat, Docker, SQlite, Power Framework (mail, json, time, reflection, sql, web, xml), MyBatis, Spring, Struts 2.

Octagon is a smart frontend for database (here the database is SQLite, but there is possible to create implementation for other database types due the use of interfaces).

A custom plugin system is used. Octagon loads all the plugins at the start.
Plugins are responsible for adding database tables, specifying the restriction on using them, adding apps (in Octagon terminology), batches and services to be run on Octagon and adding some modification to the web frontend.

For example, there are some Octagon plugins, which periodically run and execute some reminders based on the current data and send it a mail message report or notification to mail box.

Octagon is still under heavy weekend development. Future features are authentication, authorization, rest interface multiuser system and others.

Octagon uses Power Framework web module, which generates dynamically html code in Java using the current Java entity class representing the table schema and others.

There are some type of pages used to list, create, update, delete, or view an entity.

I know, that generating frontend html code in Java may not be very efficient and if used by thousands of users, it could be not good, but I needed to try to use Power Framework web module.

Spring factories are used to create some Spring beans dynamically based on the current list of loaded plugins.

There is possible to create new Octagon plugins like the ones intended to add new entity types (for example: student, class, subject, teacher, exam) or new batch periodically checking the database content, making some database updates and sending mail reports.

Octagon also partially implements filtering of the list of an entity and saving this filter for later use.
