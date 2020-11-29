# Scheduler

Scheduler is an application that stores contacts and appointments into a database. Users can add, edit, and delete customers and appointments.
There are two types of customers that can be created: homeowners and apartment managers. The two types of customers have 
attributes that are different from each other. Users can also generate reports to view appointments by contact, customer, or appointment type.


## Application Information

Author: Ben Garding

Contact: bengarding@gmail.com

Version: 1.0

Date: 11-28-2020


## Built With

IDE: IntelliJ IDEA 2020.2.3 (Ultimate Edition)

JDK: 11.0.8

JavaFX: 11.0.2

JDBC: MySQL 8.0.21


## Directions

To use this application, first log in with the credentials of 'admin' for both username and password. When you reach the main page, 
you can switch between the tabs to show either appointments or contacts in the database. From the menu bar, you can click 
'New' to create a new appointment or contact. From the 'Edit' menu, you can edit or delete an appointment or contact that 
has been selected in the table. From the 'Reports' menu, you can view counts of appointment by type, customer or contact. 
You can also view appointments for a given customer or contact.

## Other Information

<li> The login form translates into French if the user's locale is set to French
<li> The appointments can be viewed by month or week, depending on which radio button
is pressed.
<li> The customer view contains a search bar to search for customers by name
<li> The IDs for both customers and appointments are auto-generated and cannot be edited from the GUI
<li> Verification is in place throughout the application to ensure data integrity