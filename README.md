# Internet-shop

***Table of Contents***

**[Project purpose](#purpose)**

**[Project structure](#structure)**

**[For developer](#developer)**


<a name="purpose"><h2>Project purpose</h2></a>
This is a template for creating internet shop.

It has login and registration forms.
Authentification uses two roles: ADMIN, USER.
There are next models and controllers for them:

  * item

  * user

  * order

  * bucket

Initial add - for injection mock data,

Add item - for add new item to shop, 

Register User - for registering new user,

Login - for user authentication and authorization,

Logout - for logging out,

All users - for displaying a list of all app users. (it works only for ADMIN)

Show items - for displaying all items in stock. 

  There are two fuction for items: 
  
  - Add - to add item to bucket (it works only for USER) 
  - Delete - to delete item from app (it works only for ADMIN)
  
  Also there is fuction:
  
  - Show bucket - for displaying user’s bucket. (it works only for USER)

Show orders - for displaying user’s order history. (it works only for USER)



<a name="structure"><h2>Project structure</h2></a>

  * Java 11

  * Maven 4.0.0

  * hibernate 5.4.5.Final

  * javax.servlet 3.1.0

  * jstl 1.2

  * log4j 2.13.0

  * maven-checkstyle-plugin

<a name="developer"><h2>For developer</h2></a>

Open the project in your IDE.

Add it as maven project.

Configure Tomcat:

add artifact;

add sdk 11.0.3;

Add sdk 11.0.3 in project struсture.

Use file internetshop.src.main.resources.init_db.sql to create the schema and all tables required by this app.

At internetshop.factory.Factory class use username, password and name of DB to create a Connection.

Change a path in internetshop.src.main.resources.log4j.properties. It has to reach your logFile.

Be careful the foreign key option for order_id at the order_item table is cascade on delete. 

Run the project.

By default there’s one user with a ADMIN role (login = Admin, password = 123).